package com.serapercel.trickle.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import com.serapercel.trickle.R
import com.serapercel.trickle.databinding.FragmentCalculatorBinding

class CalculatorFragment : Fragment() {
    private lateinit var binding: FragmentCalculatorBinding

    private var canAddOperation = false
    private var canAddDecimal = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calculator,container, false)

        return binding.root
    }

    fun numberAction(view: View) {
        if (view is Button) {
            if (view.text == ".") {
                if (canAddDecimal)
                    binding.tvWorkings.append(view.text)
                canAddOperation = false

            } else
                binding.tvWorkings.append(view.text)
            canAddOperation = true
        }
    }

    fun operationAction(view: View) {
        if (view is Button && canAddOperation) {
            binding.tvWorkings.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }


    fun backSpaceAction(view: View) {
        val length = binding.tvWorkings.length()
        if (length > 0)
            binding.tvWorkings.text = binding.tvWorkings.text.subSequence(0, length - 1)
    }

    fun equalsAction(view: View) {

    }

    fun allClearAction(view: View) {}

}