package com.serapercel.trickle.presentation.ui.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.serapercel.trickle.data.entity.*
import com.serapercel.trickle.databinding.FragmentAddTransactionBinding
import com.serapercel.trickle.presentation.ui.viewModel.AddTransactionViewModel
import com.serapercel.trickle.util.NetworkResult
import com.serapercel.trickle.util.toastLong
import com.serapercel.trickle.util.toastShort
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AddTransactionFragment @Inject constructor(
    var account: Account
) : Fragment() {

    private var _binding: FragmentAddTransactionBinding? = null
    private val binding get() = _binding!!

    private lateinit var addTransactionViewModel: AddTransactionViewModel

    private var formatDate = SimpleDateFormat("dd MMMM yyyy", Locale.US)
    private lateinit var title: String
    private lateinit var price: String
    private var income: Boolean = false
    private var date = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addTransactionViewModel =
            ViewModelProvider(requireActivity())[AddTransactionViewModel::class.java]

        binding.btnPickDate.setOnClickListener {
            val getDate = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                requireContext(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->

                    val selectDate = Calendar.getInstance()
                    selectDate.set(Calendar.YEAR, y)
                    selectDate.set(Calendar.MONTH, m)
                    selectDate.set(Calendar.DAY_OF_MONTH, d)

                    date = formatDate.format(selectDate.time)
                    binding.btnPickDate.text = date

                },
                getDate.get(Calendar.YEAR),
                getDate.get(Calendar.MONTH),
                getDate.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        binding.switchIncome.setOnCheckedChangeListener { _, isChecked ->
            income = isChecked
        }

        binding.btnSaveTransaction.setOnClickListener {
            title = binding.etTitle.text.toString()
            price = binding.etPrice.text.toString()
            if (title.isEmpty() || price.isEmpty()) {
                requireActivity().toastShort("Please fill the fields!")
            } else if (date.isEmpty()) {
                requireActivity().toastShort("Please pick date!")
            } else {
                requireActivity().toastLong("Saved: $title $price $date ")
                if (income) {
                    val income = Income(account, title, price, date)
                    requestFirebaseData(income)
                } else {
                    val expense = Expense(account, title, price, date)
                    requestFirebaseData(expense)
                }
            }

        }

    }

    private fun requestFirebaseData(transaction: ITransaction) {
        addTransactionViewModel.addTransaction(transaction, account.user)
        addTransactionViewModel.transactionResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        requireContext().toastShort("Transaction Added")
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