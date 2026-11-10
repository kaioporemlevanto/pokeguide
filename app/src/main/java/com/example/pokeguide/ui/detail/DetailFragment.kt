package com.example.pokeguide.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.pokeguide.data.local.FavoritesManager
import com.example.pokeguide.data.model.PokemonResult
import com.example.pokeguide.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailViewModel by viewModels()
    private lateinit var favoritesManager: FavoritesManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritesManager = FavoritesManager(requireContext())

        // Resgata o nome do Pokémon passado pela tela de busca ou lista
        val pokemonName = arguments?.getString("pokemonName") ?: ""

        if (pokemonName.isNotEmpty()) {
            viewModel.loadPokemonDetails(pokemonName)
        }

        // Botão Retornar com fallback de segurança
        binding.tvBack.setOnClickListener {
            if (!findNavController().popBackStack()) {
                findNavController().navigateUp()
            }
        }

        // Observa os dados da API
        viewModel.pokemonDetail.observe(viewLifecycleOwner) { pokemon ->
            if (pokemon != null) {
                // Nome e Imagem Oficial via Coil
                binding.tvPokemonNameDetail.text = pokemon.name.replaceFirstChar { it.uppercase() }
                binding.ivPokemonDetail.load(pokemon.imageUrl) {
                    crossfade(true)
                }

                // Preenche e dimensiona as barras de status de forma proporcional
                pokemon.stats.forEach { statResponse ->
                    val statValue = statResponse.base_stat
                    when (statResponse.stat.name) {
                        "hp" -> {
                            binding.tvHpValue.text = statValue.toString()
                            updateStatBar(binding.viewHpBar, statValue)
                        }
                        "attack" -> {
                            binding.tvAttackValue.text = statValue.toString()
                            updateStatBar(binding.viewAttackBar, statValue)
                        }
                        "defense" -> {
                            binding.tvDefenseValue.text = statValue.toString()
                            updateStatBar(binding.viewDefenseBar, statValue)
                        }
                        "speed" -> {
                            binding.tvSpeedValue.text = statValue.toString()
                            updateStatBar(binding.viewSpeedBar, statValue)
                        }
                    }
                }

                // Configura o estado do ícone de favorito
                var isFavorite = favoritesManager.isFavorite(pokemon.name)
                updateFavoriteIcon(isFavorite)

                // Clique no ícone para Favoritar / Desfavoritar
                binding.ivFavorite.setOnClickListener {
                    if (isFavorite) {
                        favoritesManager.removeFavorite(pokemon.name)
                        isFavorite = false
                        Toast.makeText(requireContext(), "${pokemon.name.uppercase()} removido dos favoritos", Toast.LENGTH_SHORT).show()
                    } else {
                        val pokemonResult = PokemonResult(
                            name = pokemon.name,
                            url = "https://pokeapi.co/api/v2/pokemon/${pokemon.id}/"
                        )
                        favoritesManager.saveFavorite(pokemonResult)
                        isFavorite = true
                        Toast.makeText(requireContext(), "${pokemon.name.uppercase()} adicionado aos favoritos!", Toast.LENGTH_SHORT).show()
                    }
                    updateFavoriteIcon(isFavorite)
                }
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (!error.isNullOrEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        if (isFavorite) {
            binding.ivFavorite.setImageResource(android.R.drawable.btn_star_big_on)
        } else {
            binding.ivFavorite.setImageResource(android.R.drawable.btn_star_big_off)
        }
    }

    // Função auxiliar para ajustar a largura da barra proporcionalmente ao valor do status
    private fun updateStatBar(view: View, statValue: Int) {
        val params = view.layoutParams as LinearLayout.LayoutParams
        // Define o weight diretamente como o valor do status vindo da PokéAPI (ex: 35 para HP, 90 para Velocidade)
        params.weight = statValue.toFloat().coerceIn(1f, 200f)
        view.layoutParams = params
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}