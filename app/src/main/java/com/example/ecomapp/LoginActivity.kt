package com.example.ecomapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private lateinit var phoneNumber: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        phoneNumber = findViewById(R.id.phone_number)
        password = findViewById(R.id.password)
        loginButton = findViewById(R.id.login_button)

        databaseReference = FirebaseDatabase.getInstance("https://ecomappbd-69524-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("users")

        loginButton.setOnClickListener {
            val phone = phoneNumber.text.toString().trim()
            val pass = password.text.toString().trim()

            if (phone.isNotEmpty() && pass.isNotEmpty()) {
                databaseReference.orderByChild("phone").equalTo(phone)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                for (userSnapshot in snapshot.children) {
                                    val dbPassword = userSnapshot.child("password").getValue(String::class.java)
                                    if (dbPassword == pass) {
                                        Toast.makeText(this@LoginActivity, "Вход успешен!", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this@LoginActivity, GalleryOfProductsActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                        return
                                    }
                                }
                                Toast.makeText(this@LoginActivity, "Неверный пароль!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@LoginActivity, "Аккаунт не найден!", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@LoginActivity, "Ошибка: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
            } else {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
