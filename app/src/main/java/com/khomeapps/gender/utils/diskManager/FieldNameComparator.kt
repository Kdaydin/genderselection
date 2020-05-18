package com.khomeapps.gender.utils.diskManager

import java.lang.reflect.Field

class FieldNameComparator : Comparator<Field> {

    override fun compare(o1: Field, o2: Field): Int {
        return o1.name.compareTo(o2.name)
    }

}