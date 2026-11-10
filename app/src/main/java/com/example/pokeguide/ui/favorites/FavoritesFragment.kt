package com.example.pokeguide.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokeguide.R
import com.example.pokeguide.data.local.FavoritesManager
import com.example.pokeguide.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoritesManager: FavoritesManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navegação pelos ícones da barra superior
        binding.btnNavSearch.setOnClickListener {
            findNavController().navigate(R.id.action_favoritesFragment_to_searchFragment)
        }
        binding.btnNavList.setOnClickListener {
            findNavController().navigate(R.id.action_favoritesFragment_to_pokemonListFragment)
        }

        favoritesManager = FavoritesManager(requireContext())
        loadFavorites()
    }

    private fun loadFavorites() {
        val favorites = favoritesManager.getFavorites()

        val adapter = FavoritesAdapter(
            favoritesList = favorites,
            onItemClick = { pokemon ->
                val bundle = Bundle().apply {
                    putString("pokemonName", pokemon.name)
                }
                findNavController().navigate(R.id.action_favoritesFragment_to_detailFragment, bundle)
            },
            onDeleteClick = { pokemon ->
                favoritesManager.removeFavorite(pokemon.name)
                loadFavorites() // Atualiza a lista após remover
            }
        )

        binding.rvFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavorites.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}