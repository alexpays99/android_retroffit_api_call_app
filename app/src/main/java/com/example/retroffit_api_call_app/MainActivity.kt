package com.example.retroffit_api_call_app

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retroffit_api_call_app.Services.ApiService
import com.example.retroffit_api_call_app.adapters.AnimalListAdapter
import com.example.retroffit_api_call_app.common.Common
import com.example.retroffit_api_call_app.databinding.ActivityMainBinding
import com.example.retroffit_api_call_app.fragments.AnimalListFragment
import com.example.retroffit_api_call_app.fragments.URL
import com.example.retroffit_api_call_app.models.Animal
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : AppCompatActivity() {
    private var animalList = mutableListOf<Animal>()
    private var retrofitAnimalList = mutableListOf<Animal>()
    private var okHttpAnimalList = mutableListOf<Animal>()
    private lateinit var adapter: AnimalListAdapter
    private lateinit var animalListFragment: AnimalListFragment
    private lateinit var binding: ActivityMainBinding
    private var apiService: ApiService? = null
    private var isRunning: Boolean = false
    private lateinit var thread: Thread
    private lateinit var task: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animalListFragment = AnimalListFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.recListConstraintLayout, animalListFragment)
    }

    private fun setupRunnableApiCallTask() {
//        task = Runnable {
//            // Run Retrofit call
//            val service = Common.retrofitService
//
//            service.getAnimals("5").enqueue(object : retrofit2.Callback<MutableList<Animal>> {
//                override fun onResponse(
//                    call: Call<MutableList<Animal>>,
//                    response: Response<MutableList<Animal>>
//                ) {
//                    if (!response.isSuccessful) {
//                        Log.e("Thread: ", "${Thread.currentThread().name}, ${response.code()}")
//                        return
//                    }
//                    try {
//                        animalList = response.body()!!
//                        setupAnimalListAdapter()
//                    } catch (e: Error) {
//                        Log.e("RETROFIT RESULT ERROR: ", e.toString())
//                    }
//                    Log.e(
//                        "Thread: ",
//                        "${Thread.currentThread().name}, " +
//                                response.body().toString()
//                    )
//                }
//
//                override fun onFailure(call: Call<MutableList<Animal>>, t: Throwable) {
//                    Log.e("Thread: ", "${Thread.currentThread().name}, " + t.toString())
//                }
//            })
//
//            // Run OkkHttp call
//            val okHttpService = OkHttpService().execute()
//
//            try {
//                OkHttpService().execute()
////                okHttpService.fetch()
////                okHttpAnimalList = okHttpService.getAnimalList()
////                animalList = (okHttpAnimalList + retrofitAnimalList) as MutableList<Animal>
////                setupAnimalListAdapter()
//            } catch (e: Error) {
//                Log.e("OKHTTP RESULT ERROR: ", e.toString())
//            }
//        }
//        thread = Thread(task)
//        thread.start()

        Thread {
            val retrofitService = Common.retrofitService

            val response = retrofitService.getAnimals("5").execute()
            try {
                retrofitAnimalList = response.body()!!
                animalList.addAll(retrofitAnimalList)
            } catch (e: Error) {
                Log.e("RETROFIT RESULT ERROR: ", e.toString())
            }

            //OkHttp call
            try {
                okHttpAnimalList = getOkHttpResponse()!!
                animalList.addAll(okHttpAnimalList)
                runOnUiThread {
                    setupAnimalListAdapter()
                }
            } catch (e: Error) {
                Log.e("OKHTTP RESULT ERROR: ", e.toString())
            }
        }.start()
    }

    private fun getOkHttpResponse(): MutableList<Animal>? {
        val res: MutableList<Animal>
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url(URL)
            .build()

        val okHttp =  client.newCall(request).execute()
        if (okHttp.isSuccessful){
            val string = okHttp.body?.string()
            val userType = object : TypeToken<MutableList<Animal>>() {}.type
            res = (Gson().fromJson(string, userType))
            return res
        }
        return null
    }


    private fun setupAnimalListAdapter() {
        adapter = AnimalListAdapter(animalList)
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = adapter
        binding.recycleView.setItemViewCacheSize(animalList.size)
    }

    override fun onStart() {
        println("onStart method has been called")
        super.onStart()
        val intent = Intent(this@MainActivity, ApiService::class.java)
        if (isRunning == false) {
//            ContextCompat.startForegroundService(this, intent)
            startService(intent)
            println("SERVICE HAS STARTED")
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            println("SERVICE HAS BINDED")
            setupRunnableApiCallTask()

        } else {
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            println("SERVICE HAS BINDED")
        }
    }

    override fun onDestroy() {
        thread.interrupt()
        Log.e("MainActivity", "${thread.name}, ${thread.state}")
        super.onDestroy()
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            apiService = (service as ApiService.CustomBinder).getService()
            if (apiService != null) {
                setupAnimalListAdapter()
                isRunning = true
                println("serviceConnection has been called")
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            apiService = null
            isRunning = false
            println("audioService = null")
        }
    }
}





