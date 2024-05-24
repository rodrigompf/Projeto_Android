package com.example.hotcue

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore

class AntesVotarActivity : AppCompatActivity() {
    private lateinit var titulo: String
    private lateinit var descricao: String
    private lateinit var timer: String // Assuming timer is of type String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_antes_de_votar)

        // Retrieve the title, description, and timer from intent extras
        titulo = intent.getStringExtra("title") ?: ""
        descricao = intent.getStringExtra("description") ?: ""
        timer = intent.getStringExtra("timer") ?: ""

        // Find and set TextViews to display title, description, and timer
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
    }
}
