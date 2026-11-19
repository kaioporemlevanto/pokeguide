package com.example.pokeguide.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pokeguide.databinding.FragmentSplashBinding
import androidx.navigation.fragment.findNavController
import com.example.pokeguide.R
import com.google.firebase.auth.FirebaseAuth


class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Delay de 2.5 segundos para exibir a splash e verificar a sessão
        Handler(Looper.getMainLooper()).postDelayed({
            // Garante que o fragment ainda está ativo caso o usuário feche o app durante os 2.5s
            if (_binding != null) {
                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser != null) {
                    findNavController().navigate(R.id.action_splashFragment_to_searchFragment)
                } else {
                    findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                }
            }
        }, 2500)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}