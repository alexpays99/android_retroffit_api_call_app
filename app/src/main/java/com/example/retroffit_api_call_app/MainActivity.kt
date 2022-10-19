package com.example.retroffit_api_call_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retroffit_api_call_app.adapters.AnimalListAdapter
import com.example.retroffit_api_call_app.databinding.ActivityMainBinding
import com.example.retroffit_api_call_app.fragments.AnimalListFragment
import com.example.retroffit_api_call_app.models.Animal

class MainActivity : AppCompatActivity() {
    private lateinit var animalList: ArrayList<Animal>
    private lateinit var adapter: AnimalListAdapter
    private lateinit var animalListFragment: AnimalListFragment
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAnimals()
        setupAnimalListAdapter()

        animalListFragment = AnimalListFragment()
        supportFragmentManager.beginTransaction().add(R.id.recListConstraintLayout, animalListFragment)
    }

    private fun setupAnimals() {
        //call data from Retrofit service
    }

    private fun setupAnimalListAdapter() {
        adapter = AnimalListAdapter(animalList)
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = adapter
        binding.recycleView.setItemViewCacheSize(animalList.size)
    }
}