package com.example.pokeguide.ui.search // Ajuste para o seu pacote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeguide.data.model.Pokemon
import com.example.pokeguide.data.repository.PokemonRepository
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val repository = PokemonRepository()

    // LiveData para observar o sucesso da busca
    private val _pokemonResult = MutableLiveData<Pokemon?>()
    val pokemonResult: LiveData<Pokemon?> get() = _pokemonResult

    // LiveData para gerenciar erros (ex: Pokémon não encontrado)
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun clearSearchResult(){
        _pokemonResult.value = null
    }
    fun searchPokemon(name: String) {
        viewModelScope.launch {
            try {
                val pokemon = repository.getPokemonByName(name)
                _pokemonResult.value = pokemon
            } catch (e: Exception) {
                _errorMessage.value = "Pokémon não encontrado ou erro de rede!"
            }
        }
    }
}