package com.example.justmessenger.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.justmessenger.SignupActivity
import com.example.justmessenger.User
import com.example.justmessenger.UsersActivity
import com.example.justmessenger.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupieAdapter

class ChatActivity : AppCompatActivity() {

    lateinit private var binding: ActivityChatBinding
    val currentUserUid = FirebaseAuth.getInstance().currentUser.uid

    val adapter = GroupieAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getParcelableExtra<User>(UsersActivity.USER_KEY)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.title = user?.username

        binding.signoutImgbtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val signupActivityIntent = Intent(this, SignupActivity::class.java)
            signupActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(signupActivityIntent)
            Toast.makeText(this, "You have signed out.", Toast.LENGTH_SHORT).show()
        }

        showMessages()

        binding.sendBtn.setOnClickListener {
            sendMessage()
        }

    }


    private fun showMessages() {
        val user = intent.getParcelableExtra<User>(UsersActivity.USER_KEY)
        val currentUser = intent.getParcelableExtra<User>(UsersActivity.CURRENT_USER_KEY)
        binding.chatRv.setAdapter(adapter)
        val ref = FirebaseDatabase.getInstance().getReference("messages")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                Log.d("Chat", "Message text: ${message?.text}")

                if (message != null && user != null && currentUser != null) {
                    if (message.toId == currentUserUid && message.fromId == user.uid) {
                        adapter.add(ChatToItem(message, currentUser))
                    } else if (message.fromId == currentUserUid && message.toId == user.uid) {
                        adapter.add(ChatFromItem(message, user))
                    }
                }


            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun sendMessage() {
        val user = intent.getParcelableExtra<User>(UsersActivity.USER_KEY)
        val currentUser = intent.getParcelableExtra<User>(UsersActivity.CURRENT_USER_KEY)

        val ref = FirebaseDatabase.getInstance().getReference("messages").push()

        val textMessage = binding.messageEt.text.toString()
        val message = Message(ref.key!!, user!!.uid, currentUser!!.uid, textMessage, System.currentTimeMillis(), currentUser.profileImageUrl, user.profileImageUrl)

        ref.setValue(message).addOnSuccessListener {
            Log.d("Chat", "Message id: ${ref.key}")
            binding.messageEt.text.clear()
            binding.chatRv.scrollToPosition(adapter.itemCount - 1)
        }
    }

    // Provides Back button
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}