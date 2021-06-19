package com.decard.printlibs.utils


object DCUtils {
    private val TAG = "---DCUtils"



    fun getCount(count: Int): String {
        when {
            count < 10 -> {
                return "000$count"
            }
            count in 10..99 -> {
                return "00$count"
            }
            count in 100..999 -> {
                return "0$count"
            }
            count >= 1000 -> {
                return "$count"
            }
        }
        return ""
    }




    fun idToPass(id: String): String {
        return id.substring(0, 6) + "********" + id.substring(id.length - 4, id.length)
    }

}