package com.example.ecomapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class CartAdapter(
    private val cartList: MutableList<Product>,
    private val onQuantityChange: (Product, Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.cartProductImage)
        val productName: TextView = itemView.findViewById(R.id.cartProductName)
        val productWeight: TextView = itemView.findViewById(R.id.cartProductWeight)
        val productPrice: TextView = itemView.findViewById(R.id.cartProductPrice)
        val productQuantity: TextView = itemView.findViewById(R.id.productQuantity)
        val minusButton: Button = itemView.findViewById(R.id.minusButton)
        val plusButton: Button = itemView.findViewById(R.id.plusButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart_product, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = cartList[position]

        holder.productImage.setImageResource(product.imageResId)
        holder.productName.text = product.name
        holder.productWeight.text = "Вес: ${product.weight}"
        holder.productPrice.text = "Цена: ${product.price}"
        holder.productQuantity.text = "1"

        var currentQuantity = 1
        holder.productQuantity.text = currentQuantity.toString()

        holder.minusButton.setOnClickListener {
            if (currentQuantity > 1) {
                currentQuantity--
                holder.productQuantity.text = currentQuantity.toString()
                onQuantityChange(product, currentQuantity) // Уведомляем об изменении
            }
        }

//        holder.minusButton.setOnClickListener {
//            val quantity = holder.productQuantity.text.toString().toInt() - 1
//            if (quantity > 0) {
//                holder.productQuantity.text = quantity.toString()
//                onQuantityChange(product, quantity)
//            }
//        }

        holder.plusButton.setOnClickListener {
            currentQuantity++
            holder.productQuantity.text = currentQuantity.toString()
            onQuantityChange(product, currentQuantity) // Уведомляем об изменении
        }

//        holder.plusButton.setOnClickListener {
//            val quantity = holder.productQuantity.text.toString().toInt() + 1
//            holder.productQuantity.text = quantity.toString()
//            onQuantityChange(product, quantity)
//        }
    }

    override fun getItemCount() = cartList.size
}
