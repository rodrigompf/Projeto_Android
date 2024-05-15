package com.example.hotcue.ui.votes

import android.media.session.MediaSessionManager.OnMediaKeyEventSessionChangedListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotcue.Adapter // Assuming Adapter is a custom adapter class
import com.example.hotcue.OrientacaoVotos
import com.example.hotcue.R
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject


class VotesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Adapter
    private lateinit var db: FirebaseFirestore
    private lateinit var orientacaoVotos: ArrayList<OrientacaoVotos>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_votesfilme, container, false)

        recyclerView = view.findViewById(R.id.recyclar)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        orientacaoVotos = arrayListOf()

        adapter = Adapter(orientacaoVotos)

        recyclerView.adapter =adapter

        EventChangeListener()

        return view
    }

    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("votacoes").addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("Firestore error", error.message.toString())
                return@addSnapshotListener
            }

            value?.let { snapshot ->
                for (dc in snapshot.documentChanges) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        val orientacaoVoto = dc.document.toObject(OrientacaoVotos::class.java)
                        orientacaoVotos.add(orientacaoVoto)
                        adapter.notifyItemInserted(orientacaoVotos.size - 5)
                    }
                }
            }
        }
    }

}