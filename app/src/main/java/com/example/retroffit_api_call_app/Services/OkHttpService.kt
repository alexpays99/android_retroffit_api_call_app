package com.example.retroffit_api_call_app.Services

import android.os.AsyncTask
import android.provider.ContactsContract.CommonDataKinds.Website.URL
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
//private val URL = "https://zoo-animal-api.herokuapp.com/animals/rand/7"

object  OkHttpService: AsyncTask<Any?, Any?, Any?>() {
    override fun doInBackground(vararg params: Any?): Any? {
        try {
            val client = OkHttpClient()
            val request: Request = Request.Builder().url(URL).build()
            val responce: Response = client.newCall(request).execute()
            Log.e("REQUEST RESULT: ", responce.body?.string().toString())
        } catch (e: Error){
            Log.e("REQUEST ERROR: ", "ERROR ON GET REQUEST " + e.localizedMessage)
        }

        return null
    }

}