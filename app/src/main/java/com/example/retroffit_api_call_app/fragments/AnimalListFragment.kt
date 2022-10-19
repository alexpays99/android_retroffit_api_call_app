package com.example.retroffit_api_call_app.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retroffit_api_call_app.MainActivity
import com.example.retroffit_api_call_app.R
import com.example.retroffit_api_call_app.adapters.AnimalListAdapter
import com.example.retroffit_api_call_app.common.Common
import com.example.retroffit_api_call_app.models.Animal
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response

class AnimalListFragment: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.activity_main, container, false)
    }
}

//object  OkHttpHandler: AsyncTask<Any?, Any?, Any?>() {
//    override fun doInBackground(vararg params: Any?): Any? {
//        try {
//            val client = OkHttpClient()
//            val request: Request = Request.Builder().url(URL).build()
//            val responce: Response = client.newCall(request).execute()
//            Log.e("REQUEST RESULT: ", responce.body?.string().toString())
//        } catch (e: Error){
//            Log.e("REQUEST ERROR: ", "ERROR ON GET REQUEST " + e.localizedMessage)
//        }
//
//        return null
//    }
//
//}