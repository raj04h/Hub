package com.hr.hub

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginbutton: Button = findViewById(R.id.btn_login)
        val passwordtext: EditText = findViewById(R.id.text_userpassword)

        loginbutton.setOnClickListener {
            val mainactivity_entry=Intent(this,MainActivity::class.java)
            startActivity(mainactivity_entry)

            Toast.makeText(this,"Incorrect Password",Toast.LENGTH_SHORT).show()
/*
            val enteredpassword=passwordtext.text.toString()

            if(enteredpassword=="pokemon"){
                val mainactivity_entry=Intent(this,MainActivity::class.java)
                startActivity(mainactivity_entry)
                finish()
            }
            else{
                Toast.makeText(this,"Incorrect Password",Toast.LENGTH_SHORT).show()
            }
        }
 */
        }
    }
}