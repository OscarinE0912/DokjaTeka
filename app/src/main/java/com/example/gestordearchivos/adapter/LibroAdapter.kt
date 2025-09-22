package com.example.gestordearchivos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestordearchivos.model.Libro

class LibroAdapter(
    private val libros: List<Libro>,
    private val onClick: (Libro) -> Unit
) : RecyclerView.Adapter<LibroAdapter.LibroViewHolder>() {

    class LibroViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitulo: TextView = view.findViewById(R.id.tvTitulo)
        val tvAutor: TextView = view.findViewById(R.id.tvAutor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibroViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_libro, parent, false)
        return LibroViewHolder(view)
    }

    override fun onBindViewHolder(holder: LibroViewHolder, position: Int) {
        val libro = libros[position]
        holder.tvTitulo.text = libro.titulo
        holder.tvAutor.text = libro.autor
        holder.itemView.setOnClickListener { onClick(libro) }
    }

    override fun getItemCount(): Int = libros.size
}
