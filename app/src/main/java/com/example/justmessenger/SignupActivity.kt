package com.example.justmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.justmessenger.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

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
        // FirebaseDatabase instance
        val database = Firebase.database
        val username = binding.usernameEt.text.toString()
        val email = binding.emailEt.text.toString()
        val password = binding.passwordEt.text.toString()
        // Creating user object
        val user = User(username, email, password)
        if (username.isEmpty() && email.isEmpty() && password.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields and try again.", Toast.LENGTH_SHORT).show()
        } else if (password.length < 8) {
            Toast.makeText(this, "Your password must include at least 8 characters.", Toast.LENGTH_SHORT).show()
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Your e-mail address does not seem valid. Please change it and try again.", Toast.LENGTH_SHORT).show()
        } else  {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "You're successfully registrated!", Toast.LENGTH_SHORT).show()
                            // Adding user to FirebaseDatabase
                            val ref = database.getReference("${auth.currentUser.uid}")
                            ref.setValue(user)
                            // Opening UsersActivity
                            val usersActivityIntent = Intent(this, UsersActivity::class.java)
                            startActivity(usersActivityIntent)
                        } else {
                            Toast.makeText(this, "Registration failed. Please try one more time.", Toast.LENGTH_SHORT).show()
                        }
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