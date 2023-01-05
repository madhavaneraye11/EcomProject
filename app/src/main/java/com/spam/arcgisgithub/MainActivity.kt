package com.spam.arcgisgithub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //some thing
        val a=3
        val b=2
        var c=a+b
        Log.d("Cal","Cal")

    }
}