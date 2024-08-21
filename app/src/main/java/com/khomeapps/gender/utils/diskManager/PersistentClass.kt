package com.khomeapps.gender.utils.diskManager

import android.content.Context
import android.util.Log
import com.khomeapps.gender.data.entities.base.Empty
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.lang.reflect.Field
import java.util.Collections

internal abstract class PersistentClass : Serializable {
    @Transient
    private lateinit var context: Context
    private lateinit var filename: String

    @Transient
    private var fileInputStream: FileInputStream? = null

    @Transient
    private var objectInputStream: ObjectInputStream? = null

    @Transient
    private var fileOutputStream: FileOutputStream? = null

    @Transient
    private var objectOutputStream: ObjectOutputStream? = null

    private var dataLoaded = false

    @Transient
    private var persistentFields: Array<Field>? = null

    private constructor() {}

    /**
     * Construct a persistent class in relation with a given context, and that will
     * store data in the given filename.
     * @param context the associated context.
     * @param filename the filename which we read/write data.
     */
    constructor(context: Context, filename: String) {
        this.persistentFields = null
        this.context = context
        this.filename = filename

        // This call appears to do actual work, which is a design no-no. However,
        // I do not consider this to be "doing work" because of two reasons:
        //
        // 1. Without it, the saving/loading mechanism has to check things every time
        // 2. It uses introspection only, which does not depend on I/O and should not
        // cause any significant waiting time, and will not throw any exceptions.
        // -- YS

        this.findPersistentFields()
    }

    @Synchronized
    private fun load() {

        if (this.openInput()) {
            for (field in this.persistentFields!!) {
                this.setFieldValue(field, this.readObject())
            }

            this.closeInput()
        }

        this.dataLoaded = true

        this.postLoad()
    }

    /**
     * Called after a load operation. The default implementation does nothing.
     * If you wish to do something after every data load, override this.
     */
    private fun postLoad() {

    }

    @Synchronized
    fun saveFieldByName(name: String, value: Any) {
        if (!this.dataLoaded) {
            this.load()
        }

        this.setFieldValue(this.getFieldByName(name), value)

        this.save()
    }

    @Synchronized
    fun clearFieldByName(name: String) {
        if (!this.dataLoaded) {
            this.load()
        }

        this.clearFieldValue(this.getFieldByName(name), Empty())

        this.save()
    }

    fun retrieveFieldByName(name: String): Any? {
        if (!this.dataLoaded) {
            this.load()
        }

        return this.getFieldValue(this.getFieldByName(name))
    }

    private fun getFieldByName(name: String): Field {
        for (field in this.persistentFields!!) {
            if (field.name == name) {
                return field
            }
        }

        throw IllegalArgumentException("No such field found: $name")
    }

    private fun setFieldValue(field: Field, value: Any?) {
        try {

            field.set(this, value)
        } catch (e: Exception) {
            // This must never happen. If it does, things are going haywire,
            // so we throw an error. After this, the world is unpredictable.
            throw Error("Can not write field of PersistentClass.", e)
        }

    }

    private fun clearFieldValue(field: Field, value: Any?) {
        try {
            field.set(this, value)
        } catch (e: Exception) {
            throw Error("Cannot clear value of field.", e)
        }
    }

    @Synchronized
    private fun save() {
        if (!this.dataLoaded) {
            this.load()
        }

        if (this.openOutput()) {
            for (field in this.persistentFields!!) {
                this.writeObject(this.getFieldValue(field)!!)
            }
        }

        this.postSave()
    }

    /**
     * Called whenever data is saved. The default implementation does nothing.
     * Override this if you want to do something after each time data is saved.
     */

    private fun postSave() {

    }

    private fun getFieldValue(field: Field): Any? {
        try {
            return field.get(this)
        } catch (e: Exception) {
            // This must never happen. If it does, things are going haywire,
            // so we throw an error. After this, the world is unpredictable.
            throw Error("Can not read field of PersistentClass.", e)
        }

    }

    private fun isFieldPersistent(field: Field): Boolean {
        val annotations = field.declaredAnnotations

        for (annotation in annotations) {
            if (annotation is Persistent) {
                return true
            }
        }

        return false
    }

    private fun findPersistentFields() {
        val fieldList = ArrayList<Field>()

        val fields = this.javaClass.declaredFields

        for (field in fields) {
            if (this.isFieldPersistent(field)) {
                field.isAccessible = true
                fieldList.add(field)
            }
        }

        Collections.sort(fieldList, FieldNameComparator())

        this.persistentFields = fieldList.toTypedArray()
    }


    /**
     * Opens the input stream. This will open an input stream for the filename specified
     * in the constructor within the given activity. This method will return true if
     * the operation succeeds, and ic_cross_red if it fails.
     *
     * @return true on success, ic_cross_red on failure.
     */

    @Synchronized
    private fun openInput(): Boolean {
        try {
            this.fileInputStream = this.context.openFileInput(this.filename)
            this.objectInputStream = ObjectInputStream(fileInputStream)
            return true
        } catch (e: FileNotFoundException) {
            this.closeInput()
            return false
        } catch (e: IOException) {
            Log.e("mmt", "Exception caught in PersistentClass.openInput()", e)
            this.closeInput()
            return false
        }

    }

    /**
     * Reads an object from the already open input stream. Will throw a NullPointerException
     * if the input stream is not open.
     * @return the object read from the stream
     */

    @Synchronized
    private fun readObject(): Any? {
        var obj: Any? = null

        try {
            obj = this.objectInputStream!!.readObject()
        } catch (e: ClassNotFoundException) {
            Log.e("mmt", "Error in reading file")
            Log.e("mmt", e.message ?: "")
        } catch (e: IOException) {
            Log.e("mmt", "Error in reading file")
            if (e.message != null) {
                Log.e("mmt", e.message ?: "")
            }
        }

        return obj
    }

    @Synchronized
    private fun closeInput() {
        try {
            if (fileInputStream != null) {
                fileInputStream!!.close()
            }
        } catch (e: IOException) {
            // Ignore
        }

        try {
            if (objectInputStream != null) {
                objectInputStream!!.close()
            }
        } catch (e: IOException) {
            // Ignore
        }

        this.fileInputStream = null
        this.objectInputStream = null
    }

    /**
     * Opens the output stream. This will open an output stream to the filename specified
     * in the constructor within the given activity. This method will return true if
     * the operation succeeds, and ic_cross_red if it fails.
     *
     * @return true on success, ic_cross_red on failure.
     */

    @Synchronized
    private fun openOutput(): Boolean {
        try {
            this.fileOutputStream = context.openFileOutput(this.filename, Context.MODE_PRIVATE)
            this.objectOutputStream = ObjectOutputStream(fileOutputStream)
            return true
        } catch (e: IOException) {
            this.closeOutput()
            return false
        }

    }

    @Synchronized
    private fun writeObject(obj: Any) {
        try {
            objectOutputStream!!.writeObject(obj)
        } catch (e: IOException) {
            Log.e("mmt", "Error in writing file")
            Log.e("mmt", e.message ?: "")
        }

    }

    @Synchronized
    private fun closeOutput() {
        try {
            if (objectOutputStream != null) {
                objectOutputStream!!.close()
            }
        } catch (e: IOException) {
            // Ignore
        }

        try {
            if (fileOutputStream != null) {
                fileOutputStream!!.close()
            }
        } catch (e: IOException) {
            // Ignore
        }

        this.objectOutputStream = null
        this.fileOutputStream = null
    }

    companion object {

        /**
         *
         */
        private const val serialVersionUID = 1L
    }

}
