package com.example.hotcue

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
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
    private lateinit var category: String
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
        category = intent.getStringExtra("category") ?: "Jogos"

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

        val addProposalButton = findViewById<Button>(R.id.adicionarproposta)
        addProposalButton.setOnClickListener {
            showAddProposalDialog()
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
            }
        })
        recyclerView.adapter = adapter

        db = FirebaseFirestore.getInstance()

        loadVotacoesItems()
    }

    private fun loadVotacoesItems() {
        db.collection("votacoes")
            .document(category)
            .collection("items")
            .document(id)
            .collection("list")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Firestore error", error.message.toString())
                    return@addSnapshotListener
                }

                orientacaoVotos.clear()
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

    private fun showAddProposalDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Adicionar Proposta")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, which ->
            val proposalText = input.text.toString()
            saveProposal(proposalText)
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }

        builder.show()
    }

    private fun saveProposal(proposalText: String) {
        val proposal = hashMapOf(
            "Titulo" to proposalText,
            "votos" to 0
        )

        val listRef = db.collection("votacoes")
            .document(category)
            .collection("items")
            .document(id)
            .collection("list")

        listRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val documents = task.result
                val maxNumber = documents?.mapNotNull { it.id.toIntOrNull() }?.maxOrNull() ?: 0
                val newNumber = maxNumber + 1

                listRef.document(newNumber.toString()).set(proposal)
                    .addOnSuccessListener {
                        Log.d("Firestore", "DocumentSnapshot written with ID: $newNumber")
                        loadVotacoesItems()
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firestore", "Error adding document", e)
                    }
            } else {
                Log.w("Firestore", "Error checking collection existence", task.exception)
            }
        }
    }
}
