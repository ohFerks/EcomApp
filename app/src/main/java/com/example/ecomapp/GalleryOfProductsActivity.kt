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
        Product(3, "Помидоры", 1.2, "1.2kg", R.drawable.tomato)
    )

    private val cartList = mutableListOf<Product>() // Список корзины

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gallery_of_products)

        // Обработка системных отступов
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Настройка RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = ProductAdapter(productList) { product -> addToCart(product) }

        // Обработка нажатия на кнопку добавления продукта
        val addProductButton: Button = findViewById(R.id.addProductButton)
        addProductButton.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addToCart(product: Product) {
        cartList.add(product)
        Toast.makeText(this, "${product.name} added to cart", Toast.LENGTH_SHORT).show()
    }
}