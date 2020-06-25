package com.example.akdsapp.Message

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.akdsapp.Login.LoginActivity
import com.example.akdsapp.Models.ChatRoom
import com.example.akdsapp.Models.RoomMessages
import com.example.akdsapp.Models.Users
import com.example.akdsapp.R
import com.example.akdsapp.utils.SohbetMesajRcyViewAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_message_room.*
import kotlinx.android.synthetic.main.activity_tests.*
import kotlinx.android.synthetic.main.tek_satir_chat_rooms.*
import kotlinx.android.synthetic.main.tek_satir_mesaj.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class MessageRoomActivity : AppCompatActivity() {
    //firebase
    var mAuthListener: FirebaseAuth.AuthStateListener?=null
    var mMesajReference:DatabaseReference?=null
    var secilenSohbetOdasiId:String=""
    var tumMesajlar:ArrayList<RoomMessages>?=null
    var myAdaptor:SohbetMesajRcyViewAdapter?=null
    var mesajIdSet: HashSet<String>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_room)
        //kullanıcının giriş çıkış işlemlerini dinler
        baslatFirebaseAuthListener()
        sohbetOdasiniOgren()
        init()

    }
    private fun init(){

        editTextTextPersonName.setOnClickListener {
            rwMesajlar.smoothScrollToPosition(myAdaptor!!.itemCount-1)

        }
        button.setOnClickListener {
            Log.d("ds","bastı butona")
            if(!editTextTextPersonName.text.toString().equals("")){
                var mesaj = editTextTextPersonName.text.toString()
                var kaydedilicekVeri=RoomMessages()
                kaydedilicekVeri.mesaj=mesaj
                kaydedilicekVeri.kullanici_id=FirebaseAuth.getInstance().currentUser?.uid
                kaydedilicekVeri.timestamp=getMesajTarihi()
                var referans=FirebaseDatabase.getInstance().reference
                    .child("ChatRoom")
                    .child(secilenSohbetOdasiId)
                    .child("chatroom_messages")
                var yeniMesajID=referans.push().key
                referans.child(yeniMesajID!!)
                    .setValue(kaydedilicekVeri)
                editTextTextPersonName.setText("")

            }
        }
    }

    private fun getMesajTarihi(): String? {
var sdf=SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("tr"))
        return  sdf.format(Date())
    }

    private fun sohbetOdasiniOgren(){
        secilenSohbetOdasiId= intent.getStringExtra("sohbet_odasi_id").toString()
        baslatMesajListener()

    }
    var mValueEventListener:ValueEventListener= object : ValueEventListener {
    override fun onCancelled(p0: DatabaseError) {

    }

    override fun onDataChange(p0: DataSnapshot) {
          sohbetOdasindakiMesajlariGetir()
    }

}

    private fun sohbetOdasindakiMesajlariGetir() {
if(tumMesajlar==null){
    tumMesajlar=ArrayList<RoomMessages>()
    mesajIdSet=HashSet<String>()
}
        mMesajReference=FirebaseDatabase.getInstance().getReference()
        var sorgu=mMesajReference?.child("ChatRoom")?.child(secilenSohbetOdasiId)?.child("chatroom_messages")!!
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    for(tekmesaj in p0!!.children){
                        var geciciMesaj=RoomMessages()
                        var kullaniciID=tekmesaj.getValue(RoomMessages::class.java)!!.kullanici_id
                        if(!mesajIdSet!!.contains(tekmesaj.key)){
                            mesajIdSet!!.add(tekmesaj.key!!)

                            if (kullaniciID!=null){
                                geciciMesaj.mesaj=tekmesaj.getValue(RoomMessages::class.java)!!.mesaj
                                geciciMesaj.kullanici_id=tekmesaj.getValue(RoomMessages::class.java)!!.kullanici_id
                                geciciMesaj.timestamp=tekmesaj.getValue(RoomMessages::class.java)!!.timestamp
                                var kullaniciDetaylari=mMesajReference?.child("Users")?.orderByKey()?.equalTo(kullaniciID)
                                kullaniciDetaylari?.addListenerForSingleValueEvent(object :
                                    ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {

                                    }

                                    override fun onDataChange(p0: DataSnapshot) {
                                        var bulunanKullanici=p0?.children?.iterator()?.next()
                                       // geciciMesaj.profil_resim=bulunanKullanici?.getValue(Users::class.java)?.imgUrl
                                        geciciMesaj.adi=bulunanKullanici?.getValue(Users::class.java)?.Name
                                    }

                                })


                                tumMesajlar?.add(geciciMesaj)
                                myAdaptor?.notifyDataSetChanged()
                                rwMesajlar.scrollToPosition(myAdaptor!!.itemCount-1)

                            }else
                            {
                                geciciMesaj.mesaj=tekmesaj.getValue(RoomMessages::class.java)!!.mesaj

                                geciciMesaj.timestamp=tekmesaj.getValue(RoomMessages::class.java)!!.timestamp

                                geciciMesaj.adi=tekmesaj.getValue(RoomMessages::class.java)!!.adi
                                tumMesajlar?.add(geciciMesaj)
                            }
                        }

                    }


                }

            })
        if(myAdaptor==null){
            initMesajlarListesi()
        }





    }

    private fun initMesajlarListesi() {
        myAdaptor= SohbetMesajRcyViewAdapter(this,tumMesajlar!!)
        rwMesajlar.adapter=myAdaptor

        rwMesajlar.layoutManager= LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        rwMesajlar.scrollToPosition(myAdaptor?.itemCount!!-1)//en altta başlasın
    }

    private fun baslatMesajListener() {
mMesajReference=FirebaseDatabase.getInstance().getReference().child("ChatRoom")
    .child(secilenSohbetOdasiId)
    .child("chatroom_messages")
        mMesajReference?.addValueEventListener(mValueEventListener)
    }

    private fun baslatFirebaseAuthListener(){
        mAuthListener=object: FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
              var kullanici=p0.currentUser
                if(kullanici==null){
                    var intent=Intent(this@MessageRoomActivity,LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener (mAuthListener!!)
    }

    override fun onStop() {
        super.onStop()
        if(mAuthListener != null ){
            FirebaseAuth.getInstance().removeAuthStateListener (mAuthListener!!)
        }
    }

    override fun onResume() {
        super.onResume()
        kullaniciKontrolEt()
    }
    private fun kullaniciKontrolEt(){
        var kullanici=FirebaseAuth.getInstance().currentUser
        if(kullanici==null){
            var intent=Intent(this@MessageRoomActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}