package com.serapercel.trickle.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.serapercel.trickle.R
import com.serapercel.trickle.databinding.FragmentSignUpBinding
import com.serapercel.trickle.presentation.ui.fragment.HomeFragment.Companion.item
import com.serapercel.trickle.util.toastLong
import com.serapercel.trickle.util.toastShort

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var passwordAgain: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        item = "sign up"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        initClickListener()
    }

    private fun initClickListener() {
        binding.btnSignUpSU.setOnClickListener {
            email = binding.etMailSU.text.toString()
            password = binding.etPasswordSU.text.toString()
            passwordAgain = binding.etPassword2.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && passwordAgain.isNotEmpty()) {
                if (password.length>=6) {
                    if (password == passwordAgain) {
                        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                            requireContext().toastShort(getString(R.string.sign_up_success))
                            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
                        }.addOnFailureListener { exception ->
                            exception.localizedMessage?.let { it -> requireContext().toastLong(it) }
                        }
                    } else requireContext().toastShort(getString(R.string.passwords_not_match))
                }else requireContext().toastShort(getString(R.string.passwords_too_short))
            } else requireContext().toastShort(getString(R.string.empty_fields))
        }

        binding.tvAlreadyRegister.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}