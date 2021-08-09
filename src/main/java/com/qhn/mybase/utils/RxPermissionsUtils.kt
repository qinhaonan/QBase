package com.qhn.mybase.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.fragment.app.FragmentActivity
import com.qhn.mybase.utils.extend.toast
import com.tbruyelle.rxpermissions2.RxPermissions
import java.util.concurrent.TimeUnit


//定位
//照相机
//打电话
object RxPermissionsUtils {
    @SuppressLint("CheckResult", "MissingPermission")
    fun callPhone(activity: FragmentActivity, phone: String) {
        RxPermissions(activity)
                .request(Manifest.permission.CALL_PHONE)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(
                        {
                            activity.startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$phone")))
                        },
                        {
                            activity.toast("拨打电话权限被拒绝 请手动拨打 : $phone")
                        }
                )
    }

    @SuppressLint("CheckResult", "MissingPermission")
    fun getLocationPermission(activity: FragmentActivity, onSuccess: () -> Unit, onFailed: (() -> Unit)? = null, onError: (() -> Unit)? = null) {
        RxPermissions(activity).request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(
                        {
                            if (it) {
                                onSuccess()
                            } else {
                                if (onFailed == null) {
                                    activity.toast("获取定位权限失败,  请前往设置页面授权.")
                                    DialogUtils.build(activity, msg = "获取定位权限失败,  请前往设置页面授权.", onPositive =
                                    {
                                        val packageURI = Uri.parse("package:" + activity.packageName)
                                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI)
                                        activity.startActivity(intent)
                                    })

                                } else
                                    onFailed()
                            }
                        },
                        {
                            if (onError == null) {
                                activity.toast("获取定位权限失败,  请前往设置页面授权.")
                            } else
                                onError()
                        }
                )

    }
    //蓝牙权限
    @SuppressLint("CheckResult", "MissingPermission")
    fun getLocationAndBlePermission(activity: FragmentActivity, onSuccess: () -> Unit, onFailed: (() -> Unit)? = null, onError: (() -> Unit)? = null) {
        RxPermissions(activity).request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.BLUETOOTH,Manifest.permission.BLUETOOTH_ADMIN)
                .subscribe(
                        {
                            if (it) {
                                onSuccess()
                            } else {
                                if (onFailed == null) {
                                    activity.toast("获取蓝牙、定位权限失败,  请前往设置页面授权.")
                                    DialogUtils.build(activity, msg = "获取获取蓝牙、定位权限失败,  请前往设置页面授权.", onPositive =
                                    {
                                        val packageURI = Uri.parse("package:" + activity.packageName)
                                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI)
                                        activity.startActivity(intent)
                                    })
                                } else
                                    onFailed()
                            }
                        },
                        {
                            if (onError == null) {
                                activity.toast("获取定位权限失败,  请前往设置页面授权.")
                            } else
                                onError()
                        }
                )

    }

}