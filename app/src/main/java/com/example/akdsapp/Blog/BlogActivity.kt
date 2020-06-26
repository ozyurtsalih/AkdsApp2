package com.example.akdsapp.Blog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.akdsapp.Models.Blog
import com.example.akdsapp.Models.Users
import com.example.akdsapp.R
import com.example.akdsapp.utils.BottomnavigationViewHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_blog.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.bottomNavigationView
import kotlinx.android.synthetic.main.tahlils_row.view.*
import kotlinx.android.synthetic.main.tek_blog_row.view.*

class BlogActivity : AppCompatActivity() {
    private val ACTİVİTY_NO = 3
    private val TAG = "HomeActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog)
        setupNavigationView()
        //Kayıtlı Bloglar fetch ediliyor.
        fetchUsers()
        fabAddRoom2.setOnClickListener {
            val intent = Intent(this, NewBlogsActivity::class.java)
            //blog ekleme intenti başlatılmak üzere java koduna dönüştürülüyor..
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)}
    }
//alt menü oluşturma
    fun setupNavigationView() {
        BottomnavigationViewHelper.setupBottomNavigationView(bottomNavigationView)
        BottomnavigationViewHelper.setupNavigation(this, bottomNavigationView)
        var menu = bottomNavigationView.menu
        var menuItem = menu.getItem(ACTİVİTY_NO)
        menuItem.setChecked(true)
    }
    //Blogs düğümündeki veriler çekiliyor.
    private fun fetchUsers() {
        // val user = mAuth.currentUser
        //val uid = user!!.uid
        val ref = FirebaseDatabase.getInstance().getReference("Blogs")
        // Blog düğümü referans alındı
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()
//Tek tek reference geziliyor..
                p0.children.forEach {
                    //Kayıt kontrol
                    Log.d("NewMessage", it.toString())
                    val blog = it.getValue(Blog::class.java)
                    if (blog != null) {
                        adapter.add(UserItem(blog))
                    }
                }
                //Adaptor ilgili rcviewe atanıyor.
                rcBlog.adapter = adapter
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}
//Gerekli verileri ilgili nesnelere atayan bölüm
class UserItem(val blog: Blog): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.title.text = blog.title
        viewHolder.itemView.desc.text = blog.desc
        Picasso.get().load(blog.imgUrl).into(viewHolder.itemView.img)
    }
    override fun getLayout(): Int {
        //atamaları her bir layouta yapıyor..
        return R.layout.tek_blog_row
    }
}
