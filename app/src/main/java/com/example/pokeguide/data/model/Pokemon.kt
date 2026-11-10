package com.example.pokeguide.data.model

data class Pokemon(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val stats: List<StatResponse>,
    val types: List<TypeResponse>
) {
    val imageUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
}

data class StatResponse(
    val base_stat: Int,
    val stat: StatDetail
)

data class StatDetail(
    val name: String // Ex: "hp", "attack", "defense", "special-attack", "special-defense", "speed"
)

data class TypeResponse(
    val slot: Int,
    val type: TypeDetail
)

data class TypeDetail(
    val name: String // Ex: "fire", "flying", "grass"
)