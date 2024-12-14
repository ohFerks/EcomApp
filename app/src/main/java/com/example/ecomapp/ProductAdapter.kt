package com.example.ecomapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.LinearLayout


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
        val cartCounterLayout: LinearLayout = itemView.findViewById(R.id.cartCounterLayout)
        val cartCounter: TextView = itemView.findViewById(R.id.cartCounter)
        val plusButton: Button = itemView.findViewById(R.id.plusButton)
        val minusButton: Button = itemView.findViewById(R.id.minusButton)

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

        if (product.isInCart == "false"){
            holder.addToCartButton.visibility = View.VISIBLE
            holder.cartCounterLayout.visibility = View.GONE
        }
        else{
            holder.cartCounterLayout.visibility = View.VISIBLE // Показываем счётчик
            holder.addToCartButton.visibility = View.GONE // Прячем кнопку
        }

        holder.cartCounter.text = product.quantity.toString()

        // Логика для кнопки "Добавить в корзину"
        holder.addToCartButton.setOnClickListener {
            product.quantity = 1 // Устанавливаем начальное количество
            holder.cartCounterLayout.visibility = View.VISIBLE // Показываем счётчик
            holder.addToCartButton.visibility = View.GONE // Прячем кнопку
            holder.cartCounter.text = product.quantity.toString()
            if (product.isInCart == "false"){
                notifyItemChanged(position)
                onCartButtonClicked(product, true)
                product.isInCart = "true"
            }

        }

        // Логика кнопки "+"
        holder.plusButton.setOnClickListener {
            product.quantity++
            holder.cartCounter.text = product.quantity.toString()
            //
            onCartButtonClicked(product, true)
        }

        // Логика кнопки "-"
        holder.minusButton.setOnClickListener {
            if (product.quantity > 1) {
                product.quantity--
                holder.cartCounter.text = product.quantity.toString()
                //
                onCartButtonClicked(product, true)
            } else {
                // Если количество становится 0, убираем счётчик
                product.quantity = 0
                holder.cartCounterLayout.visibility = View.GONE
                holder.addToCartButton.visibility = View.VISIBLE
                onCartButtonClicked(product, false)
                product.isInCart = "false"
            }
        }
    }

    override fun getItemCount() = productList.size
}

