package com.example.hotcue

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.hotcue.ui.votes.VotesFragment

class AntesVotarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_antes_de_votar)


        val btnVoltarAtras = findViewById<Button>(R.id.btnVoltarAtras)

        btnVoltarAtras.setOnClickListener {
            // Create an Intent to start the new activity
            val intent = Intent(this, VotesFragment::class.java)
            startActivity(intent)
        }
    }



}