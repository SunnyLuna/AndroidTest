package com.decard.androidtest.net.repository

import android.annotation.SuppressLint
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import com.decard.androidtest.net.WebService
import com.decard.androidtest.net.bean.NetErrorBean
import com.example.commonlibs.utils.MD5Utils
import io.reactivex.schedulers.Schedulers
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileOutputStream

/**
 *
 * @author ZJ
 * create at 2021/9/17 14:27
 */
object DownloadRepository {
    private val logger = LoggerFactory.getLogger("---DownloadRepository")
    val downloadFileLiveData = MutableLiveData<ResultNet<Any>>()


    @SuppressLint("CheckResult")
    fun downloadFile(url: String, filename: String, md5: String, size: String) {
        logger.debug("downloadFile: $url $filename  $md5  $size")
        WebService.create()
            .downloadMP4()
            .subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
            .subscribe({ responseBody ->
                val total = responseBody.contentLength()
                logger.debug("下载文件大小: $total")
                val destinationFile =
                    File(Environment.getExternalStorageDirectory().absolutePath + File.separator + filename)

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
                    val progress = byteSum * 1f / total * 100
                    if (System.currentTimeMillis() - startTime > 1000) {
                        startTime = System.currentTimeMillis()
                        logger.debug("downloadFile: $byteSum")
                        logger.debug("下载进度: $progress")
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
                    logger.debug("downloadFile: 文件存在")

                    val fileMd5 = MD5Utils.getFileMD5(file)
                    logger.debug("downloadFile: MD5: $fileMd5")
                    if (fileMd5 == md5) {
                        logger.debug("downloadFile: 下载成功")
                    } else {
                        downloadFileLiveData.postValue(
                            ResultNet.Error(
                                NetErrorBean(
                                    "",
                                    "文件MD5值不同",
                                    false
                                )
                            )
                        )
                    }
                } else {
                    downloadFileLiveData.postValue(
                        ResultNet.Error(
                            NetErrorBean(
                                "",
                                "下载文件不存在",
                                false
                            )
                        )
                    )
                }
            }, {
                downloadFileLiveData.postValue(
                    ResultNet.Error(
                        NetErrorBean(
                            "",
                            "${it.message}",
                            false
                        )
                    )
                )
                logger.debug("downloadFileError: ${it.message}")
            })
    }
}