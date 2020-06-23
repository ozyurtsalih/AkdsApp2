package com.example.akdsapp.Models

class ChatRoom {
  var chatRoomName: String? = null
    var author_id: String?= null
    var room_id: String?=null
    var chatroom_messages:List<RoomMessages>?=null
    constructor(){}
    constructor(chatRoomName: String, author_id: String, room_id:String, chatroom_messages:List<RoomMessages>){
        this.author_id=author_id
        this.chatRoomName=chatRoomName
        this.room_id=room_id
        this.chatroom_messages=chatroom_messages
    }

}