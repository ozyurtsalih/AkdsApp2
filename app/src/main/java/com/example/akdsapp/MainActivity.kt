package com.example.akdsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.akdsapp.Adds.UserItem
import com.example.akdsapp.Login.LoginActivity
import com.example.akdsapp.Models.Tahlils
import com.example.akdsapp.Models.Users
import com.example.akdsapp.Register.RegisterActivity
import com.example.akdsapp.utils.BottomnavigationViewHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_adds.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.bottomNavigationView
import kotlinx.android.synthetic.main.activity_main.imgTabDirectMessage
import kotlinx.android.synthetic.main.tahlils_row.view.*
import kotlinx.android.synthetic.main.tek_blog_row.view.*

class MainActivity : AppCompatActivity() {
    private val ACTİVİTY_NO=0
    private val TAG = "HomeActivity"
    var  mAuth = FirebaseAuth.getInstance()

    lateinit var mDatabase : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavigationView()
        verifyUserIsLoggedIn()
        tahlilCounter()
        bilgiDoldur()

//Sistemden çıkış butonu..
        imgTabDirectMessage.setOnClickListener {FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)}
        updateButton.setOnClickListener ( View.OnClickListener { view -> profilUpdate()  })


    }
    //Kullanıcı girişi kontrol diliyor..
    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
    fun setupNavigationView(){
        BottomnavigationViewHelper.setupBottomNavigationView(bottomNavigationView)
        BottomnavigationViewHelper.setupNavigation(this,bottomNavigationView)
        var menu=bottomNavigationView.menu
        var menuItem=menu.getItem(ACTİVİTY_NO)
        menuItem.setChecked(true)
    }
    private fun bilgiDoldur() {
        val user = mAuth.currentUser
        val uid = user!!.uid
        val rootRef = FirebaseDatabase.getInstance().reference
        val uidRef = rootRef.child("Users").child(uid)
        val valueEventListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userad = dataSnapshot.child("Name").value
                adTxt.text=userad.toString()
                var mail:String =  dataSnapshot.child("Mail").value.toString()
                mailTxt.text=mail
                var yas:String =  dataSnapshot.child("Yas").value.toString()
                yasTxt.setText(yas)
                var img:String =  dataSnapshot.child("imgUrl").value.toString()
                Picasso.get().load(img).into(imgProfile)
                var bio:String =  dataSnapshot.child("Bio").value.toString()
                bioTxt.setText(bio)
                var telephone:String =  dataSnapshot.child("Telephone").value.toString()
                phoneTxt.setText(telephone)
                var medya:String =  dataSnapshot.child("Medya").value.toString()
                medyaTxt.setText(medya)
                var unvan:String =  dataSnapshot.child("Unvan").value.toString()
                meslekTxt.setText(unvan)

                testTv.text=toplam.toString()

            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, databaseError.message) //Don't ignore errors!
            }
        }
        uidRef.addListenerForSingleValueEvent(valueEventListener)

       // val adTxt = findViewById<View>(R.id.adTxt) as TextView

        //adTxt.text= mDatabase.child("Users").child(uid).child("Name").getValue(String.class)
        //mailTxt.setText(mDatabase.child("Users").child(uid).child("Mail").toString())
       // yasTxt.= mDatabase!!.child("Users").child(uid).child("Yas").toString()

    }
    private fun profilUpdate(){
        val user = mAuth.currentUser
        val uid = user!!.uid
        val mDatabase = FirebaseDatabase.getInstance().getReference("Users")


                   mDatabase.child(uid).child("Yas").setValue(yasTxt.text.toString())
                   mDatabase.child(uid).child("Medya").setValue(medyaTxt.text.toString())
                   mDatabase.child(uid).child("Telephone").setValue(phoneTxt.text.toString())
                   mDatabase.child(uid).child("Unvan").setValue(meslekTxt.text.toString())
                   mDatabase.child(uid).child("Bio").setValue(bioTxt.text.toString())
        startActivity(Intent(this, MainActivity::class.java))
        Toast.makeText(this, "Bilgiler başarı ile güncellendi :)", Toast.LENGTH_LONG).show()



    }



    var toplam:Int=0
    private fun tahlilCounter() {
        val userid= FirebaseAuth.getInstance().currentUser?.uid
        // sadece o anki kullanıcıya ait tahlil sonuçları id ile karşılaştırdık.
        val ref = FirebaseDatabase.getInstance().getReference("Tahlils").orderByChild("uid").equalTo(userid)
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                p0.children.forEach {
                        toplam++
                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

}

