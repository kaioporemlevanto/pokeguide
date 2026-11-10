package com.example.pokeguide.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(pokemon: PokemonEntity)

    @Delete
    suspend fun deleteFavorite(pokemon: PokemonEntity)

    @Query("SELECT * FROM favorite_pokemons")
    fun getAllFavorites(): LiveData<List<PokemonEntity>>

    @Query("SELECT EXISTS(SELECT * FROM favorite_pokemons WHERE id = :pokemonId)")
    suspend fun isFavorite(pokemonId: Int): Boolean
}