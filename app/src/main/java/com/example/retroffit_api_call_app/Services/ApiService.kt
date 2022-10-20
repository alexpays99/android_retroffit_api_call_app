package com.example.retroffit_api_call_app.Services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.retroffit_api_call_app.common.Common
import com.example.retroffit_api_call_app.models.Animal
import retrofit2.Call
import retrofit2.Response

class ApiService: Service() {
    private val binder = CustomBinder()
    private lateinit var thread: Thread
    private lateinit var task: Runnable

    inner class CustomBinder : Binder() {
        fun getService(): ApiService = this@ApiService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onCreate() {
        println("onCreate() method is called")
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("onStartCommand() method is called")
        return START_NOT_STICKY
//            return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        println("onDestroy() method is called")
        stopSelf()
        super.onDestroy()
        println("SERVICE HAS BEEN DESTROYED")
    }

    fun setupTask(task: Runnable) {
        println("setupTask() method is called")
        thread = Thread(task)
        println("onDestroy() method is called")
        thread.start()
        println(thread.name + ", " + thread.state + "has started")
    }

    fun dispose() {
        println("dispose() method is called")
        thread.stop()
        println(thread.name + ", " + thread.state)
        stopSelf()
        println("service has been destroyed")
    }
}