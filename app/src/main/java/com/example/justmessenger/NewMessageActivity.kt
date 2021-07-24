package com.example.justmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.justmessenger.chat.ChatActivity
import com.example.justmessenger.chat.ChatToItem
import com.example.justmessenger.databinding.ActivityNewMessageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupieAdapter

class NewMessageActivity : AppCompatActivity() {

    lateinit private var binding: ActivityNewMessageBinding
    lateinit private var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        supportActionBar?.title = "Start a new chat"

        showUsers()
    }

    companion object {
        val USER_KEY = "USER_KEY"

        val CURRENT_USER_KEY = "CURRENT_USER_KEY"
    }

    private fun showUsers() {
        val adapter = GroupieAdapter()
        binding.usersRv.setAdapter(adapter)
        val database = Firebase.database
        val ref = database.getReference("/users/")
        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val chatActivityIntent = Intent(applicationContext, ChatActivity::class.java)
                snapshot.children.forEach {
                    val user = it.getValue(User::class.java)

                    // Adding user
                    if (user != null && user.uid != auth.currentUser?.uid) {
                        adapter.add(UserItem(user))
                    }
                    // Sending current user data
                    if (user?.uid == auth.currentUser?.uid) {
                        chatActivityIntent.putExtra(CURRENT_USER_KEY, user)
                    }
                }

                adapter.setOnItemClickListener { item, view ->
                    val user = item as UserItem
                    chatActivityIntent.putExtra(USER_KEY, user.user)
                    startActivity(chatActivityIntent)
                }

            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }


}