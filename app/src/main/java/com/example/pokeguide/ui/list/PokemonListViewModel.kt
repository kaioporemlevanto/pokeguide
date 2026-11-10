package com.example.pokeguide.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeguide.data.model.PokemonResult
import com.example.pokeguide.data.repository.PokemonRepository
import kotlinx.coroutines.launch

class PokemonListViewModel : ViewModel() {

    private val repository = PokemonRepository()

    private val _pokemonList = MutableLiveData<List<PokemonResult>>()
    val pokemonList: LiveData<List<PokemonResult>> get() = _pokemonList

    private val currentList = mutableListOf<PokemonResult>()
    private var offset = 0
    private val limit = 20 // Carrega de 20 em 20 para otimizar o grid

    init {
        loadMorePokemons()
    }

    fun loadMorePokemons() {
        viewModelScope.launch {
            try {
                val response = repository.getPokemonList(offset, limit)
                currentList.addAll(response.results)
                _pokemonList.value = currentList
                offset += limit // Incrementa o offset para a próxima página
            } catch (e: Exception) {
                // Tratamento de erro de rede se necessário
            }
        }
    }
}