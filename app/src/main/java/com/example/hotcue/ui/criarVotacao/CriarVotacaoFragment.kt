package com.example.hotcue.ui.criarVotacao

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.hotcue.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CriarVotacaoFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_criar_votacao, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email

        currentUserEmail?.let { email ->

            val etextView = view.findViewById<EditText>(R.id.textView)
            val botaoEnviarVoto = view.findViewById<AppCompatButton>(R.id.botao_enviar_voto)
            val Titulo = view.findViewById<EditText>(R.id.Titulo)
            val spinnerTipoVotacao = view.findViewById<Spinner>(R.id.spinner_tipo_votacao)
            val spinnerTimer = view.findViewById<Spinner>(R.id.spinner_timer)

            // Configure Spinner for Timer
            val timerValues = arrayOf("15", "30", "45", "60", "120", "180")
            val timerAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, timerValues)
            timerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerTimer.adapter = timerAdapter

            // Configure Spinner for Tipo Votacao
            val tipoVotacaoValues = arrayOf("Filmes", "Jogos", "Musica", "Outros")
            val tipoVotacaoAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                tipoVotacaoValues
            )
            tipoVotacaoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerTipoVotacao.adapter = tipoVotacaoAdapter

            botaoEnviarVoto.setOnClickListener {
                val stextView = etextView.text.toString().trim()
                val eTitulo = Titulo.text.toString().trim() // Trim whitespace from title
                val eTimer = spinnerTimer.selectedItem.toString().trim().toIntOrNull()
                    ?: 0 // Convert timer value to Int, default to 0 if conversion fails
                val tipoVotacao =
                    spinnerTipoVotacao.selectedItem.toString().trim() // Get the selected type

                val userMap = hashMapOf(
                    "Descrição" to stextView,
                    "Titulo" to eTitulo,
                    "UtilizadorEmail" to email, // Saving user's email instead of username
                    "Votos" to 0,
                    "Timer" to eTimer
                )

                // Get the current count of documents in the selected type collection
                db.collection("votacoes").document(tipoVotacao).collection("items")
                    .get()
                    .addOnSuccessListener { result ->
                        val documentCount = result.size() // Count of existing documents

                        // Set the document with an incremented identifier
                        db.collection("votacoes").document(tipoVotacao)
                            .collection("items")
                            .document((documentCount + 1).toString()) // Assigning a unique number
                            .set(userMap)
                            .addOnSuccessListener {
                                etextView.text.clear()
                                Titulo.text.clear()
                                Toast.makeText(
                                    requireContext(),
                                    "Votação criada com sucesso!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener { exception ->
                                // Handle failure
                                Log.e(
                                    "CriarVotacaoFragment",
                                    "Error saving document",
                                    exception
                                )
                                Toast.makeText(
                                    requireContext(),
                                    "Erro ao criar votação",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                    .addOnFailureListener { exception ->
                        // Handle failure
                        Log.e(
                            "CriarVotacaoFragment",
                            "Error getting documents",
                            exception
                        )
                        Toast.makeText(
                            requireContext(),
                            "Erro ao buscar documentos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
    }
}




