package com.example.retroffit_api_call_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.retroffit_api_call_app.databinding.ActivityMainBinding
import com.example.retroffit_api_call_app.fragments.AnimalFragment

class MainActivity : AppCompatActivity() {
    private lateinit var animalListFragment: AnimalFragment
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animalListFragment = AnimalFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainActivityFragment, animalListFragment)
            .commitNow()
    }
}





