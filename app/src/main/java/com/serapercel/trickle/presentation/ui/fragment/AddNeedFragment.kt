package com.serapercel.trickle.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.serapercel.trickle.data.entity.Need
import com.serapercel.trickle.data.entity.User
import com.serapercel.trickle.databinding.FragmentAddNeedBinding
import com.serapercel.trickle.presentation.ui.viewModel.AddNeedViewModel
import com.serapercel.trickle.util.NetworkResult
import com.serapercel.trickle.util.toastShort
import javax.inject.Inject

class AddNeedFragment @Inject constructor(
    var user: User
): Fragment() {

    private var _binding: FragmentAddNeedBinding? = null
    private val binding get() = _binding!!

    private lateinit var addNeedViewModel: AddNeedViewModel
    private lateinit var count: String
    private lateinit var name: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNeedBinding.inflate(inflater, container, false)

        addNeedViewModel = ViewModelProvider(requireActivity()).get(AddNeedViewModel::class.java)

        binding.btnSaveNeed.setOnClickListener {
            name = binding.etName.text.toString()
            count = binding.etCount.text.toString()
            if (name.isEmpty() || count.isEmpty()) {
                requireActivity().toastShort("Please fill the fields!")
            } else {
                val need = Need(count, name)
                requestFirebaseData(need)
            }
        }

        return binding.root
    }

    private fun requestFirebaseData(need: Need) {
        addNeedViewModel.addNeed(need, user)
        addNeedViewModel.needsResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        requireContext().toastShort("Need Added")
                    }
                }
                is NetworkResult.Error -> {
                    requireContext().toastShort(response.message.toString())
                }
                is NetworkResult.Loading -> {
                    requireContext().toastShort("Adding")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}