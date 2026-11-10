package com.example.pokeguide.data.model

data class PokemonResponse(
    val results: List<PokemonResult>
)

data class PokemonResult(
    val name: String,
    val url: String
) {
    // Extrai o número (ID) do final da string da URL: "https://pokeapi.co/api/v2/pokemon/25/" -> 25
    val id: Int
        get() = url.split("/".toRegex()).dropLast(1).last().toInt()

    // Monta a URL direta para a arte oficial do Pokémon
    val imageUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
}