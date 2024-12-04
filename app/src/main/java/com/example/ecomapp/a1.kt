package com.example.ecomapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.util.Log

class a1 : AppCompatActivity() {
    private lateinit var inputText: EditText
    private lateinit var sendButton: Button
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a1) // Убедись, что это правильный layout

        // Инициализация Firebase
        databaseReference = FirebaseDatabase.getInstance("https://ecomappbd-69524-default-rtdb.europe-west1.firebasedatabase.app").getReference("a1")
        inputText = findViewById(R.id.a2)
        sendButton = findViewById(R.id.a3)

        sendButton.setOnClickListener {
            val text = inputText.text.toString().trim()

            if (text.isNotEmpty()) {
                val messageId = databaseReference.push().key
                if (messageId != null) {
                    databaseReference.child(messageId).setValue(text)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Текст отправлен!", Toast.LENGTH_SHORT).show()
                            inputText.setText("") // Очистка поля
                            Log.d("Firebase", "Данные успешно записаны")
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                            Log.e("Firebase", "Ошибка записи: ${e.message}")
                        }
                }
            } else {
                Toast.makeText(this, "Введите текст!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
