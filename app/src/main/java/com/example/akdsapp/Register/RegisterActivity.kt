package com.example.akdsapp.Register

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.akdsapp.MainActivity
import com.example.akdsapp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {
    companion object {
        val TAG = "RegisterActivity"
    }
    var mAuth = FirebaseAuth.getInstance()
    lateinit var mDatabase : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val regBtn= findViewById<View>(R.id.regBtn)
        mDatabase=FirebaseDatabase.getInstance().getReference("Users")
        regBtn.setOnClickListener ( View.OnClickListener { view -> registerUser()  })
        selectphoto_button_register.setOnClickListener {
            Log.d(TAG, "Try to show photo selector")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }
    private fun registerUser () {
        val emailTxt = findViewById<View>(R.id.emailTxt) as EditText
        val passwordTxt = findViewById<View>(R.id.passwordTxt) as EditText
        val nameTxt = findViewById<View>(R.id.nameTxt) as EditText

        var email = emailTxt.text.toString()
        var password = passwordTxt.text.toString()
        var name = nameTxt.text.toString()

        if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty()) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    val uid = user!!.uid
                    mDatabase.child(uid).child("Name").setValue(name)
                    mDatabase.child(uid).child("uid").setValue(uid)
                    //uploadImageToFirebaseStorage()
                    if (selectedPhotoUri != null) {
                        val filename = UUID.randomUUID().toString()
                        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

                        ref.putFile(selectedPhotoUri!!)
                            .addOnSuccessListener {
                                Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")

                                ref.downloadUrl.addOnSuccessListener {
                                    Log.d(TAG, "File Location: $it")
                                    mDatabase.child(uid).child("imgUrl").setValue(it.toString())
                                    // saveUserToFirebaseDatabase()
                                }
                            }
                            .addOnFailureListener {
                                Log.d(TAG, "Failed to upload image to storage: ${it.message}")
                            }
                    }else{
                        mDatabase.child(uid).child("imgUrl").setValue("https://firebasestorage.googleapis.com/v0/b/akdsappv2.appspot.com/o/images%2F2a240787-569d-4efb-878b-01b483a11405?alt=media&token=687ef594-7ebe-43a4-bda9-d359673a9b1f")
                    }


                    startActivity(Intent(this, MainActivity::class.java))
                    Toast.makeText(this, "Successfully registered :)", Toast.LENGTH_LONG).show()
                }else {
                    Toast.makeText(this, "Error registering, try again later :(", Toast.LENGTH_LONG).show()
                }
            })
        }else {
            Toast.makeText(this,"Please fill up the Credentials :|", Toast.LENGTH_LONG).show()
        }
    }

    var selectedPhotoUri: Uri? = null


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            // proceed and check what the selected image was....
            Log.d(TAG, "Photo was selected")

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            selectphoto_imageview_register.setImageBitmap(bitmap)

            selectphoto_button_register.alpha = 0f


        }
    }




}