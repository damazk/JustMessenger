package com.example.justmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth

class LatestMessagesActivity : AppCompatActivity() {

    lateinit private var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null) {
            val signupActivityIntent = Intent(this, SignupActivity::class.java)
            startActivity(signupActivityIntent)
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