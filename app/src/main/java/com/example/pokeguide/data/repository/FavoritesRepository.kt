package com.example.pokeguide.data.repository

import com.example.pokeguide.data.model.PokemonResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FavoritesRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun getUserFavoritesCollection() =
        auth.currentUser?.uid?.let { uid ->
            db.collection("users").document(uid).collection("favorites")
        }

    fun saveFavorite(pokemon: PokemonResult, onComplete: (Boolean) -> Unit = {}) {
        val collection = getUserFavoritesCollection() ?: return
        collection.document(pokemon.name.lowercase())
            .set(pokemon)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun removeFavorite(pokemonName: String, onComplete: (Boolean) -> Unit = {}) {
        val collection = getUserFavoritesCollection() ?: return
        collection.document(pokemonName.lowercase())
            .delete()
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun isFavorite(pokemonName: String, onResult: (Boolean) -> Unit) {
        val collection = getUserFavoritesCollection()
        if (collection == null) {
            onResult(false)
            return
        }
        collection.document(pokemonName.lowercase())
            .get()
            .addOnSuccessListener { document -> onResult(document.exists()) }
            .addOnFailureListener { onResult(false) }
    }

    fun observeFavorites(onUpdate: (List<PokemonResult>) -> Unit) {
        val collection = getUserFavoritesCollection() ?: return
        collection.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) return@addSnapshotListener
            val list = snapshot.documents.mapNotNull { it.toObject(PokemonResult::class.java) }
            onUpdate(list)
        }
    }
}