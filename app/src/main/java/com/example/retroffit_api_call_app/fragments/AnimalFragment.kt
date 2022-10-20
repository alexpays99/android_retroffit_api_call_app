package com.example.retroffit_api_call_app.fragments

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retroffit_api_call_app.Services.ApiService
import com.example.retroffit_api_call_app.adapters.AnimalListAdapter
import com.example.retroffit_api_call_app.common.Common
import com.example.retroffit_api_call_app.databinding.FragmentAnimalBinding
import com.example.retroffit_api_call_app.models.Animal
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request

class AnimalFragment : Fragment() {
    private var animalList = mutableListOf<Animal>()
    private var retrofitAnimalList = mutableListOf<Animal>()
    private var okHttpAnimalList = mutableListOf<Animal>()
    private lateinit var binding: FragmentAnimalBinding
    private lateinit var adapter: AnimalListAdapter
    private var apiService: ApiService? = null
    private var isRunning: Boolean = false
    private lateinit var thread: Thread


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnimalBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRunnableApiCallTask()
    }

    private fun setupRunnableApiCallTask() {
        Thread {
            //Retrofit call
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

                val handler = Handler(Looper.getMainLooper())
                handler.post {
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

        val okHttp = client.newCall(request).execute()
        if (okHttp.isSuccessful) {
            val string = okHttp.body?.string()
            val userType = object : TypeToken<MutableList<Animal>>() {}.type
            res = (Gson().fromJson(string, userType))
            return res
        }
        return null
    }


    private fun setupAnimalListAdapter() {
        adapter = AnimalListAdapter(animalList)
        binding.recycleView.layoutManager = LinearLayoutManager(activity)
        binding.recycleView.adapter = adapter
        binding.recycleView.setItemViewCacheSize(animalList.size)
    }

    override fun onStart() {
        println("onStart method has been called")
        super.onStart()
        val intent = Intent(activity, ApiService::class.java)
        if (isRunning == false) {
//            ContextCompat.startForegroundService(this, intent)
            activity?.startService(intent)
            println("SERVICE HAS STARTED")
            activity?.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            println("SERVICE HAS BINDED")
            setupRunnableApiCallTask()

        } else {
            activity?.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
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