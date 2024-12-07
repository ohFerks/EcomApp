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
    private lateinit var textField1: EditText
    private lateinit var textField2: EditText
    private lateinit var sendButton: Button
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a1)

        databaseReference = FirebaseDatabase.getInstance("https://ecomappbd-69524-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("a1")

        textField1 = findViewById(R.id.a2)
        textField2 = findViewById(R.id.a4)

        sendButton = findViewById(R.id.a3)

        sendButton.setOnClickListener {
            val text1 = textField1.text.toString().trim()
            val text2 = textField2.text.toString().trim()

            if (text1.isNotEmpty() && text2.isNotEmpty()) {
                val messageId = databaseReference.push().key
                if (messageId != null) {
                    val productData = mapOf(
                        "id" to System.currentTimeMillis().toString(),
                        "text1" to text1,
                        "text2" to text2, // Добавлено
                        "weight" to "1kg",
                        "imageResource" to "default_image"
                    )
                    databaseReference.child(messageId).setValue(productData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Данные успешно отправлены!", Toast.LENGTH_SHORT).show()
                            textField1.setText("")
                            textField2.setText("")
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(this, "Заполните оба поля!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}