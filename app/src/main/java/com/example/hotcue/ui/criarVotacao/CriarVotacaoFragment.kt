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

        val user = FirebaseAuth.getInstance().currentUser

        user?.let { currentUser ->

            val email = currentUser.email

            val etextView = view.findViewById<EditText>(R.id.textView)
            val linearCriarVoto = view.findViewById<LinearLayout>(R.id.linear_criar_voto)
            val botaoEnviarVoto = view.findViewById<AppCompatButton>(R.id.botao_enviar_voto)
            val Titulo = view.findViewById<EditText>(R.id.Titulo)
            val spinnerTipoVotacao = view.findViewById<Spinner>(R.id.spinner_tipo_votacao)
            val spinnerTimer = view.findViewById<Spinner>(R.id.spinner_timer)

            // Configure Spinner for Timer
            val timerValues = arrayOf("15", "30", "45", "1", "2", "4")
            val timerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, timerValues)
            timerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerTimer.adapter = timerAdapter

            // Configurar um listener para o Spinner
            spinnerTipoVotacao.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val selectedItem = parent?.getItemAtPosition(position).toString()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }

            botaoEnviarVoto.setOnClickListener {
                val stextView = etextView.text.toString().trim()
                val currentSpinnerValue = spinnerTipoVotacao.selectedItem.toString()
                val eTitulo = Titulo.text.toString().trim()
                val eTimer = spinnerTimer.selectedItem.toString().trim().toIntOrNull() ?: 0 // Convert timer value to Int, default to 0 if conversion fails

                // Get the current count of documents
                db.collection("votacoes")
                    .get()
                    .addOnSuccessListener { result ->
                        val documentCount = result.size() // Count of existing documents
                        val userMap = hashMapOf(
                            "Descrição" to stextView,
                            "Identificador" to currentSpinnerValue,
                            "Titulo" to eTitulo,
                            "Utilizador" to email,
                            "Votos" to 0,
                            "Timer" to eTimer
                        )

                        // Set the document with an incremented identifier
                        db.collection("votacoes")
                            .document((documentCount + 1).toString()) // Assigning a unique number
                            .set(userMap)
                            .addOnSuccessListener {
                                etextView.text.clear()
                                Titulo.text.clear()
                            }
                            .addOnFailureListener { exception ->
                                // Handle failure
                            }
                    }
                    .addOnFailureListener { exception ->
                        // Handle failure
                    }
            }
        }
    }
}




