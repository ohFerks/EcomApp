package com.example.ecomapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AdressAndNumberActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_adressandnumber)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userAddress: EditText = findViewById(R.id.user_address)
        val userNumber: EditText = findViewById(R.id.user_number)
        val button: Button = findViewById(R.id.button_enter_address)

        button.setOnClickListener{
            val address = userAddress.text.toString().trim()
            val number = userNumber.text.toString().trim()

            val login = intent.getStringExtra("login")!!

            if (address == "" || number == "")
                Toast.makeText(this, "Укажите адрес и номер телефона", Toast.LENGTH_LONG).show()
            else{
                val db = DbHelper(this, null)
                val isUpdated = db.updateAddressAndNumber(login, address, number)

                if (isUpdated) {
                    Toast.makeText(this, "Адрес успешно добавлен", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, AuthActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Ошибка обновления адреса", Toast.LENGTH_LONG).show()
                }

            }

        }
    }
}