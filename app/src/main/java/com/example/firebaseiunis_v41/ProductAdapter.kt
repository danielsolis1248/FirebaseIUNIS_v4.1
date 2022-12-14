package com.example.firebaseiunis_v41

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseiunis_v41.databinding.ItemProductBinding
import java.lang.reflect.Type
import java.text.FieldPosition

class ProductAdapter (private val productList:MutableList<Product>, private val listener: OnProductListener):
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private lateinit var context:Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_product,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        val product = productList[position]
        holder.setListener(product)
        holder.binding.tvName.text = product.name
        holder.binding.tvPrice.text = product.precio.toString()
        holder.binding.tvQuantity.text = product.quantity.toString()
    }

    override fun getItemCount(): Int = productList.size

    fun add(product: Product){
        if(!productList.contains(product)){
            productList.add(product)
            notifyItemInserted(productList.size-1)
        }
    }

    fun update(product: Product) {
        TODO("Not yet implemented")
    }

    fun delete(product: Product) {
        TODO("Not yet implemented")
    }

    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val binding = ItemProductBinding.bind(view)

        fun setListener(product: Product) {

            binding.root.setOnClickListener {
                listener.onClick(product)
            }

            binding.root.setOnLongClickListener {
                listener.onLongClick(product)
                true
            }

        }

    }



}