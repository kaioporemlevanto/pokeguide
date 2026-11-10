package com.example.pokeguide.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokeguide.R
import com.example.pokeguide.databinding.FragmentPokemonListBinding

class PokemonListFragment : Fragment() {

    private var _binding: FragmentPokemonListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PokemonListViewModel by viewModels()
    private lateinit var pokemonAdapter: PokemonListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navegação pelos ícones da barra superior
        binding.btnNavSearch.setOnClickListener {
            findNavController().navigate(R.id.action_pokemonListFragment_to_searchFragment)
        }
        binding.btnNavFavorites.setOnClickListener {
            findNavController().navigate(R.id.action_pokemonListFragment_to_favoritesFragment)
        }

        setupRecyclerView()

        viewModel.pokemonList.observe(viewLifecycleOwner) { list ->
            pokemonAdapter.submitList(list.toMutableList())
        }
    }

    private fun setupRecyclerView() {
        pokemonAdapter = PokemonListAdapter { pokemon ->
            // Ao clicar em um pokémon do grid, navega para a tela de detalhes
            val bundle = Bundle().apply {
                putString("pokemonName", pokemon.name)
            }
            findNavController().navigate(R.id.action_pokemonListFragment_to_detailFragment, bundle)
        }

        // Configura um GridLayoutManager com 3 colunas (conforme o protótipo)
        val gridLayoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvPokemonList.layoutManager = gridLayoutManager
        binding.rvPokemonList.adapter = pokemonAdapter

        // Detecta quando o usuário chega próximo ao fim da lista para carregar mais (Paginação)
        binding.rvPokemonList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = gridLayoutManager.childCount
                val totalItemCount = gridLayoutManager.itemCount
                val firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition()

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                ) {
                    viewModel.loadMorePokemons()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}