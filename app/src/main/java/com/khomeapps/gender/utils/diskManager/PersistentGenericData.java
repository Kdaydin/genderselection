package com.khomeapps.gender.utils.diskManager;


import android.content.Context;

import androidx.annotation.Nullable;

public class PersistentGenericData<T> extends PersistentClass {

    private static final long serialVersionUID = 1L;

    @Persistent
    private T item;

    public PersistentGenericData(Context context, String filename) {
        super(context, filename);
    }

    /**
     *
     */

    T getItem() {
        return item;
    }

    void setItem(T item) {
        this.item = item;
    }

    public synchronized void saveFieldByName(T value) {
        super.saveFieldByName("item", value);
    }

    public synchronized void clearFieldByName() {
        super.clearFieldByName("item");
    }

    @Nullable
    public Object retrieveFieldByName() {
        return super.retrieveFieldByName("item");
    }
}
