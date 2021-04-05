package com.example.justmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.justmessenger.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    lateinit private var binding: ActivityLoginBinding
    lateinit private var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.backTv.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener {
            signinUser()
        }
    }


    private fun signinUser() {
        val email = binding.emailEt.text.toString()
        val password = binding.passwordEt.text.toString()
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "You're successfully logged in!", Toast.LENGTH_SHORT).show()
                        val usersActivityIntent = Intent(this, UsersActivity::class.java)
                        startActivity(usersActivityIntent)
                    } else {
                        Toast.makeText(this, "Logging failed. Please try one more time.", Toast.LENGTH_SHORT).show()
                    }
                }
    }
}