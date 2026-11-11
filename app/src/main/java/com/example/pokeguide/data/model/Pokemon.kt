package com.example.pokeguide.data.model

import com.google.gson.annotations.SerializedName

data class Pokemon(
    val id: Int,
    val name: String,
    val stats: List<StatResponse>,
    val types: List<PokemonTypeSlot>, // Adicionado para capturar os tipos
    val sprites: Sprites
) {
    val imageUrl: String
        get() = sprites.other?.officialArtwork?.front_default
            ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
}

data class PokemonTypeSlot(
    val slot: Int,
    val type: PokemonTypeInfo
)

data class PokemonTypeInfo(
    val name: String
)

data class StatResponse(
    val base_stat: Int,
    val stat: StatInfo
)

data class StatInfo(
    val name: String
)

data class Sprites(
    val other: OtherSprites?
)

data class OtherSprites(
    @SerializedName("official-artwork")
    val officialArtwork: OfficialArtwork?
)

data class OfficialArtwork(
    val front_default: String?
)