package com.example.ecomapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
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
    private val filteredList = mutableListOf<Product>() // Отфильтрованный список
    private val cartList = mutableListOf<Product>() // Список корзины
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var searchBox: EditText

    private val CART_REQUEST_CODE = 100 // Код запроса для CartActivity

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

        searchBox = findViewById(R.id.searchBox)
        loadProductsFromDatabase()

        adapter = ProductAdapter(filteredList) { product, isAdded ->
            if (isAdded) {
                //cartList.add(product)

                val cartItem = cartList.find { it.name == product.name }
                if (cartItem != null) {
                    cartItem.quantity = product.quantity // Синхронизируем количество
                } else {
                    cartList.add(product) // Добавляем новый продукт
                }
            } else {
                cartList.removeIf { it.name == product.name }
                //cartList.remove(product)
            }
        }

        recyclerView.adapter = adapter

        cartButton.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            intent.putParcelableArrayListExtra("cartList", ArrayList(cartList))
            startActivityForResult(intent, CART_REQUEST_CODE) // Запускаем с ожиданием результата
        }

        setupSearchBox()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CART_REQUEST_CODE && resultCode == RESULT_OK) {
            val updatedCartList = data?.getParcelableArrayListExtra<Product>("updatedCartList")
            updatedCartList?.let { updatedList ->
                cartList.clear()
                cartList.addAll(updatedList)

                // Обновляем состояние продуктов в галерее
//                productList.forEach { product ->
//                    product.isInCart = if (updatedCartList.any { it.name == product.name }) "true" else "false"
//                }

                productList.forEach { product ->
                    val cartItem = updatedList.find { it.name == product.name }
                    if (cartItem != null) {
                        product.quantity = cartItem.quantity // Синхронизируем количество
                        product.isInCart = "true"
                    } else {
                        product.isInCart =
                            "false" // Убираем флаг корзины для отсутствующих продуктов
                        product.quantity = 0 // Сбрасываем количество для удалённых из корзины
                    }
                }

                adapter.notifyDataSetChanged() // Обновляем адаптер галереи
            }
        }
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
                            //val isInCart = "false"
                            // Загрузка имени ресурса картинки из базы
                            val imageName = productData["imageResource"]?.toString() ?: "default_image"
                            val imageResource = getImageResourceFromName(imageName)

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

                filteredList.clear()
                filteredList.addAll(productList)
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

    private fun getImageResourceFromName(imageName: String): Int {
        return resources.getIdentifier(imageName, "drawable", packageName).takeIf { it != 0 }
            ?: R.drawable.apple // Используем дефолтную картинку, если ресурс не найден
    }

    private fun setupSearchBox() {
        searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().lowercase()
                filterProducts(query)
            }
        })
    }

    private fun filterProducts(query: String) {
        filteredList.clear()
        if (query.isEmpty()) {
            filteredList.addAll(productList) // Если строка поиска пустая, показываем всё
        } else {
            filteredList.addAll(productList.filter { product ->
                product.name.lowercase().contains(query)
            })
        }
        adapter.notifyDataSetChanged()
    }
}
