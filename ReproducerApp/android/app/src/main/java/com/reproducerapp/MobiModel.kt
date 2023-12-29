package com.reproducerapp

import android.util.Log
import android.widget.Toast
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.thingclips.smart.android.user.api.ILoginCallback
import com.thingclips.smart.android.user.api.ILogoutCallback
import com.thingclips.smart.android.user.api.IRegisterCallback
import com.thingclips.smart.android.user.bean.User
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.sdk.api.IResultCallback

class MobiModel(val context: ReactApplicationContext): ReactContextBaseJavaModule(context) {

    private val TAG = MobiModel::class.java.simpleName

    override fun getName(): String {
        Log.e("Called", TAG)
        return TAG
    }

    // String countryCode, String email, String passwd, final ILoginCallback callback
    @ReactMethod
    fun loginUser(countryCode: String, email: String, password: String, promise: Promise) {
        Log.d("CalledNow", TAG)
        val packageName: String = context.applicationContext.packageName
        Log.e("packageName", packageName)
        Toast.makeText(context, packageName, Toast.LENGTH_LONG).show()
        ThingHomeSdk.getUserInstance().loginWithEmail(countryCode, email, password, object : ILoginCallback {
            override fun onSuccess(p0: User?) {
                promise.resolve(true)
                Log.d(TAG, "Login success")
            }

            override fun onError(p0: String?, p1: String?) {
                // Code, Error
                promise.reject("LOGIN_ERROR", "Login failed Code, Error: ($p0), ($p1)", null)
                Log.d(TAG, "Login failed")
            }
        })
    }

    @ReactMethod
    fun registerByEmail(countryCode: String, email: String, password: String, code: String, promise: Promise) {
        Log.d("CalledNow", TAG)
        // countryCode: String, email: String, password: String, code:String
        ThingHomeSdk.getUserInstance().registerAccountWithEmail(countryCode, email, password, code, object : IRegisterCallback {
            override fun onSuccess(p0: User?) {
                promise.resolve(true)
                Log.d(TAG, "Login success")
            }

            override fun onError(p0: String?, p1: String?) {
                // Code, Error
                promise.reject("LOGIN_ERROR", "Login failed Code, Error: ($p0), ($p1)", null)
                Log.d(TAG, "Login failed")
            }
        })
    }

    @ReactMethod
    fun sendVerificationCodeBeforeRegistration(email: String, countryCode: String, promise: Promise) {
        // username region countryCode type
        ThingHomeSdk.getUserInstance().sendVerifyCodeWithUserName(email, "", countryCode, 1, object : IResultCallback {
            override fun onError(p0: String?, p1: String?) {
                // Code, Error
                promise.reject("LOGIN_ERROR", "Login failed Code, Error: ($p0), ($p1)", null)
                Log.d(TAG, "Login failed")
            }

            override fun onSuccess() {
                promise.resolve(true)
                Log.d(TAG, "Login success")
            }
        })
    }

    @ReactMethod
    fun logout(promise: Promise) {
        // username region countryCode type
        ThingHomeSdk.getUserInstance().logout(object : ILogoutCallback {
            override fun onError(p0: String?, p1: String?) {
                // Code, Error
                promise.reject("LOGIN_ERROR", "Login failed Code, Error: ($p0), ($p1)", null)
                Log.d(TAG, "Logout failed")
            }

            override fun onSuccess() {
                promise.resolve(true)
                Log.d(TAG, "Logout success")
            }
        })
    }

    @ReactMethod
    fun getUser(promise: Promise) {
        // username region countryCode type
        val user = ThingHomeSdk.getUserInstance().user
        val resolvedMutableMap = mutableMapOf<String, String>()
        resolvedMutableMap["email"] = user?.email ?: ""
        resolvedMutableMap["username"] = user?.username ?: ""
        promise.resolve(resolvedMutableMap)
    }

}