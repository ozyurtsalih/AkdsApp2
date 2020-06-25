package com.example.akdsapp.utils

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.akdsapp.Message.MessageActivity
import com.example.akdsapp.Message.MessageRoomActivity
import com.example.akdsapp.Models.ChatRoom
import com.example.akdsapp.Models.Users
import com.example.akdsapp.R
import com.example.akdsapp.Register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.tek_satir_chat_rooms.view.*

class ChatRoomRcyViewAdapter (mActivity:AppCompatActivity,tumSohbetOdalari: ArrayList<ChatRoom>): RecyclerView.Adapter<ChatRoomRcyViewAdapter.SohbetOdasiHolder>(){
    var sohbetOdalari=tumSohbetOdalari
    var myActivity= mActivity
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatRoomRcyViewAdapter.SohbetOdasiHolder {
       var inflater=LayoutInflater.from(parent?.context)
        var tekSatirSohbetOdalari= inflater.inflate(R.layout.tek_satir_chat_rooms,parent,false)
        return SohbetOdasiHolder(tekSatirSohbetOdalari)
    }

    override fun getItemCount(): Int {
        return sohbetOdalari.size
    }

    override fun onBindViewHolder(holder: ChatRoomRcyViewAdapter.SohbetOdasiHolder, position: Int) {
        var oAnOlusturulanSohbetOdasi= sohbetOdalari[position]
        holder.setData(oAnOlusturulanSohbetOdasi,position)
    }
    inner class SohbetOdasiHolder(itemView: View?): RecyclerView.ViewHolder(itemView!!){
        var tekSatirSohbetOdasi=itemView as RelativeLayout
        var sohbetOdasiOlusturan =tekSatirSohbetOdasi.tvOlusturanAdi
        var sohbetOdasiAdi=tekSatirSohbetOdasi.tvSohbetOdasiAdi
        var imgrowdel=tekSatirSohbetOdasi.img_row_delete



fun setData(oAnOlusturulanSohbetOdasi: ChatRoom, position: Int){
    tekSatirSohbetOdasi.setOnClickListener {
        var intent=Intent(itemView.context,MessageRoomActivity::class.java)
        intent.putExtra("sohbet_odasi_id",oAnOlusturulanSohbetOdasi.room_id)
        myActivity.startActivity(intent)
    }


    sohbetOdasiOlusturan.text=oAnOlusturulanSohbetOdasi.chatRoomName
    imgrowdel.setOnClickListener {
        Log.d(RegisterActivity.TAG, "sil tıkladım")
if(oAnOlusturulanSohbetOdasi.author_id.equals(FirebaseAuth.getInstance().currentUser!!.uid)){
    val alert = AlertDialog.Builder(itemView.context)

    // Başlık
    alert.setTitle("Çıkış")
    //Mesaj
    alert.setMessage("Çıkış yapmak istediğinize emin misiniz?")
    //Herhangi bir boşluğa basınca kapanmaması için true olursa kapanır
    //Geri tuşununu da pasif hale getiriyoruz
    alert.setCancelable(false);
    //AlertDialog'un iconunu belirliyoruz


    alert.setPositiveButton("Evet") { dialogInterface: DialogInterface, i: Int ->
        // Evet butonuna tıklayınca olacaklar
        Toast.makeText(itemView.context,"Evet",Toast.LENGTH_LONG).show()
        ( myActivity as MessageActivity).sohbetOdasiSil(oAnOlusturulanSohbetOdasi.room_id.toString())
    }

    alert.setNegativeButton("Hayır") {dialogInterface: DialogInterface, i: Int ->
        // Hayır butonuna tıklayınca olacaklar
        //Toast.makeText(itemView.context,"Hayır",Toast.LENGTH_LONG).show()
    }
    alert.show()
}
else
{
    Toast.makeText(itemView.context,"Bu sohbet odası size ait değil",Toast.LENGTH_LONG).show()
}

    }
    var ref=FirebaseDatabase.getInstance().reference
    var sorgu=ref.child("kullanici").orderByKey().equalTo(oAnOlusturulanSohbetOdasi.author_id).addListenerForSingleValueEvent(
        object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(p0: DataSnapshot) {
                for (kullanici in p0!!.children){
                    sohbetOdasiOlusturan.text=kullanici.getValue(Users::class.java)?.Name.toString()
                }
            }
        })
}
    }
}