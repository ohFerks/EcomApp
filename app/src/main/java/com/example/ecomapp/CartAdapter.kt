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
        holder.productWeight.text = "${product.weight} кг"
        holder.productPrice.text = "${product.price} ₽"
        holder.productQuantity.text = product.quantity.toString()

        holder.minusButton.setOnClickListener {
            if (product.quantity > 1) {
                product.quantity--
                holder.productQuantity.text = product.quantity.toString()
                //
                //notifyItemChanged(position)

                onQuantityChange(product, product.quantity) // Уведомляем об изменении
            }
            else{
                // Удаляем продукт, если количество равно 1
                cartList.removeAt(position)
                //
                notifyItemRemoved(position)
                //
                onQuantityChange(product, 0)// Уведомляем о удалении продукта
            }
        }

        holder.plusButton.setOnClickListener {
            product.quantity++
            holder.productQuantity.text = product.quantity.toString()
            onQuantityChange(product, product.quantity) // Уведомляем об изменении
        }
    }

    override fun getItemCount(): Int = cartList.size
}
