package com.example.gestordearchivos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestordearchivos.R
import com.example.gestordearchivos.model.Libro
import com.bumptech.glide.Glide
import java.io.File

class LibroAdapter(
    private val listaLibros: List<Libro>,
    private val onClick: (Libro) -> Unit
) : RecyclerView.Adapter<LibroAdapter.LibroViewHolder>() {

    inner class LibroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagePortada: ImageView = itemView.findViewById(R.id.imageViewPortada)
        val textTitulo: TextView = itemView.findViewById(R.id.textViewTitulo)
        val textAutor: TextView = itemView.findViewById(R.id.textViewAutor)
        val textGenero: TextView = itemView.findViewById(R.id.textViewGenero)
        val textSinopsis: TextView = itemView.findViewById(R.id.textViewSinopsis)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progresoLibro)
        val textPorcentaje: TextView = itemView.findViewById(R.id.textViewPorcentaje)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibroViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_libro, parent, false)
        return LibroViewHolder(view)
    }

    override fun onBindViewHolder(holder: LibroViewHolder, position: Int) {
        val libro = listaLibros[position]
        holder.textTitulo.text = libro.titulo
        holder.textAutor.text = libro.autor
        holder.textGenero.text = libro.genero
        holder.textSinopsis.text = libro.sinopsis
        holder.progressBar.progress = libro.progreso
        holder.textPorcentaje.text = "Avance: ${libro.progreso}%"

        // ✅ Cargar portada con Glide (archivo local o URL)
        val portada = libro.rutaPortada
        if (!portada.isNullOrEmpty()) {
            val file = File(portada)
            Glide.with(holder.itemView.context)
                .load(if (file.exists()) file else portada)
                .placeholder(R.mipmap.placeholder_libro_foreground)
                .error(R.mipmap.placeholder_libro_foreground)
                .into(holder.imagePortada)
        } else {
            holder.imagePortada.setImageResource(R.mipmap.placeholder_libro_foreground)
        }

        holder.itemView.setOnClickListener { onClick(libro) }
    }

    override fun getItemCount(): Int = listaLibros.size
}
