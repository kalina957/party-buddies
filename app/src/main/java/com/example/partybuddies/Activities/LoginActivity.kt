package com.example.partybuddies.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.partybuddies.R


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

}
