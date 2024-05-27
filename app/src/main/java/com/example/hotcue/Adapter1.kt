package com.example.hotcue

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter1(
    private val orientarVotosList: ArrayList<OrientacaoVotos>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<Adapter1.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(orientacaoVoto: OrientacaoVotos)
        fun onVoteButtonClick(orientacaoVoto: OrientacaoVotos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.template_antesvotar, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val orientar = orientarVotosList[position]
        holder.titulo.text = orientar.Titulo
        holder.votes.text = orientar.Votos.toString()

        // Set OnClickListener for the item view
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(orientar)
        }

        // Set OnClickListener for the vote button
        holder.voteButton.setOnClickListener {
            itemClickListener.onVoteButtonClick(orientar)
        }
    }

    override fun getItemCount(): Int {
        return orientarVotosList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo: TextView = itemView.findViewById(R.id.Titulo_descrition)
        val votes: TextView = itemView.findViewById(R.id.votos_item)
        val voteButton: Button = itemView.findViewById(R.id.button_votes)
    }
}