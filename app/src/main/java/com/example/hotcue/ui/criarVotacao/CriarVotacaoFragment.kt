package com.example.hotcue.ui.criarVotacao

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.hotcue.R
import com.google.firebase.firestore.FirebaseFirestore

class CriarVotacaoFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()

    data class Vote(
        val vtFinalizada: Boolean,
        val vtIdentificacao: String,
        val vtTipo: String,
        val vtUtilizador: String,
        val vtVotos: Char
    )

    data class Votecampos(
        val vt_campo: String
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_criar_votacao, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FirebaseFirestore.getInstance()

        val botaoCriarVoto = view.findViewById<AppCompatButton>(R.id.botao_criar_voto)
        val linearCriarVoto = view.findViewById<LinearLayout>(R.id.linear_criar_voto)
        val botaoEnviarVoto = view.findViewById<AppCompatButton>(R.id.botao_enviar_voto)

        botaoCriarVoto.setOnClickListener {
            val templateCriarVoto = LayoutInflater.from(requireContext())
                .inflate(R.layout.template_criar_voto, null, false)
            linearCriarVoto.addView(templateCriarVoto)
        }

        botaoEnviarVoto.setOnClickListener {
            adicionarVotacao()
        }
    }

    fun adicionarVotacao() {
        val linearCriarVoto = requireView().findViewById<LinearLayout>(R.id.linear_criar_voto)
        for (i in 0 until linearCriarVoto.childCount) {
            val votoView = linearCriarVoto.getChildAt(i)
            if (votoView is LinearLayout) {
                val editText = votoView.findViewById<EditText>(R.id.txt_criar_voto)
                val textoVoto = editText.text.toString()
                addVoteToFirestore(textoVoto)
            }
        }
    }

    fun addVoteToFirestore(textoVoto: String) {
        Log.d("MeuApp", "Texto do Voto: $textoVoto")
        val vote = hashMapOf(
            "vtFinalizada" to false,
            "vtIdentificacao" to "", // Você pode definir isso conforme necessário
            "vtTipo" to "", // Você pode definir isso conforme necessário
            "vtUtilizador" to "", // Você pode definir isso conforme necessário
            "vtVotos" to ""
        )

        db.collection("votacoes")
            .add(vote)
            .addOnSuccessListener { documentReference ->
                // Adicionado com sucesso ao Firestore
            }
            .addOnFailureListener { e ->
                // Tratar erros
            }
    }
}



