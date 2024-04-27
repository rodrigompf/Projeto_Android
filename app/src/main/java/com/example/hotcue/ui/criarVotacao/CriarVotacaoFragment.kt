package com.example.hotcue.ui.criarVotacao

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.hotcue.R
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date
import java.util.UUID

class CriarVotacaoFragment: Fragment() {
    private val db = FirebaseFirestore.getInstance()
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

    }

    private fun criarVotacao() {
        // Coleta os dados dos campos preenchidos pelo usuário
        val vt_data_fim = Date() // Aqui você obteria a data final da votação, por exemplo
        val vt_data_inicio = Date() // Aqui você obteria a data de início da votação, por exemplo
        val vt_finalizado = false // Aqui você obteria o estado da votação (se está finalizada ou não), por exemplo
        val vt_identificacao = "Identificação da votação" // Aqui você obteria a identificação da votação, por exemplo
        val vt_tipo = "Tipo da votação" // Aqui você obteria o tipo da votação, por exemplo
        val vt_utilizador = "Utilizador responsável" // Aqui você obteria o utilizador responsável pela votação, por exemplo
        val vt_votos = "Votos da votação" // Aqui você obteria os votos da votação, por exemplo

        // Gere um identificador único para a votação
        val votacaoId = UUID.randomUUID().toString()

        // Crie um mapa de dados com os dados da votação
        val votacaoData = hashMapOf(
            "vt_data_fim" to vt_data_fim,
            "vt_data_inicio" to vt_data_inicio,
            "vt_finalizado" to vt_finalizado,
            "vt_identificacao" to vt_identificacao,
            "vt_tipo" to vt_tipo,
            "vt_utilizador" to vt_utilizador,
            "vt_votos" to vt_votos
        )

        // Supondo que você tenha uma lista de campos de votação e seus valores
        val camposVotacao = hashMapOf<String, Any>(
            "campo1" to "valor1",
            "campo2" to "valor2",
            // Adicione os campos de votação conforme necessário
        )

        // Adicione os dados dos campos de votação ao mapa de dados da votação
        val votacaoComCampos = HashMap<String, Any>()
        votacaoComCampos.putAll(votacaoData)
        votacaoComCampos.putAll(camposVotacao)

        // Salve os dados da votação na coleção "votações" no Firestore
        db.collection("votações")
            .document(votacaoId)
            .set(votacaoData)
            .addOnSuccessListener {
                // Votação salva com sucesso
                println("Votação criada com sucesso!")
            }
            .addOnFailureListener { e ->
                // Ocorreu um erro ao salvar a votação
                println("Erro ao criar a votação: $e")
            }
    }
}






