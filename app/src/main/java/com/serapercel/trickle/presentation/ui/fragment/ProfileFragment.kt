package com.serapercel.trickle.presentation.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.serapercel.trickle.databinding.FragmentProfileBinding
import com.serapercel.trickle.presentation.ui.activity.StarterActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding.btnSignOutUser.setOnClickListener {
            auth.signOut()
            logoutFromSharedPref()
            val intent = Intent(requireActivity(), StarterActivity::class.java)
            startActivity(intent)
        }
        binding.btnSignOutAccount.setOnClickListener {
            if (auth.currentUser != null) {
                logoutFromSharedPref()
                val action =
                    HomeFragmentDirections.actionHomeFragmentToAccountFragment(auth.currentUser!!.email)
                findNavController().navigate(action)
            }
        }
    }

    private fun logoutFromSharedPref() {
        try {
            val sharedPreference =
                requireContext().getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE)
            val editor = sharedPreference.edit()
            editor.remove("account")
            editor.apply()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}