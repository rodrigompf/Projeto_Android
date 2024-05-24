package com.example.hotcue

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.hotcue.ui.filmes.FilmesFragment

class Adapter(
    private val orientarVotosList: ArrayList<OrientacaoVotos>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<Adapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(orientacaoVoto: OrientacaoVotos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.template_quadrado_votacao_aberta, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val orientar: OrientacaoVotos = orientarVotosList[position]
        holder.titulo.text = orientar.Titulo
        holder.votos.text = orientar.Votos.toString() // Convert Int to String
        holder.timer.text = orientar.Timer.toString() // Convert Long to String

        // Set OnClickListener for the button
        holder.antesVotarButton.setOnClickListener {
            // Call onItemClick method of the listener interface
            itemClickListener.onItemClick(orientar)
        }
    }

    override fun getItemCount(): Int {
        return orientarVotosList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo: TextView = itemView.findViewById(R.id.Titulo_get)
        val votos: TextView = itemView.findViewById(R.id.Votos_get)
        val timer: TextView = itemView.findViewById(R.id.Timer_get)
        val antesVotarButton: Button = itemView.findViewById(R.id.AntesVotarButton)
    }
}