package com.example.pokeguide.data.local

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.pokeguide.data.model.PokemonResult

class FavoritesManager(context: Context) {
    private val prefs = context.getSharedPreferences("poke_favorites_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val keyFavorites = "favorite_pokemons_list"

    fun getFavorites(): MutableList<PokemonResult> {
        val json = prefs.getString(keyFavorites, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<PokemonResult>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveFavorite(pokemon: PokemonResult) {
        val favorites = getFavorites()
        if (favorites.none { it.name == pokemon.name }) {
            favorites.add(pokemon)
            prefs.edit().putString(keyFavorites, gson.toJson(favorites)).apply()
        }
    }

    fun removeFavorite(pokemonName: String) {
        val favorites = getFavorites()
        favorites.removeAll { it.name.equals(pokemonName, ignoreCase = true) }
        prefs.edit().putString(keyFavorites, gson.toJson(favorites)).apply()
    }

    fun isFavorite(pokemonName: String): Boolean {
        return getFavorites().any { it.name.equals(pokemonName, ignoreCase = true) }
    }
}