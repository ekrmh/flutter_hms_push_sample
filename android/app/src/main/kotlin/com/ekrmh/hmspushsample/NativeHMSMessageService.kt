package com.ekrmh.hmspushsample

import android.content.Intent
import android.util.Log
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage

class NativeHMSMessageService : HmsMessageService() {

    companion object {
        // Broadcast Receiver Constants
        val DATA = "DATA"
        val NOTIFICATION_RECEIVED = "NOTIFICATION_RECEIVED"
        val NEW_TOKEN_RECEIVED = "NEW_TOKEN_RECEIVED"
    }

    // When notification receive then this function trigger.
    override fun onMessageReceived(message: RemoteMessage?) {
        // Send notification data to UI using broadcast receiver
        val intent = Intent(NOTIFICATION_RECEIVED)
        intent.putExtra(DATA, message)
        applicationContext.sendBroadcast(intent);

    }

    // When new token receive then this function trigger.
    override fun onNewToken(token: String?) {
        // Send notification token to UI using broadcast receiver
        val intent = Intent(NEW_TOKEN_RECEIVED)
        intent.putExtra(DATA, token)
        applicationContext.sendBroadcast(intent);
    }
}

