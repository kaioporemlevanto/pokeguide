package com.example.pokeguide.ui.detail // Ajuste para o seu pacote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeguide.data.model.Pokemon
import com.example.pokeguide.data.repository.PokemonRepository
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    private val repository = PokemonRepository()

    private val _pokemonDetail = MutableLiveData<Pokemon?>()
    val pokemonDetail: LiveData<Pokemon?> get() = _pokemonDetail

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun loadPokemonDetails(name: String) {
        viewModelScope.launch {
            try {
                val pokemon = repository.getPokemonByName(name)
                _pokemonDetail.value = pokemon
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao carregar detalhes do Pokémon."
            }
        }
    }
}