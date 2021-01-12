package com.decard.pdflibs

import android.graphics.Bitmap
import android.graphics.Paint
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.itextpdf.text.Image
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import java.io.File


class MainActivity : AppCompatActivity() {

    private val TAG = "---MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RxPermissions(this).request(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ).subscribe {
            Log.d(TAG, "onCreate: $it")
        }

        val paint = Paint()
//        paint.setColor(resources.getColor(R.color.colorPrimary))
        paint.strokeWidth = 3f
        val pdfFile = File(
            Environment.getExternalStorageDirectory()
                .absolutePath + File.separator +
                    "sign.pdf"
        )
        pdfView.fromFile(pdfFile)
            .enableSwipe(true)    //是否允许翻页，默认是允许翻页
            .enableDoubletap(true)
            .defaultPage(0)    //设置默认显示第0页
//            .swipeHorizontal(true)//设置翻页方式，默认竖直
            .onLoad {
                Log.d(TAG, "onCreate: $it")
            }
            .onPageChange { page, pageCount ->
                Log.d(TAG, "onCreate: page: $page   pageCount: $pageCount")
            }
            .onDraw { canvas, pageWidth, pageHeight, displayedPage ->
                if (displayedPage == 1)
                    canvas.drawCircle(pageWidth / 2, pageWidth / 2, pageWidth / 4, paint)
            }
            .onPageScroll { page, positionOffset ->
                    Log.d(TAG, "onPageScroll:page:   $page   positionOffset: $positionOffset")
            }
            .onLocation { x, y ->
                if (!pdfView.isZooming){
                    this.x = x
                    this.y = y
                    Log.d(TAG, "onCreate: $x    $y")
                }
            }
            .enableAnnotationRendering(true)// 渲染风格（就像注释，颜色或表单）
            .load()

        Log.d(TAG, "onCreate: widthPixels: ${ScreenUtils.widthPixels(this)}")
        Log.d(TAG, "onCreate: heightPixels: ${ScreenUtils.heightPixels(this)}")
        Log.d(TAG, "onCreate: density: ${ScreenUtils.displayMetrics(this).density}")
        Log.d(TAG, "onCreate: xdpi: ${ScreenUtils.displayMetrics(this).xdpi}")
        Log.d(TAG, "onCreate: ydpi: ${ScreenUtils.displayMetrics(this).ydpi}")
    }

    var x = 0f
    var y = 0f

    override fun onResume() {
        super.onResume()
        val startTime = System.currentTimeMillis()
//        PDFUtils.getFontPosition(
//            Environment.getExternalStorageDirectory()
//                .absolutePath + File.separator + "Logan.pdf", "写日志", 4
//        )
//        Log.d(TAG, "PDFUtils: ${System.currentTimeMillis() - startTime}")
//
//        val time = System.currentTimeMillis()
//        SealXYUtil.xyxxxx()
//        Log.d(TAG, "SealXYUtil: ${System.currentTimeMillis() - time}")


        btn_click.setOnClickListener {
//            val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.zj)
            //签名
            val bitmap = sign_pad.signatureBitmap
//            iv_sign.setImageBitmap(bitmap)
            sign_pad.clear()

            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 5, baos)
            val image: Image = Image.getInstance(baos.toByteArray())
            Log.d(TAG, "onCreate: ${baos.toByteArray().size}")
            Observable.create<String> {
                PDFUtils.addPi(image, 1, x - 100, 1080 - y - 100)
                it.onNext("嗯嗯")
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {

                val pdfFile = File(
                    Environment.getExternalStorageDirectory()
                        .absolutePath + File.separator +
                            "signed.pdf"
                )
                pdfView.fromFile(pdfFile)
                    .enableSwipe(true)    //是否允许翻页，默认是允许翻页
                    .defaultPage(0)    //设置默认显示第0页
//            .swipeHorizontal(true)//设置翻页方式，默认竖直
                    .onLoad {
                        Log.d(TAG, "onCreate: $it")
                        Log.d(TAG, "onResume: ${pdfView.height}   ${pdfView.width}")
                    }
                    .onPageChange { page, pageCount ->
                        Log.d(TAG, "onCreate: page: $page   pageCount: $pageCount")
                    }
                    .onDraw { canvas, pageWidth, pageHeight, displayedPage ->
                    }
                    .onPageScroll { page, positionOffset ->
                        Log.d(TAG, "onPageScroll:page:   $page   positionOffset: $positionOffset")
                    }
                    .onLocation { clickX, clickY ->
                        x = clickX
                        y = clickY
                        Log.d(TAG, "onCreate: $x    $y")
                    }
                    .enableAnnotationRendering(true)// 渲染风格（就像注释，颜色或表单）
                    .load()
            }

//            val path = Environment.getExternalStorageDirectory()
//                .absolutePath + File.separator + "response.pdf"
//            val file = File(path)
//            if (file.exists()) {
//                Log.d(TAG, "onResume: 文件存在")
//                var fileToBase64 = PDFAndBase64ConvertUtil.pdfToBase64(file)
//                FileUtils.writeFileToSDCard("pdf", "base.txt", fileToBase64!!)
//                fileToBase64 = fileToBase64.replace("[\\s*\t\n\r]".toRegex(), "")
//                FileUtils.writeFileToSDCard("pdf", "baseDeal.txt", fileToBase64)
//            } else {
//                Log.d(TAG, "onResume: 文件不存在")
//            }
        }
    }


}