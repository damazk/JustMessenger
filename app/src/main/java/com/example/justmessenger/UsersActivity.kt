package com.example.justmessenger

import android.content.Intent
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
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder

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
            val signupActivityIntent = Intent(this, SignupActivity::class.java)
            signupActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(signupActivityIntent)
            Toast.makeText(this, "You have signed out.", Toast.LENGTH_SHORT).show()
        }

        showUsers()
    }

    private fun showUsers() {
        val adapter = GroupieAdapter()
        binding.usersRv.setAdapter(adapter)
        val database = Firebase.database
        val ref = database.getReference("/users/")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val user = it.getValue(User::class.java)
                    if (user != null && user.uid != auth.currentUser?.uid) {
                        adapter.add(UserItem(user))
                        //Toast.makeText(applicationContext, "${user?.username} is here", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }
}