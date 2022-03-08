package com.example.commonlibs.utils

import android.graphics.*
import android.util.Base64
import android.util.Log
import java.io.*

object Base64Utils {

    fun replaceBase(str: String): String {
        return str.replace("[\\s*\t\n\r]", "")
    }

    fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

    fun bitmapToBase64(path: String): String {
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        //待处理的图片
        var `in`: InputStream? = null
        var data: ByteArray? = null
        //读取图片字节数组
        try {
            `in` = FileInputStream(path)
            data = ByteArray(`in`.available())
            `in`.read(data)
            `in`.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        //对字节数组Base64编码
//            BASE64Encoder encoder = new BASE64Encoder();
        //返回Base64编码过的字节数组字符串
        return ""
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    fun base64ToBitmap(base64Data: String?): Bitmap? {
        val bytes = Base64.decode(base64Data, Base64.DEFAULT) ?: return null
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    fun base64ToBytes(base64Data: String?): ByteArray? {
        val bytes = Base64.decode(base64Data, Base64.DEFAULT) ?: return null
        return bytes
    }

    /**
     * @param base64Data
     * @return
     */
    fun base64ToFile(base64Data: String?, filePath: String?): File {
        val bytes = Base64.decode(base64Data, Base64.DEFAULT)
        val file = File(filePath)
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        var bos: BufferedOutputStream? = null
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
            bos = BufferedOutputStream(fos)
            bos.write(bytes)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (bos != null) {
                try {
                    bos.close()
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }
            }
            if (fos != null) {
                try {
                    fos.close()
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }
            }
        }
        return file
    }

    @Synchronized
    fun getBase64(datas: ByteArray?): String {
        if (datas == null) {
            Log.d("---SystemCamera", "null")
            return ""
        }
        val stream = ByteArrayOutputStream()
        val image = YuvImage(datas, ImageFormat.NV21, 640, 480, null)
        image.compressToJpeg(Rect(0, 0, 640, 480), 100, stream)

        val bytes = stream.toByteArray()
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        FileUtils.saveMyBitmap("张静", "123", bitmap)
        try {
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            Log.d("---SystemCamera", e.message!!)
            e.printStackTrace()
        } finally {
            try {
                stream.flush()
                stream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return Base64.encodeToString(bytes, 0)
    }


}