package com.example.justmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.example.justmessenger.chat.Message
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.justmessenger.chat.ChatActivity
import com.example.justmessenger.databinding.ActivityLatestMessagesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupieAdapter

class LatestMessagesActivity : AppCompatActivity() {

    lateinit var binding: ActivityLatestMessagesBinding
    lateinit private var auth: FirebaseAuth
    val adapter = GroupieAdapter()
    val currentUserUid = FirebaseAuth.getInstance().currentUser.uid


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLatestMessagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null) {
            val signupActivityIntent = Intent(this, SignupActivity::class.java)
            startActivity(signupActivityIntent)
        }

        binding.latestMessagesRv.adapter = adapter
        binding.latestMessagesRv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        adapter.setOnItemClickListener { item, view ->
            Log.d("LatestMessages", "test test")
            val chatActivityFromLatestIntent = Intent(this, ChatActivity::class.java)
            val latestMessageItem = item as LatestMessageItem
            chatActivityFromLatestIntent.putExtra(NewMessageActivity.USER_KEY, latestMessageItem.user)
            chatActivityFromLatestIntent.putExtra(NewMessageActivity.CURRENT_USER_KEY, latestMessageItem.currentUser)
            startActivity(chatActivityFromLatestIntent)
        }

        showLatestMessages()
    }


    val latestMessagesHashMap = HashMap<String, Message>()

    private fun showLatestMessages() {
        val ref = FirebaseDatabase.getInstance().getReference("latestMessages/$currentUserUid/")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val latestMessage = snapshot.getValue(Message::class.java) ?: return
                latestMessagesHashMap[snapshot.key!!] = latestMessage
                refreshRecyclerView()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val latestMessage = snapshot.getValue(Message::class.java) ?: return
                latestMessagesHashMap[snapshot.key!!] = latestMessage
                refreshRecyclerView()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })


    }

    private fun refreshRecyclerView() {
        adapter.clear()
        latestMessagesHashMap.values.forEach {
            adapter.add(LatestMessageItem(it))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuNewMessage_btn -> {
                val newMessageIntent = Intent(this, NewMessageActivity::class.java)
                startActivity(newMessageIntent)
            }
            R.id.menuSignout_btn -> {
                auth.signOut()
                val signupActivityIntent = Intent(this, SignupActivity::class.java)
                signupActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(signupActivityIntent)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nav, menu)
        return super.onCreateOptionsMenu(menu)
    }
}