package com.decard.dblibs.convert

data class ClassBean(var classNumber: String, var className: String){
    override fun toString(): String {
        return "ClassBean(classNumber='$classNumber', className='$className')"
    }
}