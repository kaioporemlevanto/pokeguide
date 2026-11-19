package com.example.pokeguide // Mantenha a primeira linha com o nome do SEU pacote!

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
// Importe a classe gerada pelo ViewBinding (verifique se o pacote bate com o seu)
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.pokeguide.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Conecta o BottomNavigationView ao NavController via NavUI
        binding.bottomNavigation.setupWithNavController(navController)

        // Controla em quais telas o menu inferior aparece
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.searchFragment,
                R.id.pokemonListFragment,
                R.id.favoritesFragment -> binding.bottomNavigation.visibility = View.VISIBLE
                else -> binding.bottomNavigation.visibility = View.GONE
            }
        }
    }
}