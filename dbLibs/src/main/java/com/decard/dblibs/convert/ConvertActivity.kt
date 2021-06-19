package com.decard.dblibs.convert

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.decard.dblibs.AppDataBase
import com.decard.dblibs.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.schedulers.Schedulers
import java.util.*

class ConvertActivity : AppCompatActivity() {
    private val TAG = "---ConvertActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_convert)
        Log.d(TAG, "onCreate: ")
        val jsonStr = "[\n" +
                "    {\n" +
                "        \"className\": \"三班\",\n" +
                "        \"classNumber\": \"3\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"className\": \"六班\",\n" +
                "        \"classNumber\": \"6\"\n" +
                "    }\n" +
                "]"
        val classB =
            Gson().fromJson<ArrayList<ClassBean>>(
                jsonStr,
                object : TypeToken<ArrayList<ClassBean>>() {}.type
            )


        val jsonStr1 = "[\n" +
                "    {\n" +
                "        \"className\": \"精英班\",\n" +
                "        \"classNumber\": \"1\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"className\": \"火箭班\",\n" +
                "        \"classNumber\": \"2\"\n" +
                "    }\n" +
                "]"
        val class1 =
            Gson().fromJson<ArrayList<ClassBean>>(
                jsonStr1,
                object : TypeToken<ArrayList<ClassBean>>() {}.type
            )

        val schoolBean = SchoolBean("中心小学", "天安门广场")
        val schoolBean1 = SchoolBean("中心中学", "岱宗")
        val manager = Manager(classB, Date(), schoolBean)
        val manager2 = Manager(class1, Date(), schoolBean1)
        AppDataBase.getInstance(this).getManagerDao().addManager(manager)
            .subscribeOn(Schedulers.io()).subscribe()
        AppDataBase.getInstance(this).getManagerDao().getManagers().subscribeOn(Schedulers.io())
            .subscribe({
                Log.d(TAG, "首次: ${it.size}")
                for (manager in it) {

                    Log.d(TAG, "首次: $manager")
                }
            }, {
                Log.d(TAG, "首次: ${it.message}")
            })
        AppDataBase.getInstance(this).getManagerDao().addManager(manager2)
            .subscribeOn(Schedulers.io()).subscribe()
        AppDataBase.getInstance(this).getManagerDao().getManagers().subscribeOn(Schedulers.io())
            .subscribe({
                Log.d(TAG, "二次: ${it.size}")
                for (manager in it) {
                    Log.d(TAG, "二次: $manager")
                }
            }, {
                Log.d(TAG, "二次: ${it.message}")
            })
    }
}