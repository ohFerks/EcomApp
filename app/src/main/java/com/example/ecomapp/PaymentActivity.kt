package com.example.ecomapp

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class PaymentActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var phoneInput: EditText
    private lateinit var addressInput: EditText
    private lateinit var deliveryDate: TextView
    private lateinit var nextButton: Button
    private lateinit var backToCartButton: Button

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

        // Кнопка "Далее"
        nextButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val phone = phoneInput.text.toString().trim()
            val address = addressInput.text.toString().trim()

            // Проверяем, заполнены ли все поля
            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                // Передача данных в CheckoutActivity
                val intent = Intent(this, CheckoutActivity::class.java)
                intent.putExtra("name", name)
                intent.putExtra("phone", phone)
                intent.putExtra("address", address)
                intent.putExtra("totalPrice", intent.getDoubleExtra("totalPrice", 1.0))
                //intent.putParcelableArrayListExtra("cartList", intent.getParcelableArrayListExtra("cartList"))
                //intent.putExtra("cartList", intent.getSerializableExtra("cartList"))
                startActivity(intent)
            }
        }

        // Кнопка "Назад в корзину"
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

