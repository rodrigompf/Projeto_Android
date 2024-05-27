package com.example.hotcue

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentChange

class AntesVotarActivity : AppCompatActivity() {
    private lateinit var titulo: String
    private lateinit var descricao: String
    private lateinit var timer: String
    private lateinit var id: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Adapter1
    private lateinit var orientacaoVotos: ArrayList<OrientacaoVotos>
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_antes_de_votar)

        titulo = intent.getStringExtra("title") ?: ""
        descricao = intent.getStringExtra("description") ?: ""
        timer = intent.getStringExtra("timer") ?: ""
        id = intent.getStringExtra("id") ?: ""

        val tituloTextView = findViewById<TextView>(R.id.TituloAntes)
        val descricaoTextView = findViewById<TextView>(R.id.DescriçãoAntes)
        val timerTextView = findViewById<TextView>(R.id.TimerAntes)

        tituloTextView.text = titulo
        descricaoTextView.text = descricao
        timerTextView.text = timer

        val btnVoltarAtras = findViewById<Button>(R.id.btnVoltarAtras)
        btnVoltarAtras.setOnClickListener {
            finish()
        }

        recyclerView = findViewById(R.id.recyclar)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        orientacaoVotos = ArrayList()
        adapter = Adapter1(orientacaoVotos, object : Adapter1.OnItemClickListener {
            override fun onItemClick(orientacaoVoto: OrientacaoVotos) {
                // Handle item click if needed
            }

            override fun onVoteButtonClick(orientacaoVoto: OrientacaoVotos) {
                // Handle vote button click
                // You can implement the logic here to update the vote count in the database
            }
        })
        recyclerView.adapter = adapter

        db = FirebaseFirestore.getInstance()

        loadVotacoesItems()
    }

    private fun loadVotacoesItems() {
        db.collection("votacoes")
            .document("Jogos")
            .collection("items")
            .document(id)
            .collection("list")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Firestore error", error.message.toString())
                    return@addSnapshotListener
                }

                orientacaoVotos.clear() // Clear the list before adding new items
                for (dc in value!!.documentChanges) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        val data = dc.document.data
                        val votos = when (val votosField = data["votos"]) {
                            is Long -> votosField.toInt()
                            is String -> votosField.toIntOrNull() ?: 0
                            else -> 0
                        }

                        val orientacaoVoto = OrientacaoVotos(
                            id = dc.document.id,
                            Titulo = data["Titulo"] as? String,
                            Descrição = data["Descrição"] as? String,
                            Timer = data["Timer"] as? Long,
                            Votos = votos
                        )
                        orientacaoVotos.add(orientacaoVoto)
                    }
                }
                adapter.notifyDataSetChanged()
            }
    }
}
