package com.example.retroffit_api_call_app.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retroffit_api_call_app.adapters.AnimalListAdapter
import com.example.retroffit_api_call_app.common.Common
import com.example.retroffit_api_call_app.databinding.FragmentAnimalBinding
import com.example.retroffit_api_call_app.models.Animal
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
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

//        task1()
//        CoroutineScope(Dispatchers.Default).launch {
//            task2()
//        }
        CoroutineScope(Dispatchers.Default).launch {
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
    private fun task1() {
        CoroutineScope(IO).launch {
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
        val scope = CoroutineScope(IO + job)

        val loadData = scope.launch {
            repeat(3) {
                //Retrofit call
                val retrofitCall = async {
                    retrofitApiCall()
                }
                try {
                    retrofitCall.await()
                } catch (e: Error) {
                    Log.e("RETROFIT RESULT ERROR: ", e.toString())
                }

                //OkHttp call
                val okkHttpCal = async {
                    okHttpApiCall()
                }
                try {
                    okkHttpCal.await()
                } catch (e: Error) {
                    Log.e("OKHTTP RESULT ERROR: ", e.toString())
                }
                Log.e("loadData isActive: ", isActive.toString())
            }
            delay(500)
        }
        loadData.join()
        delay(1700)
        loadData.cancel()
        Log.e("loadData isActive: ", loadData.isActive.toString())
    }

    private suspend fun retrofitApiCall() {
        val retrofitService = Common.retrofitService

        val response = retrofitService.getAnimals("5").execute()
        retrofitAnimalList = response.body()!!
        animalList.addAll(retrofitAnimalList)
        withContext(Dispatchers.Main) {
            setupAnimalListAdapter()
        }
        Log.e("Thread", Thread.currentThread().toString())
        Log.e("request1", "request1")
    }

    private suspend fun okHttpApiCall() {
        okHttpAnimalList = getOkHttpAnimals()!!
        animalList.addAll(okHttpAnimalList)
        adapter.list.addAll(animalList)
        Log.e("Thread", Thread.currentThread().toString())
        Log.e("request2", "request2")
        withContext(Dispatchers.Main) {
            setupAnimalListAdapter()
        }
    }

    private fun displayAlertDialog() {
        AlertDialog.Builder(requireActivity())
            .setTitle("Error!")
            .setMessage("Load data exception!")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, id -> println("finish") }
            .create()
            .show()
    }

    //subtask3
    private suspend fun task3() {
        // comment necessary line to check work with job and superJob

//        superVisionJob()
        job()
    }

    private suspend fun superVisionJob() {
        val handler = CoroutineExceptionHandler { _, exception ->
            val handler = Handler(Looper.getMainLooper())
            handler.post { displayAlertDialog() }
            println("CoroutineExceptionHandler got $exception with suppressed ${exception.suppressed.contentToString()}")
        }
        val job = SupervisorJob()
        val scope = CoroutineScope(Dispatchers.IO + job)

        scope.launch(handler) {
            val exception = launch {
                delay(1000)
                throw Exception()
            }
            val loadRetrofitData = launch {
                retrofitApiCall()
            }
            try {
                exception.start()
            } catch (e: Exception) {
                Log.e("job", "exception Exception")
            }
            try {
                loadRetrofitData.start()
            } catch (e: Exception) {
                Log.e("job", "loadRetrofitData Exception")
            }
        }.join()
    }

    private suspend fun job() {
        val handler = CoroutineExceptionHandler { _, exception ->
            val handler = Handler(Looper.getMainLooper())
            handler.post { displayAlertDialog() }
            println("CoroutineExceptionHandler got $exception with suppressed ${exception.suppressed.contentToString()}")
        }
        val job = Job()
        val scope = CoroutineScope(Dispatchers.IO + job)

        scope.launch(handler) {
            val exception = launch {
                delay(1000)
                throw Exception()
            }
            val loadRetrofitData = launch {
                retrofitApiCall()
            }
            try {
                exception.start()
            } catch (e: Exception) {
                Log.e("job", "exception Exception")
            }
            try {
                loadRetrofitData.start()
            } catch (e: Exception) {
                Log.e("job", "loadRetrofitData Exception")
            }
        }.join()
    }
}