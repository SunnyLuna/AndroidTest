package com.example.commonlibs.utils


import android.content.res.Resources
import android.graphics.*
import android.util.Log
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import kotlin.math.roundToInt

/**
 * Bitmap 工具类
 * @author ZJ
 * created at 2019/11/25 11:18
 * bitmap内存占用计算：按照像素点计算
 */

object BitmapUtils {
    private val logger = LoggerFactory.getLogger("---BitmapUtils")

    fun getBitmap(file: File?): Bitmap? {
        if (file == null) return null
        return BitmapFactory.decodeFile(file.absolutePath)
    }

    /**
     * 位图合成
     */
    @JvmStatic
    fun combineBitmap(background: Bitmap?, foreground: Bitmap): Bitmap? {
        if (background == null) {
            return null
        }
        val bgWidth = background.width
        val bgHeight = background.height
        val fgWidth = foreground.width
        val fgHeight = foreground.height

        val signatureBitmap = Bitmap.createScaledBitmap(
            foreground,
            fgWidth / 3,
            fgHeight / 3,
            true
        )
        val newmap = Bitmap
            .createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newmap)
        val paint = Paint()
        canvas.drawBitmap(background, Matrix(), null)
        canvas.drawBitmap(
            signatureBitmap,
            bgWidth - signatureBitmap.width - 10F,
            (bgHeight - signatureBitmap.height) - 10F,
            paint
        )
        canvas.save()
        canvas.restore()
        return newmap
    }

    /**
     * 字符串转bitmap
     *
     * @param bmpStr 字符串
     * @return bitmap
     */
    @JvmStatic
    fun stringToBitmap(bmpStr: String): Bitmap? {
        // OutputStream out;
        return try {
            val bitmapArray: ByteArray = HexUtils.hexStringToBytes(bmpStr)
            BitmapFactory.decodeByteArray(
                bitmapArray, 0,
                bitmapArray.size
            )
        } catch (e: Exception) {
            null
        }
    }

    /**
     *将string字符串转为bytes
     */
    @JvmStatic
    fun stringToBytes(str: String): ByteArray {
        val bitmap = stringToBitmap(str)
        val baos = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return baos.toByteArray()
    }

    /**
     *将bitmap转为bytes
     */
    @JvmStatic
    fun bitmapToBytes(bitmap: Bitmap?): ByteArray? {
        if (bitmap == null) return null
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return baos.toByteArray()
    }

    /**
     * Bytes to Bitmap
     */
    @JvmStatic
    fun bytesToBitmap(bmpStr: ByteArray): Bitmap? {
        return if (bmpStr.isEmpty()) {
            null
        } else {
            BitmapFactory.decodeByteArray(bmpStr, 0, bmpStr.size)
        }

    }

    /**
     * 按正方形剪裁图片
     * 指定正方形边长
     */
    @JvmStatic
    fun cropBitmap(bitmap: Bitmap, width: Int): Bitmap {
        var width = width
        // 得到图片的宽，高
        val w = bitmap.width
        val h = bitmap.height
        //width最大不能超过长方形的短边
        if (w < width || h < width) {
            width = if (w > h) h else w
        }
        val retX = (w - width) / 2
        val retY = (h - width) / 2

        return Bitmap.createBitmap(bitmap, retX, retY, width, width + 30, null, false)
    }

    /**
     * 从中间截取一个正方形
     */
    fun cropBitmap(bitmap: Bitmap): Bitmap {
        val w = bitmap.width // 得到图片的宽，高
        val h = bitmap.height
        val cropWidth = if (w > h) h else w// 裁切后所取的正方形区域边长

        return Bitmap.createBitmap(
            bitmap, (bitmap.width - cropWidth) / 2,
            (bitmap.height - cropWidth) / 2, cropWidth, cropWidth
        )
    }

    /**
     * 把图片裁剪成圆形
     */
    fun getCircleBitmap(bitmap: Bitmap?): Bitmap? {//把图片裁剪成圆形
        var bitmap: Bitmap? = bitmap ?: return null
        bitmap = cropBitmap(bitmap!!)//裁剪成正方形
        try {
            val circleBitmap = Bitmap.createBitmap(
                bitmap.width,
                bitmap.height, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(circleBitmap)
            val paint = Paint()
            val rect = Rect(
                0, 0, bitmap.width,
                bitmap.height
            )
            val rectF = RectF(
                Rect(
                    0, 0, bitmap.width,
                    bitmap.height
                )
            )
            var roundPx = 0.0f
            roundPx = bitmap.width.toFloat()
            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = Color.WHITE
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            val src = Rect(
                0, 0, bitmap.width,
                bitmap.height
            )
            canvas.drawBitmap(bitmap, src, rect, paint)
            return circleBitmap
        } catch (e: Exception) {
            return bitmap
        }
    }

    /**
     * 质量压缩不会改变图片在内存中的大小，仅仅会减小图片所占用的磁盘空间的大小，因为质量压缩不会改变图片的分辨率，
     * 图片在内存中的大小是根据width*height，一个像素所占用的字节数计算的，宽高没变，在内存中的大小自然不会变，
     * 质量压缩的原理是通过改变图片的位深和透明度来减小图片占用的磁盘空间大小，所以不适合作为缩略图，可以用于保持图片质量的同时
     * 减小图片所占用的磁盘空间大小，另外，png是无损压缩，设置quality无效
     * @param datas ByteArray?
     * @return String
     */
    fun qualityCompress(bitmap: Bitmap?, qualityScore: Int): Bitmap? {
        val bos = ByteArrayOutputStream()
        try {
            bitmap?.compress(Bitmap.CompressFormat.JPEG, qualityScore, bos)
            val bytes = bos.toByteArray()
            val compressBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            bos.flush()
            bos.close()
            return compressBitmap
        } catch (e: IOException) {
            Log.d("---SystemCamera", e.message!!)
            e.printStackTrace()
            return null
        } finally {
            try {
                bos.flush()
                bos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    /**
     * 压缩bitmap至小于100kb
     * @param bitmap Bitmap?
     * @return Bitmap?
     */
    fun qualityCompressTo100(bitmap: Bitmap?): Bitmap? {
        val bos = ByteArrayOutputStream()
        try {
            do {
                bos.reset()
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 50, bos)
            } while (bos.toByteArray().size / 1024 > 100)
            val bytes = bos.toByteArray()
            val compressBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            bos.flush()
            bos.close()
            return compressBitmap
        } catch (e: IOException) {
            Log.d("---SystemCamera", e.message!!)
            e.printStackTrace()
            return null
        } finally {
            try {
                bos.flush()
                bos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * inSampleSize 采样率，默认和最小值为1，大于1时，值为2的幂（不为2的幂，解码器会取与该值最接近的2的幂
     * 通过改变分辨率来减小图片所占用的磁盘空间和内存空间大小，但是采样率只能设置2的n次方，可能图片的最优比例在中间
     * @param res Resources
     * @param id Int
     * @param reqWidth Int
     * @param reqHeight Int
     * @return Bitmap
     */
    fun decodeBitmapFromResource(res: Resources, id: Int, reqWidth: Int, reqHeight: Int): Bitmap {
        val options = BitmapFactory.Options()
        //不获取图片，不加载到内存中，只返回图片属性
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, id, options)
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(res, id, options)
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val width = options.outWidth
        val height = options.outHeight
        var sampleSize = 1
        if (width > reqWidth || height > reqHeight) {
            val widthRatio = (width.toFloat() / reqWidth.toFloat()).roundToInt()
            val heightRatio = (height.toFloat() / reqHeight.toFloat()).roundToInt()
            //
            sampleSize = if (widthRatio > heightRatio) {
                widthRatio
            } else {
                heightRatio
            }
        }
        logger.debug("calculateInSampleSize: 采样率$sampleSize")
        return sampleSize
    }


    /**
     * 缩放压缩  尺寸压缩
     * 通过减少图片的像素来降低图片的磁盘空间大小和内存大小，可以用于缓存缩略图
     * @param bitmap Bitmap?
     * @param ratio Int
     * @return Bitmap?
     */
    fun compress(bitmap: Bitmap?, ratio: Int): Bitmap? {
        if (bitmap == null) return null

        //压缩bitmap到对应尺寸

        val bitmapResult = Bitmap.createBitmap(
            bitmap.width / ratio,
            bitmap.height / ratio,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmapResult)
        val rectf = RectF(0f, 0f, bitmap.width.toFloat() / ratio, bitmap.height.toFloat() / ratio)
        canvas.drawBitmap(bitmap, null, rectf, null)
        return bitmapResult
    }

    fun recycleBitmap(bitmap: Bitmap?) {
        if (bitmap != null && !bitmap.isRecycled) {
            bitmap.recycle()
        }
    }
}