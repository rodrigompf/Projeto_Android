package com.example.hotcue.ui.outros

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotcue.Adapter
import com.example.hotcue.AntesVotarActivity
import com.example.hotcue.OrientacaoVotos
import com.example.hotcue.R
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore

class OutrosFragment : Fragment(), Adapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Adapter
    private lateinit var db: FirebaseFirestore
    private lateinit var orientacaoVotos: ArrayList<OrientacaoVotos>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_outros, container, false)

        recyclerView = view.findViewById(R.id.recyclar)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        orientacaoVotos = ArrayList()
        adapter = Adapter(orientacaoVotos, this)
        recyclerView.adapter = adapter

        db = FirebaseFirestore.getInstance()


        EventChangeListener()

        return view
    }

    private fun EventChangeListener() {
        db.collection("votacoes")
            .document("Outros")
            .collection("items")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Firestore error", error.message.toString())
                    return@addSnapshotListener
                }
                for (dc in value!!.documentChanges) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        val orientacaoVoto = dc.document.toObject(OrientacaoVotos::class.java)
                        orientacaoVoto.id = dc.document.id // Store the document ID
                        orientacaoVotos.add(orientacaoVoto)

                        val title = orientacaoVoto.Titulo
                        val description = orientacaoVoto.Descrição
                        val timer = orientacaoVoto.Timer // Assuming timer is a field in OrientacaoVotos
                        Log.d("Firestore", "Titulo: $title, Descricao: $description, Timer: $timer")
                    }
                }
                adapter.notifyDataSetChanged()
            }
    }

    override fun onItemClick(orientacaoVoto: OrientacaoVotos) {
        // Retrieve the title, description, and timer of the selected item
        val title = orientacaoVoto.Titulo
        val description = orientacaoVoto.Descrição
        val timer = orientacaoVoto.Timer // Assuming timer is a field in OrientacaoVotos

        // Create an Intent to start AntesVotarActivity and pass the data as extras
        val intent = Intent(requireContext(), AntesVotarActivity::class.java).apply {
            putExtra("title", title)
            putExtra("description", description)
            putExtra("timer", timer)
            putExtra("id", orientacaoVoto.id)
        }

        // Start the activity if the context is not null
        requireContext().startActivity(intent)
    }
}