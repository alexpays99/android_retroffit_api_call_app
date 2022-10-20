package com.example.retroffit_api_call_app.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
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
    private lateinit var task: Runnable
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
        task = Runnable {
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
                okHttpAnimalList = getOkHttpAnimals()!!
                animalList.addAll(okHttpAnimalList)

                val handler = Handler(Looper.getMainLooper())
                handler.post {
                    setupAnimalListAdapter()
                }
            } catch (e: Error) {
                Log.e("OKHTTP RESULT ERROR: ", e.toString())
            }
        }
        thread = Thread(task)
        thread.start()
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
        binding.recycleView.layoutManager = LinearLayoutManager(activity)
        binding.recycleView.adapter = adapter
        binding.recycleView.setItemViewCacheSize(animalList.size)
    }

    override fun onDestroy() {
        thread.interrupt()
        Log.e("MainActivity", "${thread.name}, ${thread.state}")
        super.onDestroy()
    }
}