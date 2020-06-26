package com.example.akdsapp.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.akdsapp.MainActivity
import com.example.akdsapp.R
import com.example.akdsapp.Register.RegisterActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    var mAuth = FirebaseAuth.getInstance()
//Auth işlemleri için gerekli değişken oluşturuldu..
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    //Buton eventleri atamları gerçekleştirildi
        val loginBtn = findViewById<View>(R.id.loginBtn) as Button
        loginBtn.setOnClickListener (View.OnClickListener { view -> login()  })
        val tvGirisYap= findViewById<View>(R.id.tvGirisYap) as TextView
        tvGirisYap.setOnClickListener {startActivity(Intent(this, RegisterActivity::class.java))}
    }
    //Girilen kullanıcı veriler firebase üzerinden kontrol ediliyor
    private fun login () {
        val emailTxt = findViewById<View>(R.id.emailTxt) as EditText
        var email = emailTxt.text.toString()
        val passwordTxt = findViewById<View>(R.id.passwordTxt) as EditText
        var password = passwordTxt.text.toString()
        //Nesneler değişkenlere atandı.
        if (!email.isEmpty() && !password.isEmpty()) {
            //Özel komutla login işlemi gerçekleştiriliyor
            this.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener ( this, OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful) {
                    //Görev başarılı ise Main activitiye giriş yapılıyor.
                    startActivity(Intent(this, MainActivity::class.java))
                    Toast.makeText(this, "Giriş Başarılı Hoşgeldiniz :)", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Üzgünüm Kullanıcı Şifre veya Kullanıcı adı hatalı :(", Toast.LENGTH_SHORT).show()
                }
            })
        }else {
            Toast.makeText(this, "Lütfen boş alan bırakmayınız :|", Toast.LENGTH_SHORT).show()
        }
    }
}