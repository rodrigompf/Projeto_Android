package com.example.hotcue.ui.filmes

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

class FilmesFragment: Fragment(), Adapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Adapter
    private lateinit var db: FirebaseFirestore
    private lateinit var orientacaoVotos: ArrayList<OrientacaoVotos>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_filmes, container, false)

        recyclerView = view.findViewById(R.id.recyclar)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        orientacaoVotos = ArrayList()
        adapter = Adapter(orientacaoVotos,this)
        recyclerView.adapter = adapter

        // Initialize Firestore here
        db = FirebaseFirestore.getInstance()

        EventChangeListener()

        return view
    }

    private fun EventChangeListener() {
        db.collection("votacoes")
            .document("Filmes") // Assuming "Filmes" is a document, change this according to your Firestore structure
            .collection("items")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Firestore error", error.message.toString())
                    // Handle error, if needed
                    return@addSnapshotListener
                }

                value?.let { snapshot ->
                    for (dc in snapshot.documentChanges) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            val orientacaoVoto = dc.document.toObject(OrientacaoVotos::class.java)
                            orientacaoVotos.add(orientacaoVoto)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }

    }
    override fun onItemClick(orientacaoVoto: OrientacaoVotos) {
        // Create an Intent to start AntesVotarActivity
        val intent = Intent(requireContext(), AntesVotarActivity::class.java).apply {
            // Pass selected item data to AntesVotarActivity
            putExtra("selectedItem", orientacaoVoto)
        }
        // Start the activity if the context is not null
        requireContext().startActivity(intent)
    }

}