package com.example.hotcue

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

class Adapter(private val OrientarVotosList : ArrayList<OrientacaoVotos>) : RecyclerView.Adapter<Adapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.template_quadrado_votacao_aberta, parent,false)

        return   MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: Adapter.MyViewHolder, position: Int) {
        val orientar: OrientacaoVotos = OrientarVotosList[position]
        holder.Titulo.text = orientar.Titulo
        holder.Votos.text = orientar.Votos.toString() // Convert Int to String
        holder.Timer.text = orientar.Timer.toString() // Convert Long to String
    }

    override fun getItemCount(): Int {
        return OrientarVotosList.size
    }

    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val Titulo : TextView = itemView.findViewById(R.id.Titulo_get)
        val Votos : TextView = itemView.findViewById(R.id.Votos_get)
        val Timer : TextView = itemView.findViewById(R.id.Timer_get)

    }
}