package com.example.retroffit_api_call_app.fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retroffit_api_call_app.adapters.AnimalListAdapter
import com.example.retroffit_api_call_app.common.Common
import com.example.retroffit_api_call_app.databinding.FragmentAnimalBinding
import com.example.retroffit_api_call_app.models.Animal
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request

class AnimalFragment : Fragment() {
    private var animalList = mutableListOf<Animal>()
    private var retrofitAnimalList = mutableListOf<Animal>()
    private var okHttpAnimalList = mutableListOf<Animal>()
    private lateinit var binding: FragmentAnimalBinding
    private lateinit var adapter: AnimalListAdapter

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

    @OptIn(DelicateCoroutinesApi::class)
    private fun setupRunnableApiCallTask() {
        GlobalScope.launch {
            task1()
        }
        GlobalScope.launch {
            task2()
        }
        GlobalScope.launch {
            task3()
        }
    }

    private fun getOkHttpAnimals(): MutableList<Animal>? {
        val res: MutableList<Animal>
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url(URL)
            .build()

        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val userType = object : TypeToken<MutableList<Animal>>() {}.type
            res = (Gson().fromJson(response.body?.string(), userType))
            return res
        }
        return null
    }

    private fun setupAnimalListAdapter() {
        adapter = AnimalListAdapter(animalList)
        adapter = AnimalListAdapter(animalList)
        binding.recycleView.layoutManager = LinearLayoutManager(activity)
        binding.recycleView.adapter = adapter
        binding.recycleView.setItemViewCacheSize(animalList.size)
    }

    // subtask1
    private suspend fun task1() {
        lifecycleScope.launch(Dispatchers.IO) {
            //Retrofit call
            val retrofitService = Common.retrofitService

            val response = retrofitService.getAnimals("5").execute()
            try {
                retrofitAnimalList = response.body()!!
                animalList.addAll(retrofitAnimalList)
                Log.e("Thread", Thread.currentThread().toString())
            } catch (e: Error) {
                Log.e("RETROFIT RESULT ERROR: ", e.toString())
            }

            //OkHttp call
            try {
                okHttpAnimalList = getOkHttpAnimals()!!
                animalList.addAll(okHttpAnimalList)
                Log.e("Thread", Thread.currentThread().toString())
                withContext(Dispatchers.Main) {
                    setupAnimalListAdapter()
                }
            } catch (e: Error) {
                Log.e("OKHTTP RESULT ERROR: ", e.toString())
            }
        }
    }

    //subtask2
    private suspend fun task2() {
        val job = Job()
        val scope = CoroutineScope(Dispatchers.IO + job)

        val loadData = scope.launch {

            //Retrofit call
            val retrofitCall = async {
                val retrofitService = Common.retrofitService

                val response = retrofitService.getAnimals("5").execute()
                retrofitAnimalList = response.body()!!
                animalList.addAll(retrofitAnimalList)
                withContext(Dispatchers.Main) {
                    setupAnimalListAdapter()
                }
                Log.e("Thread", Thread.currentThread().toString())
            }

            try {
                retrofitCall.await()
            } catch (e: Error) {
                Log.e("RETROFIT RESULT ERROR: ", e.toString())
            }

            //OkHttp call
            val okkHttpCal = async {
                okHttpAnimalList = getOkHttpAnimals()!!
                animalList.addAll(okHttpAnimalList)
                adapter.list.addAll(animalList)
                Log.e("Thread", Thread.currentThread().toString())
                withContext(Dispatchers.Main) {
                    setupAnimalListAdapter()
                }
            }

            try {
                okkHttpCal.await()
            } catch (e: Error) {
                Log.e("OKHTTP RESULT ERROR: ", e.toString())
            }
        }

        for (i in 0..3) {
            loadData.start()
            delay(500)
            Log.e("loadData isActive: ", loadData.isActive.toString())
        }
        delay(900)
        loadData.cancel("Coroutine canceled")
        Log.e("loadData isActive: ", loadData.isActive.toString())
    }

    //subtask3
    private suspend fun task3() {
        val supervisorJob = SupervisorJob()
    }
}