package com.example.akdsapp.Message

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.akdsapp.Models.ChatRoom
import com.example.akdsapp.Models.RoomMessages
import com.example.akdsapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_new_room2.*
import java.text.SimpleDateFormat
import java.time.DateTimeException
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER



class NewRoomFragment : DialogFragment() {

lateinit var chatRoomName: EditText
    lateinit var chatRoomAdd: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater!!.inflate(R.layout.fragment_new_room2, container, false )
        chatRoomName= view.findViewById(R.id.etRoom)
        chatRoomAdd=view.findViewById(R.id.btnNewRoom)

        chatRoomAdd.setOnClickListener {
            if(!chatRoomName.text.isNullOrEmpty()){
                var ref = FirebaseDatabase.getInstance().reference
                var roomId=ref.child("ChatRoom").push().key
                var newRoom = ChatRoom()
                newRoom.author_id=FirebaseAuth.getInstance().currentUser?.uid
                newRoom.chatRoomName=chatRoomName.text.toString()
                newRoom.room_id=roomId
                ref.child("ChatRoom").child(roomId.toString()).setValue(newRoom)
var MesajId=ref.child("ChatRoom").push().key
                var karsilamaMesaji=RoomMessages()
                karsilamaMesaji.mesaj="sohbet odasına hoşgeldiniz"
                karsilamaMesaji.timestamp=getMesajTarih()
                ref.child("ChatRoom")
                    .child(roomId.toString())
                    .child("chatroom_messages")
                    .child(MesajId.toString())
                    .setValue(karsilamaMesaji)
                Toast.makeText(activity,"sohbet odası başarıyla oluşturuldu",Toast.LENGTH_SHORT).show()
                (activity as MessageActivity).init()
                dialog?.dismiss()
            }else
            {
                Toast.makeText(activity,"sohbet odası adını yazınız",Toast.LENGTH_SHORT).show()
            }
        }

        return view

    }

private fun getMesajTarih() :String{
    var sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("tr"))
    return sdf.format(Date())
}
}