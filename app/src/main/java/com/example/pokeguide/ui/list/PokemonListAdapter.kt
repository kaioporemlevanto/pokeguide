package com.example.pokeguide.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pokeguide.data.model.PokemonResult
import com.example.pokeguide.databinding.ItemPokemonGridBinding

class PokemonListAdapter(
    private val onClick: (PokemonResult) -> Unit
) : ListAdapter<PokemonResult, PokemonListAdapter.PokemonViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = ItemPokemonGridBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PokemonViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PokemonViewHolder(
        private val binding: ItemPokemonGridBinding,
        private val onClick: (PokemonResult) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(pokemon: PokemonResult) {
            binding.tvPokemonNameGrid.text = pokemon.name.replaceFirstChar { it.uppercase() }

            // Carrega a imagem oficial do Pokémon utilizando o Coil
            binding.ivPokemonGrid.load(pokemon.imageUrl) {
                crossfade(true)
            }

            // Clique no card do Pokémon para abrir os detalhes
            binding.root.setOnClickListener {
                onClick(pokemon)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<PokemonResult>() {
        override fun areItemsTheSame(oldItem: PokemonResult, newItem: PokemonResult): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: PokemonResult, newItem: PokemonResult): Boolean {
            return oldItem == newItem
        }
    }
}