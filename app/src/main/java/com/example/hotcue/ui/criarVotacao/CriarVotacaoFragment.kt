package com.example.hotcue.ui.criarVotacao

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.hotcue.R

class CriarVotacaoFragment: Fragment() {

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

        botao_enviar_voto.setOnClickListener {
            val templateQuadradoVotacaoAberta = LayoutInflater.from(view.context).inflate(R.layout.template_quadrado_votacao_aberta,null,false)
            linear_receber_voto.addView(templateQuadradoVotacaoAberta)
        }

    }
}