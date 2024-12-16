package com.example.ecomapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.typeOf
import android.content.Context
import android.content.res.Configuration
import android.widget.FrameLayout

class CartActivity : AppCompatActivity() {

    private var cartList = mutableListOf<Product>()
    private var totalPrice = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val recyclerView: RecyclerView = findViewById(R.id.cartRecyclerView)
        val payTextButton: TextView = findViewById(R.id.payTextButton)
        val goBackIcon: FrameLayout = findViewById(R.id.arrowButton)



        cartList = intent.getParcelableArrayListExtra("cartList") ?: mutableListOf()

        val adapter = CartAdapter(cartList) { product, quantity ->
            calculateTotalPrice() // Пересчитываем сумму
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Возврат обратно в корзину
        goBackIcon.setOnClickListener {
            val intent = Intent()
            intent.putParcelableArrayListExtra("updatedCartList", ArrayList(cartList))
            setResult(RESULT_OK, intent)
            finish()
        }

        payTextButton.setOnClickListener{
            if (cartList.isEmpty()){
                Toast.makeText(this, "Добавьте продукты для перехода к оплате", Toast.LENGTH_LONG).show()
            }
            else{
                val products = cartList.joinToString(separator = "\n") { product ->
                    "${product.name} ${product.weight * product.quantity} кг"
                }

                val intent = Intent(this, PaymentActivity::class.java)
                intent.putExtra("totalPrice", totalPrice)
                intent.putExtra("products", products)
                startActivity(intent)
            }
        }

        calculateTotalPrice()
    }

    private fun calculateTotalPrice() {
        totalPrice = cartList.sumOf { it.price * it.quantity }

        // Format the total price to two decimal places
        val formattedPrice = String.format("%.2f", totalPrice)
        findViewById<TextView>(R.id.totalPrice).text = "$formattedPrice ₽"
    }
    override fun attachBaseContext(newBase: Context) {
        val config = Configuration(newBase.resources.configuration)
        config.fontScale = 1.0f // Блокируем изменение масштаба шрифтов
        val newContext = newBase.createConfigurationContext(config)
        super.attachBaseContext(newContext)
    }
}
