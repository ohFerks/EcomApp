package com.example.ecomapp

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Locale

class PaymentActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var phoneInput: EditText
    private lateinit var addressInput: EditText
    private lateinit var deliveryDate: TextView
    private lateinit var nextButton: Button
    private lateinit var backToCartButton: Button
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        // Инициализация элементов интерфейса
        nameInput = findViewById(R.id.nameInput)
        phoneInput = findViewById(R.id.phoneInput)
        addressInput = findViewById(R.id.addressInput)
        deliveryDate = findViewById(R.id.deliveryDate)
        nextButton = findViewById(R.id.nextButton)
        backToCartButton = findViewById(R.id.backToCartButton)

        // Установка ближайшей даты доставки
        deliveryDate.text = "Дата доставки: ${getNearestSunday()}"

        databaseReference = FirebaseDatabase.getInstance("https://ecomappbd-69524-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("orders")

        // Кнопка "Оформить заказ"
        nextButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val phone = phoneInput.text.toString().trim()
            val address = addressInput.text.toString().trim()

            val products = intent.getStringExtra("products") ?: "пусто"
            val totalPrice = intent.getDoubleExtra("totalPrice", 1.0)

            val orderId = databaseReference.push().key ?: return@setOnClickListener

            val order1 = products.split("\n")

            val order = mutableMapOf(
                "name" to name,
                "phone" to phone,
                "address" to address,
                "totalPrice" to "${totalPrice} руб",
                "order" to order1
            )

            // Проверяем, заполнены ли все поля
            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                // Передача данных в CheckoutActivity

                databaseReference.child(orderId).setValue(order)
                    .addOnSuccessListener {
                        //Toast.makeText(this, "Оплата завершена. Заказ создан", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, CheckoutActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Ошибка регистрации: ${e.message}", Toast.LENGTH_SHORT).show()
                    }

                val intent = Intent(this, CheckoutActivity::class.java)
                startActivity(intent)
            }
        }

        // Кнопка "Вернуться в корзину"
        backToCartButton.setOnClickListener {
            finish()
        }
    }

    private fun getNearestSunday(): String {
        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_WEEK)

        if (today in Calendar.MONDAY..Calendar.WEDNESDAY) {
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        } else {
            calendar.add(Calendar.WEEK_OF_YEAR, 1)
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        }

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(calendar.time)
    }
}

