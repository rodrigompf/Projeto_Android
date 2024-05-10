package com.example.hotcue.ui.criarVotacao

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.hotcue.R
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

        val etextView = view.findViewById<EditText>(R.id.textView)
        val botaoCriarVoto = view.findViewById<AppCompatButton>(R.id.botao_criar_voto)
        val linearCriarVoto = view.findViewById<LinearLayout>(R.id.linear_criar_voto)
        val botaoEnviarVoto = view.findViewById<AppCompatButton>(R.id.botao_enviar_voto)
        val Titulo = view.findViewById<EditText>(R.id.Titulo)
        val spinnerTipoVotacao = view.findViewById<Spinner>(R.id.spinner_tipo_votacao)
        botaoCriarVoto.setOnClickListener {
            val templateCriarVoto = LayoutInflater.from(requireContext())
                .inflate(R.layout.template_criar_voto, null, false)
            linearCriarVoto.addView(templateCriarVoto)
        }
        // Configurar um listener para o Spinner
        spinnerTipoVotacao.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        botaoEnviarVoto.setOnClickListener {
            val stextView = etextView.text.toString().trim()
            val currentSpinnerValue = spinnerTipoVotacao.selectedItem.toString()
            val eTitulo = Titulo.text.toString().trim()
            val userMap = hashMapOf(
                "Descrição" to stextView,
                "tipo" to currentSpinnerValue,
                "Titulo" to eTitulo
            )
            db.collection("campos_votacao").document("3").set(userMap)
                .addOnSuccessListener {
                    etextView.text.clear()
                }
                .addOnFailureListener {

                }
        }
    }
}


/*data class Vote(
        val vtFinalizada: Boolean,
        val vtIdentificacao = textView
        val vtTipo: String,
        val vtUtilizador: String,
        val vtVotos: Char

        data class Votecampos(
        val vt_campo: String
    )

    fun adicionarVotacao() {
        val linearCriarVoto = requireView().findViewById<LinearLayout>(R.id.linear_criar_voto)
        for (i in 0 until linearCriarVoto.childCount) {
            val votoView = linearCriarVoto.getChildAt(i)
            if (votoView is LinearLayout) {
                val editText = votoView.findViewById<EditText>(R.id.txt_criar_voto)
                val textoVoto = editText.text.toString().trim()
                addVoteToFirestore(textoVoto)
            }
        }
    }

    fun addVoteToFirestore(textoVoto: String) {
        val vote = hashMapOf(
            "vtFinalizada" to false,
            "vtIdentificacao" to stextView ,
            "vtTipo" to "",
            "vtUtilizador" to "",
            "vtVotos" to ""
        )

        db.collection("votacoes")
            .add(vote)
            .addOnSuccessListener { documentReference ->
            }
            .addOnFailureListener { e ->
            }
    }

    botaoEnviarVoto.setOnClickListener {
            adicionarVotacao()
        }
    )*/




