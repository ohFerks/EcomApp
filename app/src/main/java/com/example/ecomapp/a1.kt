package com.example.ecomapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class a1 : AppCompatActivity() {
    private lateinit var textField1: EditText
    private lateinit var textField2: EditText
    private lateinit var sendButton: Button
    private lateinit var selectImageButton: Button
    private lateinit var previewImageView: ImageView
    private lateinit var databaseReference: DatabaseReference
    private var selectedImageResource: Int = R.drawable.apple // Начальное значение

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a1)

        databaseReference = FirebaseDatabase.getInstance("https://ecomappbd-69524-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("a1")

        textField1 = findViewById(R.id.a2)
        textField2 = findViewById(R.id.a4)
        sendButton = findViewById(R.id.a3)
        selectImageButton = findViewById(R.id.selectImageButton)
        previewImageView = findViewById(R.id.previewImageView) // Предпросмотр выбранной картинки

        // Слушатель на выбор изображения
        selectImageButton.setOnClickListener {
            showImageSelectionDialog()
        }

        // Слушатель на отправку данных
        sendButton.setOnClickListener {
            val text1 = textField1.text.toString().trim()
            val text2 = textField2.text.toString().trim()

            if (text1.isNotEmpty() && text2.isNotEmpty()) {
                val messageId = databaseReference.push().key
                if (messageId != null) {
                    val productData = mapOf(
                        "id" to System.currentTimeMillis().toString(),
                        "text1" to text1,
                        "text2" to text2,
                        "weight" to "1kg",
                        "imageResource" to selectedImageResource.toString() // Сохраняем ресурс изображения
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

    private fun showImageSelectionDialog() {
        val imageOptions = arrayOf("Капуста", "Морковь", "Картофель")
        val imageResources = arrayOf(R.drawable.kapusta, R.drawable.tomato, R.drawable.orangetomato)

        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Выберите изображение")
        builder.setItems(imageOptions) { _, which ->
            selectedImageResource = imageResources[which]
            previewImageView.setImageResource(selectedImageResource) // Обновляем предпросмотр
        }
        builder.show()
    }
}
