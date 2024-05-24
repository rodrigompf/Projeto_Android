package com.example.hotcue

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class AntesVotarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_antes_de_votar)

        val btnVoltarAtras = findViewById<Button>(R.id.btnVoltarAtras)

        btnVoltarAtras.setOnClickListener {
            // Finish the current activity and return to the parent activity (MainActivity)
            finish()
        }
    }
}