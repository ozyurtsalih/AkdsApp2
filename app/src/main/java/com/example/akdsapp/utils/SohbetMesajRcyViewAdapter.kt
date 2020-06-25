package com.example.akdsapp.utils

import android.content.Context
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.akdsapp.Models.RoomMessages
import com.example.akdsapp.R
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.tek_satir_mesaj.view.*

class SohbetMesajRcyViewAdapter(context: Context,tumMesajlar:ArrayList<RoomMessages>):RecyclerView.Adapter<SohbetMesajRcyViewAdapter.sohbetMesajViewHolder>(){
 var myContext=context
    var myTumMesajlar=tumMesajlar

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): sohbetMesajViewHolder {
        var inflater=LayoutInflater.from(myContext)
        var view:View?=null
        if(viewType==2){
            view=inflater.inflate(R.layout.tek_satir_mesaj, parent, false)
        }else
        {
            view=inflater.inflate(R.layout.tek_satir_mesaj2, parent, false)
        }

        return sohbetMesajViewHolder(view)
    }

    override fun getItemCount(): Int {
       return myTumMesajlar.size
    }

    override fun getItemViewType(position: Int): Int {
      if(myTumMesajlar.get(position).kullanici_id.equals(FirebaseAuth.getInstance().currentUser?.uid)){
          return 1
      }else
      {
          return 2
      }
    }
//xmldeki javalara veri bağlama
    override fun onBindViewHolder(holder: sohbetMesajViewHolder, position: Int) {
       var oankiMesaj=myTumMesajlar.get(position)
        holder?.setData(oankiMesaj,position)
    }
    inner class sohbetMesajViewHolder(itemView:View?):RecyclerView.ViewHolder(itemView!!){
         var tumLayout=itemView as ConstraintLayout
        var profilResmi=tumLayout.imageview_new_message
        var mesaj=tumLayout.username_textview_new_message
        var isim =tumLayout.tvAuthor
        var tarih= tumLayout.tvTarih



fun setData(oankiMesaj:RoomMessages, position: Int){
    mesaj.text=oankiMesaj.mesaj
    isim.text=oankiMesaj.adi
    tarih.text=oankiMesaj.timestamp
    if(!oankiMesaj.profil_resim.isNullOrEmpty()){
        Picasso.get().load(oankiMesaj.profil_resim).resize(48,48).into(profilResmi)
    }


}


    }
}