package com.example.pokeguide.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pokeguide.data.model.PokemonResult
import com.example.pokeguide.databinding.ItemFavoriteListBinding

class FavoritesAdapter(
    private val favoritesList: List<PokemonResult>,
    private val onItemClick: (PokemonResult) -> Unit,
    private val onDeleteClick: (PokemonResult) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val binding = ItemFavoriteListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FavViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val pokemon = favoritesList[position]
        holder.bind(pokemon, onItemClick, onDeleteClick)
    }

    override fun getItemCount(): Int = favoritesList.size

    class FavViewHolder(private val binding: ItemFavoriteListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            pokemon: PokemonResult,
            onItemClick: (PokemonResult) -> Unit,
            onDeleteClick: (PokemonResult) -> Unit
        ) {
            binding.tvFavItemName.text = pokemon.name.replaceFirstChar { it.uppercase() }
            binding.ivFavItem.load(pokemon.imageUrl) {
                crossfade(true)
            }

            // Clica no item para ver detalhes
            binding.root.setOnClickListener { onItemClick(pokemon) }

            // Clica no ícone para remover dos favoritos
            binding.ivFavDelete.setOnClickListener { onDeleteClick(pokemon) }
        }
    }
}