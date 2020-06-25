package com.example.akdsapp.Models

class Blog (val uid: String, val title: String, val desc: String, val imgUrl: String) {
    constructor() : this("", "", "","")
}