package com.example.ecomapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class RegistrationActivity : AppCompatActivity() {

    private lateinit var phoneNumber: EditText
    private lateinit var password: EditText
    private lateinit var farmerSwitch: Switch
    private lateinit var registerButton: Button
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // Инициализация Firebase Database
        databaseReference = FirebaseDatabase.getInstance("https://ecomappbd-69524-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("users")

        phoneNumber = findViewById(R.id.phone_number)
        password = findViewById(R.id.password)
        farmerSwitch = findViewById(R.id.farmer_switch)
        registerButton = findViewById(R.id.register_button)

        registerButton.setOnClickListener {
            val phone = phoneNumber.text.toString().trim()
            val pass = password.text.toString().trim()
            val isFarmer = farmerSwitch.isChecked

            if (phone.isNotEmpty() && pass.isNotEmpty()) {
                checkIfPhoneExists(phone) { exists ->
                    if (exists) {
                        Toast.makeText(this, "Аккаунт с таким номером уже существует!", Toast.LENGTH_SHORT).show()
                    } else {
                        registerNewUser(phone, pass, isFarmer)
                    }
                }
            } else {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkIfPhoneExists(phone: String, callback: (Boolean) -> Unit) {
        databaseReference.orderByChild("phone").equalTo(phone)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    callback(snapshot.exists()) // Если данные есть, вернёт true
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@RegistrationActivity, "Ошибка проверки номера: ${error.message}", Toast.LENGTH_SHORT).show()
                    callback(false)
                }
            })
    }

    private fun registerNewUser(phone: String, password: String, isFarmer: Boolean) {
        val userId = databaseReference.push().key ?: return

        val user = mapOf(
            "phone" to phone,
            "password" to password,
            "isFarmer" to isFarmer
        )

        databaseReference.child(userId).setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_SHORT).show()
                // Здесь можно переходить на следующий экран
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Ошибка регистрации: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
