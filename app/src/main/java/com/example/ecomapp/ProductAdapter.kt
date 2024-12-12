package com.example.ecomapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(
    private val productList: List<Product>, // Список продуктов для отображения
    private val onCartButtonClicked: (Product, Boolean) -> Unit // Сообщаем о добавлении/удалении из корзины
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        val productWeight: TextView = itemView.findViewById(R.id.productWeight)
        val addToCartButton: Button = itemView.findViewById(R.id.addToCartButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        // Заполняем данные продукта
        holder.productImage.setImageResource(product.imageResId)
        holder.productName.text = product.name
        holder.productPrice.text = "${product.price} ₽"
        holder.productWeight.text = "${product.weight} кг"

        // Логика кнопки "Добавить в корзину"
        var isInCart = false // Состояние кнопки для текущего продукта

        holder.addToCartButton.setOnClickListener {
            isInCart = !isInCart
            if (isInCart) {
                holder.addToCartButton.setBackgroundColor(Color.parseColor("#EDF4C1"))
                holder.addToCartButton.text = "В корзине"
                holder.addToCartButton.width = 120
                holder.addToCartButton.height = 55
                onCartButtonClicked(product, true) // Сообщаем, что продукт добавлен в корзину
            } else {
                holder.addToCartButton.setBackgroundColor(Color.parseColor("#FFFFFF"))
                holder.addToCartButton.text = "Добавить в корзину"
                onCartButtonClicked(product, false) // Сообщаем, что продукт удалён из корзины
            }
        }
    }

    override fun getItemCount() = productList.size
}

