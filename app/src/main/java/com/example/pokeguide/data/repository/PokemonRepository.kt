package com.example.pokeguide.data.repository

import com.example.pokeguide.data.api.RetrofitClient
import com.example.pokeguide.data.model.Pokemon
import com.example.pokeguide.data.model.PokemonResponse

class PokemonRepository {
    private val api = RetrofitClient.api

    suspend fun getPokemonByName(name: String): Pokemon {
        return api.getPokemonByName(name)
    }

    // Método para buscar a lista paginada
    suspend fun getPokemonList(offset: Int, limit: Int): PokemonResponse {
        return api.getPokemonList(offset, limit)
    }
}