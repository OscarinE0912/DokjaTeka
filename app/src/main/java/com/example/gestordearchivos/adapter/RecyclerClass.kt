package com.example.gestordearchivos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestordearchivos.R

data class Item(val titulo: String, val descripcion: String)

class RecyclerClass(
    private val lista: List<Item>,
    private val onClick: (Item) -> Unit
) : RecyclerView.Adapter<RecyclerClass.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.tvTitulo)
        val descripcion: TextView = view.findViewById(R.id.tvDescripcion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = lista[position]
        holder.titulo.text = item.titulo
        holder.descripcion.text = item.descripcion
        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount(): Int = lista.size
}
