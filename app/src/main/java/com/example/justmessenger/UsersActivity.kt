package com.example.justmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.justmessenger.databinding.ActivityUsersBinding
import com.google.firebase.auth.FirebaseAuth

class UsersActivity : AppCompatActivity() {

    lateinit private var binding: ActivityUsersBinding
    lateinit private var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        setSupportActionBar(binding.toolbar)

        binding.settingsImgbtn.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "You have signed out.", Toast.LENGTH_SHORT).show()
            onDestroy()
        }
    }
}