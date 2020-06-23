package com.example.akdsapp.Message

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.akdsapp.Models.ChatRoom
import com.example.akdsapp.Models.RoomMessages
import com.example.akdsapp.R
import com.example.akdsapp.utils.ChatRoomRcyViewAdapter
import com.example.akdsapp.utils.BottomnavigationViewHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.bottomNavigationView
import kotlinx.android.synthetic.main.activity_message.*
import kotlinx.android.synthetic.main.activity_tests.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.log

class MessageActivity : AppCompatActivity() {
   lateinit  var tumsohbetOdalari:ArrayList<ChatRoom>
    private val ACTİVİTY_NO=4
    private val TAG = "HomeActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        init()
        setupNavigationView()
        fabAddRoom.setOnClickListener {
            var dialog= NewRoomFragment()
            dialog.show(supportFragmentManager,"goster")

        }
    }
    fun setupNavigationView(){
        BottomnavigationViewHelper.setupBottomNavigationView(bottomNavigationView)
        BottomnavigationViewHelper.setupNavigation(this,bottomNavigationView)
        var menu=bottomNavigationView.menu
        var menuItem=menu.getItem(ACTİVİTY_NO)
        menuItem.setChecked(true)
    }
    fun init(){
       tumSohbetOdalariniGetir()
    }
    private fun tumSohbetOdalariniGetir(){
        tumsohbetOdalari=ArrayList<ChatRoom>()
        var ref = FirebaseDatabase.getInstance().reference
        var sorgu=ref.child("ChatRoom").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            @SuppressLint("WrongConstant")
            override fun onDataChange(p0: DataSnapshot) {
                for(teksohbetOdasi in p0!!.children){
                    var oAnkiSohbetOdasi=ChatRoom()
                    var nesneMap=(teksohbetOdasi.getValue()as HashMap<String,Object>)
                    oAnkiSohbetOdasi.room_id=nesneMap.get("room_id").toString()
                    oAnkiSohbetOdasi.chatRoomName=nesneMap.get("chatRoomName").toString()
                    oAnkiSohbetOdasi.author_id=nesneMap.get("author_id").toString()

                    var tumMesajlar=ArrayList<RoomMessages>()
                    for(mesajlar in teksohbetOdasi.child("chatroom_messages").children){
var okunanMesaj=RoomMessages()
                        okunanMesaj.timestamp=mesajlar.getValue(RoomMessages::class.java)?.timestamp
                        okunanMesaj.adi=mesajlar.getValue(RoomMessages::class.java)?.adi
                        okunanMesaj.mesaj=mesajlar.getValue(RoomMessages::class.java)?.mesaj
                        okunanMesaj.kullanici_id=mesajlar.getValue(RoomMessages::class.java)?.kullanici_id
                        okunanMesaj.profil_resim=mesajlar.getValue(RoomMessages::class.java)?.profil_resim
                        tumMesajlar.add(okunanMesaj)


                    }
                   /* oAnkiSohbetOdasi.author_id=teksohbetOdasi.getValue(ChatRoom::class.java)?.author_id
                    oAnkiSohbetOdasi.chatRoomName=teksohbetOdasi.getValue(ChatRoom::class.java)?.chatRoomName
                    oAnkiSohbetOdasi.room_id=teksohbetOdasi.getValue(ChatRoom::class.java)?.room_id*/
Log.e("test",oAnkiSohbetOdasi.toString())
                    oAnkiSohbetOdasi.chatroom_messages=tumMesajlar
                    tumsohbetOdalari.add(oAnkiSohbetOdasi)
                }
                Toast.makeText(this@MessageActivity,"tüm sohbet odası sayısı"+tumsohbetOdalari.size,Toast.LENGTH_SHORT).show()
             sohbetOdalariListele()
            }

        })

    }
    private fun sohbetOdalariListele(){
        var adapter = ChatRoomRcyViewAdapter(tumsohbetOdalari)
        rvSohbetOdalari.adapter=adapter
        rvSohbetOdalari.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

    }
}