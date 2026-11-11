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

        // Ação do Botão Retornar (Garante o retorno via pilha ou fallback de navegação)
        binding.tvBack.setOnClickListener {
            if (!findNavController().popBackStack()) {
                findNavController().navigateUp()
            }
        }

        // Resgata o nome do Pokémon
        val pokemonName = arguments?.getString("pokemonName") ?: ""
        if (pokemonName.isNotEmpty()) {
            viewModel.loadPokemonDetails(pokemonName)
        }

        // Observa os detalhes do Pokémon
        viewModel.pokemonDetail.observe(viewLifecycleOwner) { pokemon ->
            if (pokemon != null) {
                binding.tvPokemonNameDetail.text = pokemon.name.replaceFirstChar { it.uppercase() }
                binding.ivPokemonDetail.load(pokemon.imageUrl) { crossfade(true) }

                // Renderiza as tags dos Tipos (Fogo, Voador, etc.)
                renderTypes(pokemon.types)

                // Preenche os 6 Status
                pokemon.stats.forEach { statResponse ->
                    val value = statResponse.base_stat
                    when (statResponse.stat.name) {
                        "hp" -> {
                            binding.tvHpValue.text = value.toString()
                            updateStatBar(binding.viewHpBar, value)
                        }
                        "attack" -> {
                            binding.tvAttackValue.text = value.toString()
                            updateStatBar(binding.viewAttackBar, value)
                        }
                        "defense" -> {
                            binding.tvDefenseValue.text = value.toString()
                            updateStatBar(binding.viewDefenseBar, value)
                        }
                        "special-attack" -> {
                            binding.tvSpAtkValue.text = value.toString()
                            updateStatBar(binding.viewSpAtkBar, value)
                        }
                        "special-defense" -> {
                            binding.tvSpDefValue.text = value.toString()
                            updateStatBar(binding.viewSpDefBar, value)
                        }
                        "speed" -> {
                            binding.tvSpeedValue.text = value.toString()
                            updateStatBar(binding.viewSpeedBar, value)
                        }
                    }
                }

                // Gerenciamento de Favoritos
                var isFavorite = favoritesManager.isFavorite(pokemon.name)
                updateFavoriteIcon(isFavorite)

                binding.ivFavorite.setOnClickListener {
                    if (isFavorite) {
                        favoritesManager.removeFavorite(pokemon.name)
                        isFavorite = false
                        Toast.makeText(requireContext(), "${pokemon.name.uppercase()} removido dos favoritos", Toast.LENGTH_SHORT).show()
                    } else {
                        val pokemonResult = com.example.pokeguide.data.model.PokemonResult(
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

    private fun renderTypes(typeSlots: List<com.example.pokeguide.data.model.PokemonTypeSlot>) {
        binding.containerTypes.removeAllViews()

        typeSlots.forEach { slot ->
            val typeName = slot.type.name
            val textView = android.widget.TextView(requireContext()).apply {
                text = translateType(typeName)
                setTextColor(android.graphics.Color.WHITE)
                textSize = 12f
                setTypeface(null, android.graphics.Typeface.BOLD)
                setPadding(24, 8, 24, 8)
                background = createTypeTagBackground(getTypeColor(typeName))

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(8, 0, 8, 0)
                }
                layoutParams = params
            }
            binding.containerTypes.addView(textView)
        }
    }

    private fun translateType(type: String): String = when (type.lowercase()) {
        "fire" -> "Fogo"
        "water" -> "Água"
        "grass" -> "Planta"
        "electric" -> "Elétrico"
        "flying" -> "Voador"
        "poison" -> "Venenoso"
        "ground" -> "Terrestre"
        "rock" -> "Pedra"
        "bug" -> "Inseto"
        "ghost" -> "Fantasma"
        "steel" -> "Aço"
        "dragon" -> "Dragão"
        "dark" -> "Sombrio"
        "fairy" -> "Fada"
        "psychic" -> "Psíquico"
        "ice" -> "Gelo"
        "fighting" -> "Lutador"
        else -> type.replaceFirstChar { it.uppercase() }
    }

    private fun getTypeColor(type: String): Int = when (type.lowercase()) {
        "fire" -> android.graphics.Color.parseColor("#F08030")
        "water" -> android.graphics.Color.parseColor("#6890F0")
        "grass" -> android.graphics.Color.parseColor("#78C850")
        "electric" -> android.graphics.Color.parseColor("#F8D030")
        "flying" -> android.graphics.Color.parseColor("#A890F0")
        "poison" -> android.graphics.Color.parseColor("#A040A0")
        "ground" -> android.graphics.Color.parseColor("#E0C068")
        "rock" -> android.graphics.Color.parseColor("#B8A038")
        "bug" -> android.graphics.Color.parseColor("#A8B820")
        "ghost" -> android.graphics.Color.parseColor("#705898")
        "steel" -> android.graphics.Color.parseColor("#B8B8D0")
        "dragon" -> android.graphics.Color.parseColor("#7038F8")
        "dark" -> android.graphics.Color.parseColor("#705848")
        "fairy" -> android.graphics.Color.parseColor("#EE99AC")
        "psychic" -> android.graphics.Color.parseColor("#F85888")
        "ice" -> android.graphics.Color.parseColor("#98D8D8")
        "fighting" -> android.graphics.Color.parseColor("#C03028")
        else -> android.graphics.Color.GRAY
    }

    private fun createTypeTagBackground(color: Int): android.graphics.drawable.GradientDrawable {
        return android.graphics.drawable.GradientDrawable().apply {
            shape = android.graphics.drawable.GradientDrawable.RECTANGLE
            cornerRadius = 12f
            setColor(color)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}