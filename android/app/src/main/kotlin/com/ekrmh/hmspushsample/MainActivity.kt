package com.ekrmh.hmspushsample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.annotation.NonNull
import com.ekrmh.hmspushsample.NativeHMSMessageService.Companion.DATA
import com.ekrmh.hmspushsample.NativeHMSMessageService.Companion.NEW_TOKEN_RECEIVED
import com.ekrmh.hmspushsample.NativeHMSMessageService.Companion.NOTIFICATION_RECEIVED
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.common.ApiException
import com.huawei.hms.push.RemoteMessage
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant


class MainActivity: FlutterActivity() {

    // Broadcast receiver
    // It trigger when our NativeHMSMessageService triggered
    private val notificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                when(it.action){
                    NEW_TOKEN_RECEIVED -> {
                        val token = it.extras?.getString(DATA)
                        // Send token to Flutter side
                        channel.invokeMethod("token", token)
                    }
                    NOTIFICATION_RECEIVED -> {
                        val message = it.extras?.getParcelable<RemoteMessage>(DATA)
                        // Send notification data to Flutter side
                        channel.invokeMethod("notification", message?.data)
                    }
                    else -> {}
                }
            }
        }
    }

    // This is our channel. Communication will be on this channel.
    private val CHANNEL = "com.ekrmh.hmspushsample/notification"
    lateinit var channel: MethodChannel

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine);

        // Register receiver
        val filter = IntentFilter()
        filter.addAction(NOTIFICATION_RECEIVED)
        filter.addAction(NEW_TOKEN_RECEIVED)
        registerReceiver(notificationReceiver, filter);


        // Create a method channel
        channel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
        // Set MethodCallHandler. When "token" method call from Flutter side then we will call getToken()
        // function and generate new token.
        channel.setMethodCallHandler { call, result ->
            when(call.method){
                "token" -> {
                    getToken()
                }
            }
        }
    }

    fun getToken(){
        Thread {
            try {
                val appId = AGConnectServicesConfig.fromContext(this).getString("client/app_id");
                val token = HmsInstanceId.getInstance(this).getToken(appId, "HCM");
                Log.i("TAG", "get token:$token");
            } catch ( e:ApiException) {
                Log.e("TAG", "get token failed, $e");
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(notificationReceiver)
    }




}

