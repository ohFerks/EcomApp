package com.example.ecomapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CheckoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val payNowButton: Button = findViewById(R.id.payNowButton)

        // Получаем данные из Intent
        val name = intent.getStringExtra("name") ?: ""
        val phone = intent.getStringExtra("phone") ?: ""
        val address = intent.getStringExtra("address") ?: ""
        val totalPrice = intent.getDoubleExtra("totalPrice", 0.0)
        val cartList = intent.getSerializableExtra("cartList") as? List<Product> ?: emptyList()

        payNowButton.setOnClickListener {
            // Имитация успешной оплаты
            Toast.makeText(this, "Оплата завершена. Спасибо за заказ!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            //saveOrderToFirebase(name, phone, address, totalPrice, cartList)
        }
    }

//    private fun saveOrderToFirebase(name: String, phone: String, address: String, totalPrice: Double, cartList: List<Product>) {
//        // Подготовка данных для Firebase
//        val order = hashMapOf(
//            "name" to name,
//            "phone" to phone,
//            "address" to address,
//            "totalPrice" to totalPrice,
//            "cartItems" to cartList.map { it.name } // Отправляем только имена продуктов
//        )
//
//        // Отправка данных в Firestore
//        val db = FirebaseFirestore.getInstance()
//        db.collection("orders")
//            .add(order)
//            .addOnSuccessListener {
//                Toast.makeText(this, "Оплата завершена. Спасибо за заказ!", Toast.LENGTH_SHORT).show()
//
//                // Завершаем процесс заказа
//                val intent = Intent(this, MainActivity::class.java)
//                  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                  startActivity(intent)
//            }
//            .addOnFailureListener { e ->
//                Toast.makeText(this, "Ошибка сохранения: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
//    }
}
