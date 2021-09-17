package com.decard.cplibs

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log


/**
 * 状态监测、远程升级
 * @author ZJ
 * created at 2020/5/19 13:25
 */
class ManagerContentProvider : ContentProvider() {


    private val TAG = "---ManagerCProvider"
    private var param = ""

    override fun call(method: String, arg: String?, extras: Bundle?): Bundle? {
        val bundle = Bundle()

        when (method) {
            "GetAppInfo" -> {
                baseInfo(extras, bundle)
            }
            "StrategyPOSP" -> {
                bundle.putBoolean("switch", true)
                bundle.putString("host", "dctms.decard.com")
                bundle.putInt("port", 40010)
//                bundle.putString("host", "47.105.35.37")
//                bundle.putInt("port", 40011)
                bundle.putString("mqttResponseHost", "")
                bundle.putInt("mqttResponsePort", 11220)
                bundle.putBoolean("success", true)
            }

            "GetParameter" -> {
                bundle.putString("parameter", param)
                bundle.putBoolean("success", true)
            }
        }
        return bundle
    }

    private fun baseInfo(extras: Bundle?, bundle: Bundle) {
        //读取APP版本
        if (extras!!.containsKey("appVersionName")) {
            val versionName = BuildConfig.VERSION_NAME
            Log.d(TAG, "baseInfo: $versionName")
            if (!TextUtils.isEmpty(versionName)) {
                bundle.putString("appVersionName", versionName)
            }
        }
        //项目编号
        if (extras.containsKey("projectCode")) {
            bundle.putString("projectCode", "fffffff")//新乡市第二人民医院驾驶员体检收费项目
        }
        //应用名称
        if (extras.containsKey("appName")) {
            val appName = getAppName(MyApp.instance)
            if (!TextUtils.isEmpty(appName)) {
                bundle.putString("appName", appName)
            }
        }
        //应用包名
        if (extras.containsKey("appPackageName")) {
            val packageName = "com.decard.physicalexamination"
            bundle.putString("appPackageName", packageName)
        }
        //应用启动页面
        if (extras.containsKey("appLauncherActivity")) {
            val launchView = "com.decard.physicalexamination.SplashActivity"
            bundle.putString("appLauncherActivity", launchView)
        }
        //设备类型
        if (extras.containsKey("deviceType")) {
            val deviceType = "f11_x1"
            bundle.putString("deviceType", deviceType)
        }
        //设备类型
        if (extras.containsKey("posModel")) {
            val psoModel = "f11_x1"
            bundle.putString("posModel", psoModel)
        }

        //设备状态
        if (extras.containsKey("deviceStatus")) {
            bundle.putString("deviceStatus", "00")
        }
        //设备序列号
        if (extras.containsKey("SN")) {
            bundle.putString("SN", "0000000000000000")
        }

        //系统名称
        if (extras.containsKey("systemType")) {
            bundle.putString("systemType", "android")
        }
        bundle.putBoolean("success", true)
    }


    private fun getAppName(context: Context?): String? {
        if (context != null) {
            val pm = context.packageManager
            if (pm != null) {
                val pi: PackageInfo?
                try {
                    pi = pm.getPackageInfo(context.packageName, 0)
                    if (pi != null) {
                        return pi.applicationInfo.loadLabel(pm).toString()
                    }
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
        return ""
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        return null
    }

    override fun onCreate(): Boolean {
        return false
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }
}