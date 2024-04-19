package com.example.hotcue

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class testebasededados : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_testebasededados)

        FirebaseApp.initializeApp(this)

        // Initialize EditText fields
        var editText1 = findViewById<EditText>(R.id.editText1)
        var editText2 = findViewById<EditText>(R.id.editText2)

        var editText3 = findViewById<EditText>(R.id.editText3)
        var editText4 = findViewById<EditText>(R.id.editText4)



        // Read data from Firestore
        readDataFromFirestore(editText1,editText2);

        val addButton = findViewById<Button>(R.id.addButton)

        addButton.setOnClickListener {
            addDataToFirestore(editText3, editText4)
        }
    }

    private fun readDataFromFirestore(editText1: EditText, editText2: EditText) {
        val db = FirebaseFirestore.getInstance()

        db.collection("Teste").document("teste")
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val teste1 = document.getString("teste1")
                    val teste2 = document.getLong("teste2")

                    // Set data to EditText fields

                    editText1.setText(teste1)
                    editText2.setText(teste2?.toString() ?: "")
                } else {
                    println("Documento não existe")
                }
            }
            .addOnFailureListener { exception ->
                println("Falha ao ler valores do Firestore: $exception")
            }
    }


    private fun addDataToFirestore(editText3: EditText, editText4: EditText) {
        val db = FirebaseFirestore.getInstance()

        val teste3Texto = editText3.text.toString();
        val teste4Texto = editText4.text.toString().toLongOrNull()
        // Create a new user with a first and last name
        val user = hashMapOf(
            "teste1" to teste3Texto ,
            "teste2" to teste4Texto// Replace with your number value
        )

        // Add a new document with a generated ID
        db.collection("Teste")
            .add(user)
            .addOnSuccessListener { documentReference ->
                println("DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                println("Error adding document: $e")
            }
    }



}