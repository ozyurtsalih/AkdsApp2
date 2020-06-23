package com.example.akdsapp.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.akdsapp.Models.ChatRoom
import com.example.akdsapp.Models.Users
import com.example.akdsapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.tek_satir_chat_rooms.view.*

class ChatRoomRcyViewAdapter (tumSohbetOdalari: ArrayList<ChatRoom>): RecyclerView.Adapter<ChatRoomRcyViewAdapter.SohbetOdasiHolder>(){
    var sohbetOdalari=tumSohbetOdalari
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
        var oAnOlusturulanSohbetOdasi=sohbetOdalari.get(position)
        holder?.setData(oAnOlusturulanSohbetOdasi,position)
    }
    inner class SohbetOdasiHolder(itemView: View?): RecyclerView.ViewHolder(itemView!!){
        var tekSatirSohbetOdasi=itemView as RelativeLayout
        var sohbetOdasiOlusturan =tekSatirSohbetOdasi.tvOlusturanAdi
        var sohbetOdasiAdi=tekSatirSohbetOdasi.tvSohbetOdasiAdi



fun setData(oAnOlusturulanSohbetOdasi: ChatRoom, position: Int){
    sohbetOdasiAdi.text=oAnOlusturulanSohbetOdasi.chatRoomName
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