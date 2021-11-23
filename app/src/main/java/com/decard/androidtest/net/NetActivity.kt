package com.decard.androidtest.net

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.decard.androidtest.R
import com.example.commonlibs.utils.StatusBarUtil
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream


class NetActivity : AppCompatActivity() {
    private val TAG = "---NetActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_net)
        StatusBarUtil.StatusBarLightMode(this)

        val btnUpload = findViewById<Button>(R.id.btn_upload)
        val btnUploads = findViewById<Button>(R.id.btn_uploads)
        val btnDownload = findViewById<Button>(R.id.btn_download)

        btnUpload.setOnClickListener {
            val filePath = Environment.getExternalStorageDirectory().path + "/barcode.jpg"
            val file = File(filePath)
            Log.d(TAG, "onCreate: fileName: ${file.name}")
//            val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            val requestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            val part = MultipartBody.Part.createFormData(
                "fileName",
                file.name,
                requestBody
            )
            WebService.create().uploadOneFile(part).subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d(TAG, "onCreate:onNext $it")
                }, {
                    Log.d(TAG, "onCreate:throwable ${it.message}")
                })
        }

        btnUploads.setOnClickListener {
//            uploadMulOne()
            uploadMulTwo()
        }

        btnDownload.setOnClickListener {
            download()
        }
    }

    private fun download() {
        val subscribe = WebService.create()
            .downloadMP4()
            .subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
            .subscribe({ responseBody ->
                val total = responseBody.contentLength()
                Log.d(TAG, "下载文件大小: ${total}")
                Log.d(TAG, "download: ${responseBody.contentType().toString()}")
                val filename = "TODAY.mp4"
                val destinationFile =
                    File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "TODAY.mp4")

                var byteread: Int
                val inputStream = responseBody.byteStream()

                val fs = FileOutputStream(destinationFile)
                val buffer = ByteArray(1024 * 10)
                var byteSum = 0L
                var startTime = System.currentTimeMillis()
                while (inputStream.read(buffer).also {
                        byteread = it
                    } != -1) {
                    byteSum += byteread //字节数 文件大小
                    if (System.currentTimeMillis() - startTime > 1000) {
                        startTime = System.currentTimeMillis()
                        val progress = byteSum * 1f / total * 100
                        Log.d(TAG, "onCreate: $byteSum")
                        Log.d(TAG, "下载进度: $progress")
                    }
                    fs.write(buffer, 0, byteread)
                }
                fs.flush()
                fs.close()
                inputStream.close()
                val filePath =
                    Environment.getExternalStorageDirectory().absolutePath + File.separator + filename
                val file = File(filePath)
                if (file.exists()) {
                    Log.d(TAG, "downloadFile: 文件存在")
                } else {
                    Log.d(TAG, "downloadFile: 下载文件不存在")
                }
            }, {
                Log.d(TAG, "downloadFileError: ${it.message}")
            })
    }

    private fun fileToPart(filePath: String): MultipartBody.Part? {
        val file = File(filePath)
        if (!file.exists()) {
            return null
        }
        val body = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(
            "fileName",
            file.name,
            body
        )
    }

    private fun uploadMulTwo() {
        val barcodePath = Environment.getExternalStorageDirectory().path + "/barcode.jpg"
        val qrcodePath = Environment.getExternalStorageDirectory().path + "/qr.jpg"
        val qinCodePath = Environment.getExternalStorageDirectory().path + "/qin.jpg"

        val barcodePart = fileToPart(barcodePath)
        val qrcodePart = fileToPart(qrcodePath)
        val qinCodePart = fileToPart(qinCodePath)
        val parts = ArrayList<MultipartBody.Part>()
        parts.add(barcodePart!!)
        parts.add(qrcodePart!!)
        parts.add(qinCodePart!!)

        val md5: RequestBody = "就是MD5".toRequestBody("text/plain".toMediaTypeOrNull())

        val subscribe = WebService.create().uploadMulTwo(md5, parts).subscribeOn(Schedulers.io())
            .subscribe({
                Log.d(TAG, "onCreate:onNext $it")
            }, {
                Log.d(TAG, "onCreate:throwable ${it.message}")
            })
    }

    private fun uploadMulOne() {
        val barcodePath = Environment.getExternalStorageDirectory().path + "/barcode.jpg"
        val qrcodePath = Environment.getExternalStorageDirectory().path + "/qr.jpg"
        val qinCodePath = Environment.getExternalStorageDirectory().path + "/qin.jpg"

        val barcodeFile = File(barcodePath)
        val qrcodeFile = File(qrcodePath)
        val qinCodeFile = File(qinCodePath)

        val barcodeBody = barcodeFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val qrcodeBody = qrcodeFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val qinCodeBody = qinCodeFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())

        //这里的key必须这么写，否则服务端无法识别
        val map: MutableMap<String, RequestBody> = HashMap()
        map["fileName\"; filename=\"" + barcodeFile.name] = barcodeBody
        map["fileName\"; filename=\"" + qrcodeFile.name] = qrcodeBody
        map["fileName\"; filename=\"" + qinCodeFile.name] = qinCodeBody
        val subscribe = WebService.create().uploadMulOne(map).subscribeOn(Schedulers.io())
            .subscribe({
                Log.d(TAG, "onCreate:onNext $it")
            }, {
                Log.d(TAG, "onCreate:throwable ${it.message}")
            })
    }


}