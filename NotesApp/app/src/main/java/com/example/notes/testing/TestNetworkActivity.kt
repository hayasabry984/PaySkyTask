package com.example.notes.testing

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.notes.network.NetworkUtils

class TestNetworkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isNetworkAvailable = NetworkUtils.isNetworkAvailable(this)
        Log.d("TestNetworkActivity", "Network is available: $isNetworkAvailable")
        //manifest: android:name=".ui.TestNetworkActivity"
    }
}
