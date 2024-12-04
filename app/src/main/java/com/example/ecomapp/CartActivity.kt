package com.example.ecomapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartActivity : AppCompatActivity() {

    private var cartList = mutableListOf<Product>()
    private val productQuantities = mutableMapOf<Product, Int>()
    private var totalPrice = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val recyclerView: RecyclerView = findViewById(R.id.cartRecyclerView)
        val payButton: Button = findViewById(R.id.payButton)
        val goBackButton: Button = findViewById(R.id.goBackButton)

        cartList = intent.getParcelableArrayListExtra("cartList") ?: mutableListOf()

        cartList.forEach { product ->
            productQuantities[product] = 1 // Начальное количество
        }

        val adapter = CartAdapter(cartList) { product, quantity ->
            productQuantities[product] = quantity // Обновляем количество в Map
            calculateTotalPrice() // Пересчитываем сумму
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        payButton.setOnClickListener {
            if (cartList.isEmpty()){
                Toast.makeText(this, "Добавьте продукты для перехода к оплате", Toast.LENGTH_LONG).show()
            }
            else{
                val intent = Intent(this, PaymentActivity::class.java)
                startActivity(intent)
            }
        }

        goBackButton.setOnClickListener {
            finish() // Возвращаемся в GalleryActivity
        }

        calculateTotalPrice()
    }

    private fun calculateTotalPrice() {
        totalPrice = productQuantities.entries.sumOf { (product, quantity) ->
            product.price * quantity
        }
        // Format the total price to two decimal places
        val formattedPrice = String.format("%.2f", totalPrice)
        findViewById<TextView>(R.id.totalPrice).text = "Итого: $formattedPrice ₽"
    }
}
