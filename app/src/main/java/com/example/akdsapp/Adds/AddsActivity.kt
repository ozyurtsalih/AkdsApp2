package com.example.akdsapp.Adds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.akdsapp.Login.LoginActivity
import com.example.akdsapp.Models.Tahlils
import com.example.akdsapp.Models.Users
import com.example.akdsapp.R
import com.example.akdsapp.UserItem
import com.example.akdsapp.utils.BottomnavigationViewHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_adds.*
import kotlinx.android.synthetic.main.activity_adds.imgTabDirectMessage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.bottomNavigationView

import kotlinx.android.synthetic.main.tahlils_row.view.*
import kotlinx.android.synthetic.main.tek_satir_sonuc.view.*

class AddsActivity : AppCompatActivity() {
    private val ACTİVİTY_NO=2
    private val TAG = "HomeActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adds)
        setupNavigationView()
        //Kullanıcıya ait sonuçları listelemek için kullanılan fonksiyonu çağırıyoruz.
        fetchUsers()
        imgTabDirectMessage.setOnClickListener {FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)}
    }
    // Navigasyon menusu fonksiyonu
    fun setupNavigationView(){
        BottomnavigationViewHelper.setupBottomNavigationView(bottomNavigationView)
        BottomnavigationViewHelper.setupNavigation(this,bottomNavigationView)
        var menu=bottomNavigationView.menu
        var menuItem=menu.getItem(ACTİVİTY_NO)
        menuItem.setChecked(true)
    }
    private fun fetchUsers() {
       //val user = mAuth.currentUser
       // val uid = user!!.uid
        // İlgili Kullanıcının verisini çağırıyoruz.
        val userid= FirebaseAuth.getInstance().currentUser?.uid
        // sadece o anki kullanıcıya ait tahlil sonuçları id ile karşılaştırdık.
        val ref = FirebaseDatabase.getInstance().getReference("Tahlils").orderByChild("uid").equalTo(userid)
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()

                p0.children.forEach {
                    Log.d("NewMessage", it.toString())
                    val tahlil = it.getValue(Tahlils::class.java)
                    if (tahlil != null) {
                        adapter.add(UserItem(tahlil))
                    }
                }

                rcSonuc.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}
class UserItem(val tahlil: Tahlils): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tvtip.text= tahlil.type
        //viewHolder.itemView.tvOlusturanAdi.text= tahlil.Name
        viewHolder.itemView.tvRisk.text= tahlil.risksonuc
        viewHolder.itemView.tvtime.text= tahlil.tarih

    }

    override fun getLayout(): Int {
        return R.layout.tek_satir_sonuc
    }
}

