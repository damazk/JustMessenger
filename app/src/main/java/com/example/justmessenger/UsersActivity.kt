package com.example.justmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.justmessenger.databinding.ActivityUsersBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

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
            finishAffinity()
        }

        showUsers()
    }

    private fun showUsers() {
        val database = Firebase.database
        val ref = database.getReference("/users/")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val user = it.getValue(User::class.java)
                    Toast.makeText(applicationContext, "${user?.username} is here", Toast.LENGTH_SHORT).show()
                    Log.d("Users", "Value is $user")
                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }
}