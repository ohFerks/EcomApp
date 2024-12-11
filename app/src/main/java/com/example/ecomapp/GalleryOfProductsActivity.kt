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
import com.google.firebase.database.*

class GalleryOfProductsActivity : AppCompatActivity() {

    private val productList = mutableListOf<Product>() // Динамический список продуктов
    private val cartList = mutableListOf<Product>() // Список корзины
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_of_products)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        databaseReference =
            FirebaseDatabase.getInstance("https://ecomappbd-69524-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("a1")

        val cartButton: Button = findViewById(R.id.goToCartButton)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3)

        loadProductsFromDatabase()

        adapter = ProductAdapter(productList) { product, isAdded ->
            if (isAdded) {
                cartList.add(product)
            } else {
                cartList.remove(product)
            }
        }
        recyclerView.adapter = adapter

        cartButton.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            intent.putParcelableArrayListExtra("cartList", ArrayList(cartList))
            startActivity(intent)
        }

         // Загружаем данные из Firebase
    }

    private fun loadProductsFromDatabase() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                for (productSnapshot in snapshot.children) {
                    val productData = productSnapshot.value as? Map<*, *>
                    if (productData != null) {
                        try {
                            val id = productData["id"]?.toString()?.toIntOrNull() ?: 0
                            val name = productData["text1"]?.toString() ?: "Без имени"
                            val price = productData["text2"]?.toString()?.toDoubleOrNull() ?: 0.0
                            val weight = productData["weight"]?.toString()?.toDoubleOrNull() ?: 1.0
                            val quantity = 1
                            val imageResource = R.drawable.kapusta

                            val product = Product(id, name, price, weight, quantity, imageResource)
                            productList.add(product)
                        } catch (e: Exception) {
                            Toast.makeText(
                                this@GalleryOfProductsActivity,
                                "Ошибка данных: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@GalleryOfProductsActivity,
                    "Ошибка загрузки данных: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
