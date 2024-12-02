package com.example.ecomapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userLogin: EditText = findViewById(R.id.user_login)
        val userEmail: EditText = findViewById(R.id.user_email)
        val userPassword: EditText = findViewById(R.id.user_password)
        val button: Button = findViewById(R.id.button_reg)
        val linkToAuth: TextView = findViewById(R.id.link_to_auth)

        button.setOnClickListener{
            val login = userLogin.text.toString().trim()
            val email = userEmail.text.toString().trim()
            val password = userPassword.text.toString().trim()



            if (login == "" || email == "" || password == "")
                Toast.makeText(this, "Ошибка. Не все поля заполнены", Toast.LENGTH_LONG).show()
            else{
                val user = User(login, email, password)

                val db = DbHelper(this, null)
                if (db.addUser(user)){
                    Toast.makeText(this, "Пользователь $login добавлен", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, AdressAndNumberActivity::class.java)
                    intent.putExtra("login", login)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, "Ошибка. Пользователь с таким логином уже существует", Toast.LENGTH_LONG).show()
                }



                userLogin.text.clear()
                userEmail.text.clear()
                userPassword.text.clear()
            }

//            val db = DbHelper(this, null)
//            db.deleteDatabase()
        }

        linkToAuth.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }
    }
}