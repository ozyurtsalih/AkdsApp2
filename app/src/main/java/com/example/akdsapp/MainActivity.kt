package com.example.akdsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.akdsapp.Login.LoginActivity
import com.example.akdsapp.Models.Tahlils
import com.example.akdsapp.Models.Users
import com.example.akdsapp.Register.RegisterActivity
import com.example.akdsapp.utils.BottomnavigationViewHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tahlils_row.view.*

class MainActivity : AppCompatActivity() {
    private val ACTİVİTY_NO=0
    private val TAG = "HomeActivity"
   var  mAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavigationView()
        verifyUserIsLoggedIn()
//Sistemden çıkış butonu..
        imgTabDirectMessage.setOnClickListener {FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)}

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
    private fun fetchUsers() {

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()
                p0.children.forEach {
                    Log.d("NewMessage", it.toString())
                    val user = it.getValue(Users::class.java)
                    if (user != null) {
                        adapter.add(UserItem(user))
                    }
                }
                recyclerview_tahlils.adapter = adapter
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}
class UserItem(val user: Users): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.username_textview_new_message.text = user.Name
        Picasso.get().load(user.imgUrl).into(viewHolder.itemView.imageview_new_message)
    }
    override fun getLayout(): Int {
        return R.layout.tahlils_row
    }
}
