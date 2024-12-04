package com.example.ecomapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GalleryOfProductsActivity : AppCompatActivity() {

    private val productList = listOf(
        Product(1, "Баклажаны", 1.5, "500g", R.drawable.baklajany),
        Product(2, "Капуста", 2.0, "1kg", R.drawable.kapusta),
        Product(3, "Помидоры", 1.2, "1.2kg", R.drawable.tomato),
        Product(1, "Помидоры оранжевые", 1.5, "500g", R.drawable.orangetomato),
        Product(2, "Яблоки", 2.0, "1kg", R.drawable.apple),
        Product(3, "Кукуруза", 1.2, "1.2kg", R.drawable.kukuruzka),
        Product(1, "Баклажаны", 1.5, "500g", R.drawable.baklajany),
        Product(2, "Капуста", 2.0, "1kg", R.drawable.kapusta),
        Product(3, "Помидоры", 1.2, "1.2kg", R.drawable.tomato),
        Product(1, "Баклажаны2", 1.5, "500g", R.drawable.baklajany),
        Product(2, "Капуста2", 2.0, "1kg", R.drawable.kapusta),
        Product(3, "Помидоры2", 1.2, "1.2kg", R.drawable.tomato)
    )

    private val cartList = mutableListOf<Product>() // Список корзины

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gallery_of_products)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val cartButton: Button = findViewById(R.id.goToCartButton) // Кнопка перехода в корзину


        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = ProductAdapter(productList) { product, isAdded ->
            if (isAdded) {
                cartList.add(product)
            } else {
                cartList.remove(product)
            }
        }

        cartButton.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            intent.putParcelableArrayListExtra("cartList", ArrayList(cartList)) // Передаём список продуктов
            startActivity(intent)
        }
    }
}