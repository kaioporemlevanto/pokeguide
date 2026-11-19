package com.example.pokeguide.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokeguide.R
import com.example.pokeguide.data.model.PokemonResult
import com.example.pokeguide.data.repository.FavoritesRepository
import com.example.pokeguide.databinding.FragmentFavoritesBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val favoritesRepository = FavoritesRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Botão Sair (Logout no FirebaseAuth)
        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_favoritesFragment_to_loginFragment)
        }

        setupFavoritesObserver()
    }

    private fun setupFavoritesObserver() {
        favoritesRepository.observeFavorites { favorites ->
            if (_binding == null) return@observeFavorites

            val adapter = FavoritesAdapter(
                favoritesList = favorites,
                onItemClick = { pokemon ->
                    val bundle = Bundle().apply {
                        putString("pokemonName", pokemon.name)
                    }
                    findNavController().navigate(R.id.action_favoritesFragment_to_detailFragment, bundle)
                },
                onDeleteClick = { pokemon ->
                    showRemoveConfirmationDialog(pokemon)
                }
            )

            binding.rvFavorites.layoutManager = LinearLayoutManager(requireContext())
            binding.rvFavorites.adapter = adapter
        }
    }

    private fun showRemoveConfirmationDialog(pokemon: PokemonResult) {
        val formattedName = pokemon.name.replaceFirstChar { it.uppercase() }

        AlertDialog.Builder(requireContext())
            .setTitle("Remover favorito")
            .setMessage("Deseja realmente remover $formattedName dos seus favoritos?")
            .setPositiveButton("Remover") { _, _ ->
                favoritesRepository.removeFavorite(pokemon.name) { success ->
                    if (success && _binding != null) {
                        Snackbar.make(binding.root, "$formattedName removido", Snackbar.LENGTH_LONG)
                            .setAction("Desfazer") {
                                favoritesRepository.saveFavorite(pokemon)
                            }
                            .show()
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}