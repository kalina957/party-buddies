package com.example.partybuddies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //val currentDate = Calendar.getInstance().time
    }

    fun login(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun test(view: View){
        val intent = Intent(this, test::class.java)
        startActivity(intent)
    }
}
