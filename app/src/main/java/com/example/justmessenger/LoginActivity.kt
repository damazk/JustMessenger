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
        if (email.isEmpty() && password.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields and try again.", Toast.LENGTH_SHORT).show()
        } else if (password.length < 8) {
            Toast.makeText(this, "Your password must include at least 8 characters.", Toast.LENGTH_SHORT).show()
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Your e-mail address does not seem valid. Please change it and try again.", Toast.LENGTH_SHORT).show()
        } else {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "You're successfully logged in!", Toast.LENGTH_SHORT).show()
                            val usersActivityIntent = Intent(this, NewMessageActivity::class.java)
                            usersActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(usersActivityIntent)
                            finish()
                        } else {
                            Toast.makeText(this, "Logging failed. Please try one more time.", Toast.LENGTH_SHORT).show()
                        }
                    }
        }

    }


}