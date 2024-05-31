package com.example.hotcue.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.hotcue.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SettingsFragment: Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val usernameTextView: TextView = view.findViewById(R.id.Username1)
        val emailTextView: TextView = view.findViewById(R.id.ed_email)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val email = currentUser.email
            if (email != null) {
                emailTextView.text = email
                db.collection("utilizadores").document(email).get()
                    .addOnSuccessListener { document ->
                        if (document != null && document.exists()) {
                            val username = document.getString("username")
                            usernameTextView.text = username
                        } else {
                            // Handle the case where the document does not exist
                            usernameTextView.text = "Username not found"
                        }
                    }
                    .addOnFailureListener { exception ->
                        // Handle any errors
                        usernameTextView.text = "Error: ${exception.message}"
                    }
            }
        }

        return view
    }
}