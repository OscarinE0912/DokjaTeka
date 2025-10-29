package com.example.gestordearchivos

<<<<<<< HEAD
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestordearchivos.model.Libro
=======
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestordearchivos.model.Libro
import java.io.File
>>>>>>> 11ca62c (Primer commit del proyecto)

class LibroAdapter(
    private val libros: List<Libro>,
    private val onClick: (Libro) -> Unit
) : RecyclerView.Adapter<LibroAdapter.LibroViewHolder>() {

    class LibroViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitulo: TextView = view.findViewById(R.id.tvTitulo)
        val tvAutor: TextView = view.findViewById(R.id.tvAutor)
<<<<<<< HEAD
=======
        val imgPortada: ImageView = view.findViewById(R.id.imgPortada)
>>>>>>> 11ca62c (Primer commit del proyecto)
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
<<<<<<< HEAD
=======

        if (!libro.portadaResId.isNullOrEmpty()) {
            val imgFile = File(libro.portadaResId)
            if (imgFile.exists()) {
                holder.imgPortada.setImageURI(Uri.fromFile(imgFile))
            } else {
                holder.imgPortada.setImageResource(R.mipmap.placeholder_libro_foreground)
            }
        } else {
            holder.imgPortada.setImageResource(R.mipmap.placeholder_libro_foreground)
        }

>>>>>>> 11ca62c (Primer commit del proyecto)
        holder.itemView.setOnClickListener { onClick(libro) }
    }

    override fun getItemCount(): Int = libros.size
}
