package com.decard.uilibs.md

import RetrofitUtil
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import com.decard.uilibs.R
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_material_design.*
import java.util.*


class MaterialDesignActivity : AppCompatActivity() {

    private var adapter: MaterialAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_design)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            //设置按钮
            actionBar.setDisplayHomeAsUpEnabled(true)
            //更换按钮图标，默认是返回箭头
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher_round)
        }

        navigationView.setCheckedItem(R.id.zinv)
        navigationView.setNavigationItemSelectedListener {
            drawerLayout.closeDrawers()
            true
        }
        floatingActionButton.setOnClickListener {
            Snackbar.make(it, "删除数据", Snackbar.LENGTH_LONG).setAction("确定", View.OnClickListener {
                Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show()
            }).show()
        }
        //下拉刷新
        swipeRefreshLayout.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        swipeRefreshLayout.setOnRefreshListener {
            addData()
        }

        //上拉加载
        rv_test.addOnScrollListener(object : EndlessRecyclerOnScrollListener() {
            override fun onLoadMore() {
                adapter!!.setLoadState(adapter!!.LOADING)

                if (lists.size < 78) {
                    // 模拟获取网络数据，延时1s
                    RetrofitUtil.getTest().getDatas().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            Log.d(TAG, "addData: 刷新数据")
                            val dataBeans: ArrayList<DataBean> = arrayListOf()
                            for (story in it.stories) {
                                var title = story.title
                                var img = story.images[0]
                                dataBeans.add(DataBean(img, title, story.id.toString()))
                            }
                            for (story in it.top_stories) {
                                dataBeans.add(
                                    DataBean(
                                        story.image,
                                        story.title,
                                        story.id.toString()
                                    )
                                )
                            }
                            adapter!!.addDatas(dataBeans)
                            lists.addAll(dataBeans)

                        }
                } else {
                    // 显示加载到底的提示
                    adapter!!.setLoadState(adapter!!.LOADING_END)
                }
            }
        })
    }

    private val TAG = "---MaterialActivity"
    private fun addData() {
        RetrofitUtil.getTest().getDatas().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d(TAG, "addData: 刷新数据")
                val dataBeans: ArrayList<DataBean> = arrayListOf()
                for (story in it.stories) {
                    var title = story.title
                    var img = story.images[0]
                    dataBeans.add(DataBean(img, title, story.id.toString()))
                }
                for (story in it.top_stories) {
                    dataBeans.add(DataBean(story.image, story.title, story.id.toString()))
                }
                adapter!!.addDatas(dataBeans)
                lists.addAll(dataBeans)
                swipeRefreshLayout.isRefreshing = false
            }
    }

    private var lists: ArrayList<DataBean> = arrayListOf()

    override fun onResume() {
        super.onResume()
        RetrofitUtil.getTest().getDatas().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                for (story in it.stories) {
                    var title = story.title
                    var img = story.images[0]
                    lists.add(DataBean(img, title, story.id.toString()))
                }
                for (story in it.top_stories) {
                    lists.add(DataBean(story.image, story.title, story.id.toString()))
                }
                adapter = MaterialAdapter(this, lists)
                adapter!!.setOnItemClickListener(object : MaterialAdapter.ClickListener {
                    override fun onItemClick(position: Int) {
                        val intent =
                            Intent(this@MaterialDesignActivity, InfoDetailsActivity::class.java)
                        intent.putExtra("id", lists.get(position).id)
                        startActivity(intent)
                    }
                })
                rv_test.adapter = adapter

                val mSelectionTracker = SelectionTracker.Builder<Long>(
                    "mySelection",
                    rv_test,
                    StableIdKeyProvider(rv_test),
                    MyItemDetailsLookup(rv_test),
                    StorageStrategy.createLongStorage()
                ) //设置可选择的item，这里设置为都可选
                    .withSelectionPredicate(SelectionPredicates.createSelectAnything())
                    .build()
                adapter!!.setSelectionTracker(mSelectionTracker)
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item!!.itemId == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        } else if (item.itemId == R.id.backup) {
            Toast.makeText(this, "正在上传", Toast.LENGTH_LONG).show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //获取menu的注入器(Inflater)并将我们配置的toolbar文件加载到menu中即可
        menuInflater.inflate(R.menu.toolbar, menu)
        return true

    }
}