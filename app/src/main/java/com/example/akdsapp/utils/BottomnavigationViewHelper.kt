package com.example.akdsapp.utils
import android.content.Context
import android.content.Intent
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.akdsapp.Adds.AddsActivity
import com.example.akdsapp.Blog.BlogActivity
import com.example.akdsapp.MainActivity
import com.example.akdsapp.Message.MessageActivity
import com.example.akdsapp.R
import com.example.akdsapp.Tests.TestsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import java.security.AccessControlContext

class BottomnavigationViewHelper {
    companion object
    {
        fun setupBottomNavigationView(bottomnavigationViewEx: BottomNavigationViewEx){
            bottomnavigationViewEx.enableAnimation(false)
            bottomnavigationViewEx.enableShiftingMode(false)
            bottomnavigationViewEx.enableItemShiftingMode(false)

        }
        fun setupNavigation(context:Context, bottomnavigationViewEx: BottomNavigationViewEx)
        {
            bottomnavigationViewEx.onNavigationItemSelectedListener=object: BottomNavigationView.OnNavigationItemSelectedListener{
                override fun onNavigationItemSelected(item: MenuItem): Boolean {
                    when(item.itemId){

                        R.id.ic_home -> {

                                val intent = Intent(context, MainActivity::class.java).addFlags(
                                    Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                context.startActivity(intent)
                                return true


                        }

                        R.id.ic_search -> {

                                val intent = Intent(context, TestsActivity::class.java).addFlags(
                                    Intent.FLAG_ACTIVITY_NO_ANIMATION)

                                context.startActivity(intent)
                               return true

                        }

                        R.id.ic_share -> {

                                val intent = Intent(context, AddsActivity::class.java).addFlags(
                                    Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                context.startActivity(intent)
                                return true


                        }

                        R.id.ic_news -> {

                                val intent = Intent(context, BlogActivity::class.java).addFlags(
                                    Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                context.startActivity(intent)


                        }

                        R.id.ic_profile -> {

                                val intent = Intent(context, MessageActivity::class.java).addFlags(
                                    Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                context.startActivity(intent)
                                return true

                        }



                    }

                    return false
                }
            }
        }
    }
}