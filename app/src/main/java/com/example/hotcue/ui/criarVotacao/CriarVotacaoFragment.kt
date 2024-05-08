package com.example.hotcue.ui.criarVotacao

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.hotcue.R
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date
import java.util.UUID

class CriarVotacaoFragment: Fragment() {
    private val db = FirebaseFirestore.getInstance()
    data class Vote(
        val vtFinalizada: Boolean,
        val vtIdentificacao: String,
        val vtTipo: String,
        val vtUtilizador: String,
        val vtVotos: Char
    )
    data class Votecampos(
        val vt_campo:String

    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_criar_votacao, container,false)
        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val botao_criar_voto = view.findViewById<AppCompatButton>(R.id.botao_criar_voto)
        val linear_criar_voto = view.findViewById<LinearLayout>(R.id.linear_criar_voto)

        val botao_enviar_voto = view.findViewById<AppCompatButton>(R.id.botao_enviar_voto)
        val linear_receber_voto = view.findViewById<LinearLayout>(R.id.linear_receber_voto)


        botao_criar_voto.setOnClickListener {
            val templateCriarVoto = LayoutInflater.from(view.context).inflate(R.layout.template_criar_voto, null, false)
            linear_criar_voto.addView(templateCriarVoto)
        }

        //botao_enviar_voto.setOnClickListener {
            //val templateQuadradoVotacaoAberta = LayoutInflater.from(view.context).inflate(R.layout.template_quadrado_votacao_aberta,null,false)
            //linear_receber_voto.addView(templateQuadradoVotacaoAberta)
       // }

        botao_enviar_voto.setOnLongClickListener{
            addVoteToFirestore()
        }
    }




     fun adicionar_votacao(){

                // Example vote
                val vote = Vote(
                    vtFinalizada = false,
                    vtIdentificacao = "Some identification",
                    vtTipo = "Some type",
                    vtUtilizador = "User ID",
                    vtVotos = 'A'
                )
        adicionar_campos()


    }

    fun adicionar_campos(){
        val linear_criar_voto = view.findViewById<LinearLayout>(R.id.linear_criar_voto)
        for (i in 0 until linear_criar_voto.childCount) {
            val votoView = linear_criar_voto.getChildAt(i)
            if (votoView is LinearLayout) {
                // Encontrar o EditText dentro do layout do voto
                val editText = votoView.findViewById<EditText>(R.id.txt_criar_voto)

                // Obter o texto do EditText
                val textoVoto = editText.text.toString()

                // Adicionar lógica aqui para fazer o que quiser com o valor do voto
                // Por exemplo, adicionar à lista de votos ou realizar outra ação
                val votecampo = Votecampos(
                    vt_campo = textoVoto
                )
            }
        }
    }

    fun addVoteToFirestore(vote: Vote) {
        // Access Firestore instance
        val db = FirebaseFirestore.getInstance()

        // Add the vote to the "votacoes" collection
        db.collection("votacoes")
            .add(vote)
            .addOnSuccessListener { documentReference ->
                // Handle successful addition
                var idaux= view.findViewById<EditText>(R.id.idAux)
                val cenascoisaas :String =(documentReference.id)

               idaux = cenascoisaas.toString()
                addVotecamposToFirestore(idaux: String)
            }
            .addOnFailureListener { e ->
                // Handle errors
                println("Error adding vote: $e")
            }
    }
    fun addVotecamposToFirestore(votecampos: Votecampos) {
        // Access Firestore instance
        val db = FirebaseFirestore.getInstance()

        // Add the vote to the "votacoes" collection
        db.collection("campos_votacao")
            .add(votecampos)
            .addOnSuccessListener { documentReference ->
                // Handle successful addition
                println("Vote added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                // Handle errors
                println("Error adding vote: $e")
            }
    }



}






