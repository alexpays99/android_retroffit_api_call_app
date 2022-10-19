package com.example.retroffit_api_call_app.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.retroffit_api_call_app.R

class AnimalListFragment: Fragment() {
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val view: View = inflater.inflate(R.layout.activity_main, container, false)

        return inflater.inflate(R.layout.activity_main, container, false)
    }
}