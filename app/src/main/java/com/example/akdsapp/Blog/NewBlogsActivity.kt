package com.example.akdsapp.Blog

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



import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_new_blogs.*
import java.text.SimpleDateFormat

import java.util.*

class NewBlogsActivity : AppCompatActivity() {
    companion object {
        val TAG = "NewBlogsActivity"
    }
    var mAuth = FirebaseAuth.getInstance()
    lateinit var mDatabase : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_blogs)
        val btn3= findViewById<View>(R.id.button3)
        mDatabase= FirebaseDatabase.getInstance().getReference("Blogs")
        //Blogs düğümü referens alındı.
        btn3.setOnClickListener ( View.OnClickListener { view -> registerBlog()  })
       selectphoto_button_register.setOnClickListener {
            Log.d(TAG, "Try to show photo selector")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }
    private fun registerBlog () {
 // Şablondaki gerekli alanlar bulunuyor.
        val descTxt = findViewById<View>(R.id.etdesc) as EditText
        val titleTxt = findViewById<View>(R.id.ettitle) as EditText
//bulunan nesnelerin içeriği değişkenlere eşitleniyor.
        var userid= FirebaseAuth.getInstance().currentUser?.email
        var desc = descTxt.text.toString()
        var title = titleTxt.text.toString()
        var tarih=getMesajTarih().toString()
        // Blog düğümüne yeni anahtar oluşturuluyor..
        var blogkey=mDatabase.push().key
        // Oluşturulan düğüme tanımlamalar yapılıyor.
        mDatabase.child(blogkey!!).child("desc").setValue(desc)
        mDatabase.child(blogkey!!).child("title").setValue(title)
        mDatabase.child(blogkey!!).child("name").setValue(userid)
        mDatabase.child(blogkey!!).child("tarih").setValue(tarih)
        //seçilen resim iterator ile firebaseye upload ediliyor ve ordan tekrar alınıp resim alanında gösteriliyor ilgili linki ile.
        if (selectedPhotoUri != null) {
            val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

            ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")
//Yüklendikten sonra yolunu istiyoruz.
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d(TAG, "File Location: $it")
                        mDatabase.child(blogkey!!).child("imgUrl").setValue(it.toString())
                        startActivity(Intent(this, BlogActivity::class.java))
                        // saveUserToFirebaseDatabase()
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, "Failed to upload image to storage: ${it.message}")
                }
        }else{
            //kullanıcı seçmediyse boş bir resim tanımlanıyor.
            mDatabase.child(blogkey!!).child("imgUrl").setValue("https://firebasestorage.googleapis.com/v0/b/akdsappv2.appspot.com/o/images%2F2a240787-569d-4efb-878b-01b483a11405?alt=media&token=687ef594-7ebe-43a4-bda9-d359673a9b1f")
        }

        Toast.makeText(this, "Successfully registered :)", Toast.LENGTH_LONG).show()


    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            // Kontrolleri gerçekleştiriyoruz.
            Log.d(TAG, "Photo was selected")
//seçilen fotoğraf tanımlanıyor.
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
//seçilen fotoğraf gerekli alana parse ediliyor
            selectphoto_imageview_register.setImageBitmap(bitmap)
//resim seç yazısı görünürlüğü düşürülüyor.
            selectphoto_button_register.alpha = 0f


        }
    }
    private fun getMesajTarih() :String{
        var sdf = SimpleDateFormat("yyyy-MM-dd", Locale("tr"))
        return sdf.format(Date())
    }
}