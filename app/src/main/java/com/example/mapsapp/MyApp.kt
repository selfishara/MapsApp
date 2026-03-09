package com.example.mapsapp

import android.app.Application
import com.example.mapsapp.data.remote.MySupabaseClient

class MyApp : Application() {

    companion object {
        lateinit var database: MySupabaseClient
    }

    override fun onCreate() {
        super.onCreate()

        //la URL y key se leen desde el archivo local.properties y se pasan a BuildConfig a través de gradle
        database = MySupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_KEY
        )
    }
}