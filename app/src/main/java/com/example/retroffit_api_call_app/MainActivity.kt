package com.example.retroffit_api_call_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retroffit_api_call_app.Services.RetrofitService
import com.example.retroffit_api_call_app.adapters.AnimalListAdapter
import com.example.retroffit_api_call_app.common.Common
import com.example.retroffit_api_call_app.databinding.ActivityMainBinding
import com.example.retroffit_api_call_app.fragments.AnimalListFragment
import com.example.retroffit_api_call_app.interfaces.RetrofitApiCallInterface
import com.example.retroffit_api_call_app.models.Animal
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var animalList: MutableList<Animal>
    private lateinit var adapter: AnimalListAdapter
    private lateinit var animalListFragment: AnimalListFragment
    private lateinit var binding: ActivityMainBinding
    private lateinit var thread: Thread
    private lateinit var task: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animalListFragment = AnimalListFragment()
        supportFragmentManager.beginTransaction().add(R.id.recListConstraintLayout, animalListFragment)

        task = Runnable {
            // Run Retrofit call
            val service = Common.retrofitService

            service.getAnimals("5").enqueue(object : retrofit2.Callback<MutableList<Animal>> {
                override fun onResponse(
                    call: Call<MutableList<Animal>>,
                    response: Response<MutableList<Animal>>
                ) {
                    if(!response.isSuccessful) {
                        Log.e("Thread: ","${Thread.currentThread().name}, ${response.code()}")
                        return
                    }
                    animalList = response.body()!!
                    setupAnimalListAdapter()
                    Log.e("Thread: ",
                        "${Thread.currentThread().name}, " +
                                "${response.body().toString()}"
                    )
                }

                override fun onFailure(call: Call<MutableList<Animal>>, t: Throwable) {
                    Log.e("Thread: ", "${Thread.currentThread().name}, " + t.toString())
                }
            })

            // Run OkkHttp call

        }
        thread = Thread(task)
        thread.start()
    }

    private fun setupAnimalListAdapter() {
        adapter = AnimalListAdapter(animalList)
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = adapter
        binding.recycleView.setItemViewCacheSize(animalList.size)
    }

    override fun onDestroy() {
        thread.interrupt()
        Log.e("MainActivity","${Thread.currentThread().name}, ${Thread.currentThread().state}")
        super.onDestroy()
    }
}