package com.example.justmessenger

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.example.justmessenger.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class SignupActivity : AppCompatActivity() {

    val REQUEST_IMAGE_CAPTURE = 1

    lateinit private var binding: ActivitySignupBinding
    lateinit private var auth: FirebaseAuth
    lateinit private var fbStorage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // FirebaseStorage
        fbStorage = Firebase.storage

        auth = FirebaseAuth.getInstance()

        binding.cameraIbtn.setOnClickListener {
            dispatchTakePictureIntent()
        }

        binding.haveAccountTv.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.signupBtn.setOnClickListener {
            createUser()
        }

    }


    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Error: you can't take or choose a photo.", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.cameraIbtn.setImageBitmap(imageBitmap)
        }
    }


    private fun createUser() {

        // User data
        val username = binding.usernameEt.text.toString()
        val email = binding.emailEt.text.toString()
        val password = binding.passwordEt.text.toString()

        // FirebaseDatabase instance
        val database = Firebase.database

        // Checking fields
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
                            Toast.makeText(this, "You're successfully registred!", Toast.LENGTH_SHORT).show()
                            // Adding user with profile image to FirebaseDatabase
                            val ref = database.getReference("users/${auth.currentUser.uid}")
                            val fbStorageRef = fbStorage.reference
                            val userProfileImageRef = fbStorageRef.child("images/${auth.currentUser.uid}/profile_image.jpg")
                            binding.cameraIbtn.isDrawingCacheEnabled = true
                            binding.cameraIbtn.buildDrawingCache()
                            val bitmap = (binding.cameraIbtn.drawable as BitmapDrawable).bitmap
                            val baos = ByteArrayOutputStream()
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                            val data = baos.toByteArray()
                            var uploadTask = userProfileImageRef.putBytes(data)
                            uploadTask.addOnSuccessListener {
                                Toast.makeText(this, "Your image Successfully uploaded to FirebaseStorage!", Toast.LENGTH_SHORT).show()
                                userProfileImageRef.downloadUrl.addOnSuccessListener {
                                    val user = User(username, auth.currentUser.uid, email,
                                            password, it.toString())
                                    ref.setValue(user)
                                }
                            }.addOnFailureListener {
                                Toast.makeText(this, "Uploading your image failed :[", Toast.LENGTH_SHORT).show()
                            }
                            // Opening UsersActivity
                            val usersActivityIntent = Intent(this, UsersActivity::class.java)
                            usersActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            //finishAffinity()
                            startActivity(usersActivityIntent)
                            finish()
                        } else {
                            Toast.makeText(this, "Registration failed. Please try one more time.", Toast.LENGTH_SHORT).show()
                        }
                    }
        }

    }

}