package com.ekrmh.hmspushsample

import android.content.Intent
import android.util.Log
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage

class NativeHMSMessageService : HmsMessageService() {

    companion object {
        val DATA = "DATA"
        val NOTIFICATION_RECEIVED = "NOTIFICATION_RECEIVED"
        val NEW_TOKEN_RECEIVED = "NEW_TOKEN_RECEIVED"
    }
    override fun onMessageReceived(message: RemoteMessage?) {
        Log.d("TAG","new notif ${message?.data}")
        val intent = Intent(NOTIFICATION_RECEIVED)
        intent.putExtra(DATA, message)
        applicationContext.sendBroadcast(intent);

    }


    override fun onNewToken(token: String?) {
        Log.d("TAG","asd")

        val intent = Intent(NEW_TOKEN_RECEIVED)
        intent.putExtra(DATA, token)
        applicationContext.sendBroadcast(intent);
    }
}

