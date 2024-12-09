package com.example.ecomapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class CheckoutActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val payNowButton: Button = findViewById(R.id.payNowButton)

        databaseReference = FirebaseDatabase.getInstance("https://ecomappbd-69524-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("orders")

        // Получаем данные из Intent
        val name = intent.getStringExtra("name") ?: ""
        val phone = intent.getStringExtra("phone") ?: ""
        val address = intent.getStringExtra("address") ?: ""
        val totalPrice = intent.getDoubleExtra("totalPrice", 0.0)
        val cartList = intent.getSerializableExtra("cartList") as? List<Product> ?: emptyList()
        //val cartList = intent.getParcelableArrayListExtra("cartList") ?: mutableListOf()

//        val cartListString = cartList.joinToString(separator = "\n") { product ->
//            "Название: ${product.name}, Вес: ${product.weight}, Цена: ${product.price}, "
//        }

        //val cartListString = "сосиска"

        payNowButton.visibility = View.GONE

        // Создаем Handler для задержки
        val handler = Handler(mainLooper)
        handler.postDelayed({
            // Показываем кнопку через 15 секунд
            payNowButton.visibility = View.VISIBLE
        }, 4000)

        payNowButton.setOnClickListener {
            // Имитация успешной оплаты

            val orderId = databaseReference.push().key ?: return@setOnClickListener

            val order = mutableMapOf(
                "name" to name,
                "phone" to phone,
                "address" to address,
                "totalPrice" to totalPrice,
                "cartList" to cartList
            )
//            cartListString.forEachIndexed { index, item ->
//                order["product_$index"] = item
//            }

            databaseReference.child(orderId).setValue(order)
                .addOnSuccessListener {
                    Toast.makeText(this, "Оплата завершена. Заказ создан", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Ошибка регистрации: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            //Toast.makeText(this, "Оплата завершена. Спасибо за заказ!", Toast.LENGTH_SHORT).show()
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
//        val db = FirebaseFirestore.getInstance("https://ecomappbd-69524-default-rtdb.europe-west1.firebasedatabase.app")
//        db.collection("orders")
//            .add(order)
//            .addOnSuccessListener {
//                Toast.makeText(this, "Оплата завершена. Спасибо за заказ!", Toast.LENGTH_SHORT).show()
//
//                // Завершаем процесс заказа
//                val intent = Intent(this, MainActivity::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                startActivity(intent)
//            }
//            .addOnFailureListener { e ->
//                Toast.makeText(this, "Ошибка сохранения: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
//    }
}
