package com.example.pokeguide // Mantenha a primeira linha com o nome do SEU pacote!

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
// Importe a classe gerada pelo ViewBinding (verifique se o pacote bate com o seu)
import com.example.pokeguide.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa o layout usando o ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toda a navegação e troca de telas será gerenciada automaticamente
        // pelo NavHostFragment que configuramos no seu activity_main.xml!
    }
}