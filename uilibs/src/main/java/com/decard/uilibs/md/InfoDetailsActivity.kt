package com.decard.uilibs.md

import RetrofitUtil
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.decard.uilibs.R
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_info_details.*

class InfoDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_details)
        setSupportActionBar(info_toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        tv_info.setMovementMethod(LinkMovementMethod.getInstance())
        val intent = intent
        val id = intent.getStringExtra("id")
        Log.d("-------------", id!!)
        RetrofitUtil.getTest().getInfo(id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<InfoBean> {
                override fun onComplete() {
                    Log.d("-------------", "onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    Log.d("-------------", "onSubscribe")
                }

                override fun onNext(infoBean: InfoBean) {
                    Log.d("-------------", infoBean.toString())


                    Glide.with(this@InfoDetailsActivity).load(infoBean.image).placeholder(R.mipmap.yanlingji).into(img_activity_info)
                    title_activity_info.text = infoBean.title
                    Observable.create<Spanned> {
                        var fromHtml = Html.fromHtml(infoBean.body, UrlImageGetter(this@InfoDetailsActivity), null)
                        it.onNext(fromHtml)
                    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                        tv_info.text = it
                    }
                }

                override fun onError(e: Throwable) {
                    Log.d("-------------", "onError${e.toString()}")
                }

            })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
