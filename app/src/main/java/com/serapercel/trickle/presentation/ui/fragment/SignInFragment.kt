package com.serapercel.trickle.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.serapercel.trickle.R
import com.serapercel.trickle.databinding.FragmentSignInBinding
import com.serapercel.trickle.util.toastLong
import com.serapercel.trickle.util.toastShort

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference


    lateinit var email: String
    private lateinit var password: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        dbRef = database.getReference("users")
        initClickListener()
    }

    private fun initClickListener() {
        binding.btnSignIn.setOnClickListener {
            email = binding.etMail.text.toString()
            password = binding.etPassword.text.toString()
            newUser(email, password)
        }

        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        binding.tvForgotPassword.setOnClickListener {
            email = binding.etMail.text.toString()

            if (email.isNotEmpty()) {
                auth.sendPasswordResetEmail(email).addOnSuccessListener {
                    requireContext().toastShort(getString(R.string.send_reset_link))
                }.addOnFailureListener { exception ->
                    exception.localizedMessage?.let { it -> requireContext().toastLong(it) }
                }
            } else requireContext().toastShort(getString(R.string.enter_email))
        }
    }

    private fun newUser(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                val action = SignInFragmentDirections.actionSignInFragmentToAccountFragment(email)
                findNavController().navigate(action)
            }.addOnFailureListener { exception ->
                exception.localizedMessage?.let { it -> requireContext().toastLong(it) }
            }
        } else requireContext().toastShort(getString(R.string.empty_fields))

    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            val action =
                SignInFragmentDirections.actionSignInFragmentToAccountFragment(auth.currentUser!!.email)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}