package com.example.justmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.justmessenger.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    lateinit private var binding: ActivitySignupBinding
    lateinit private var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.haveAccountTv.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.signupBtn.setOnClickListener {
            createUser()
        }

    }

    private fun createUser() {
        val email = binding.emailEt.text.toString()
        val password = binding.passwordEt.text.toString()
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Successfully created user with $email email!", Toast.LENGTH_SHORT).show()
                        val usersActivityIntent = Intent(this, UsersActivity::class.java)
                        startActivity(usersActivityIntent)
                    } else {
                        Toast.makeText(this, "Registration failed. Please try one more time.", Toast.LENGTH_SHORT).show()
                    }
                }
    }


    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        val usersActivity = Intent(this, UsersActivity::class.java)
        if(currentUser != null) {
            startActivity(usersActivity)
        }
    }
}