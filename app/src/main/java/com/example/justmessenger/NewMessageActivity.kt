package com.example.justmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.justmessenger.chat.ChatActivity
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

        binding.newMessagesRv.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))

        showUsers()
    }

    companion object {
        val USER_KEY = "USER_KEY"

        val CURRENT_USER_KEY = "CURRENT_USER_KEY"
    }

    private fun showUsers() {
        val currentUserUid = auth.currentUser.uid
        val adapter = GroupieAdapter()
        binding.newMessagesRv.setAdapter(adapter)
        val database = Firebase.database
        val usersRef = database.getReference("users")

        // Trying to check there are users with whom currentUser has dialog or not
//        val messagesRef = database.getReference("latestMessages/$currentUserUid")
//        val messagesRefValue = messagesRef.get().addOnSuccessListener {  }
//        Log.d("NewMessage", "Latest messages ref: ${messagesRefValue}")
        usersRef.addValueEventListener(object : ValueEventListener {

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