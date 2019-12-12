package com.example.userportal_android

import android.app.Application
import android.os.Bundle
import android.util.Log
import com.github.nkzawa.socketio.client.Socket
import com.github.nkzawa.socketio.client.IO

class UserPortalApplication : Application() {
    var socket : Socket? = null
        private set

    override fun onCreate() {
        super.onCreate()

        socket = IO.socket("https://userportal-web.azurewebsites.net")

        // Log.println(Log.DEBUG, "Application", "Socket connection created.")
    }
}