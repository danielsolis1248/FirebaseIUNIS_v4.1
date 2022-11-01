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
        TODO("No yet implemented")
    }

    override fun getItemCount(): Int = productList.size

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