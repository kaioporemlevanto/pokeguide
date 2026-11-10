package com.example.pokeguide.ui.search // Ajuste para o seu pacote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.pokeguide.R
import com.example.pokeguide.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    // Inicializa o ViewModel usando a delegação do KTX
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navegação pelos ícones da barra superior
        binding.btnNavList.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_pokemonListFragment)
        }
        binding.btnNavFavorites.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_favoritesFragment)
        }

        // Ação do botão de busca
        binding.btnSearch.setOnClickListener {
            val pokemonName = binding.etPokemonName.text.toString().trim().lowercase()

            if (pokemonName.isNotEmpty()) {
                viewModel.searchPokemon(pokemonName)
            } else {
                Toast.makeText(requireContext(), getString(R.string.erro_campo_vazio), Toast.LENGTH_SHORT).show()
            }
        }

        // Observa se o Pokémon foi encontrado com sucesso
        viewModel.pokemonResult.observe(viewLifecycleOwner) { pokemon ->
            if (pokemon != null) {
                val bundle = Bundle().apply {
                    putString("pokemonName", pokemon.name)
                }
                findNavController().navigate(R.id.action_searchFragment_to_detailFragment, bundle)
            }
        }

        // Observa erros (caso o usuário digite um nome inválido)
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (!error.isNullOrEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}