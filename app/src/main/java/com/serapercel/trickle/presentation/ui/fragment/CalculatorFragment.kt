package com.serapercel.trickle.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.serapercel.trickle.databinding.FragmentCalculatorBinding
import com.serapercel.trickle.util.toastShort

class CalculatorFragment : Fragment() {
    private var _binding: FragmentCalculatorBinding? = null
    private val binding get() = _binding!!

    private var canAddOperation = false
    private var canAddDecimal = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalculatorBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAC.setOnClickListener { allClearAction() }
        binding.btnD.setOnClickListener { backSpaceAction() }
        binding.btnZero.setOnClickListener { numberAction(it) }
        binding.btnOne.setOnClickListener { numberAction(it) }
        binding.btnTwo.setOnClickListener { numberAction(it) }
        binding.btnThree.setOnClickListener { numberAction(it) }
        binding.btnFour.setOnClickListener { numberAction(it) }
        binding.btnFive.setOnClickListener { numberAction(it) }
        binding.btnSix.setOnClickListener { numberAction(it) }
        binding.btnSeven.setOnClickListener { numberAction(it) }
        binding.btnEight.setOnClickListener { numberAction(it) }
        binding.btnNine.setOnClickListener { numberAction(it) }
        binding.btnSum.setOnClickListener { operationAction(it) }
        binding.btnMinus.setOnClickListener { operationAction(it) }
        binding.btnMultiple.setOnClickListener { operationAction(it) }
        binding.btnDivide.setOnClickListener { operationAction(it) }
        binding.btnDecimal.setOnClickListener { numberAction(it) }
        binding.btnEquals.setOnClickListener { equalsAction() }
    }

    private fun numberAction(view: View) {
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

    private fun operationAction(view: View) {
        if (view is Button && canAddOperation) {
            binding.tvWorkings.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }

    private fun backSpaceAction() {
        val length = binding.tvWorkings.length()
        if (length > 0)
            binding.tvWorkings.text = binding.tvWorkings.text.subSequence(0, length - 1)
    }

    private fun equalsAction() {
        try {
            binding.tvResults.text = calculateResults()
        } catch (e: Exception) {
            requireContext().toastShort("Invalid operation!")
        }
    }

    private fun allClearAction() {
        binding.tvWorkings.text = ""
        binding.tvResults.text = ""
    }

    private fun calculateResults(): String {
        val digitsOperators = digitOperators()
        if (digitsOperators.isEmpty()) return ""

        val timesDivision = timeDivisionCalculate(digitsOperators)
        if (timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)
        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float
        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex) {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit
            }
        }
        return result
    }

    private fun timeDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('x') || list.contains('/')) {
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex) {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                try {
                    when (operator) {
                        'x' -> {
                            newList.add(prevDigit * nextDigit)
                            restartIndex = i + 1
                        }
                        '/' -> {
                            newList.add(prevDigit / nextDigit)
                            restartIndex = i + 1

                        }
                        else -> {
                            newList.add(prevDigit)
                            newList.add(operator)
                            newList.add(nextDigit)

                        }
                    }
                } catch (e: Exception) {
                    requireContext().toastShort("Invalid operation!")
                }

            }
            if (i > restartIndex)
                newList.add(passedList[i])
        }
        return newList
    }

    private fun digitOperators(): MutableList<Any> {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for (character in binding.tvWorkings.text) {
            if (character.isDigit() || character == '.') {
                currentDigit += character
            } else {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }
        if (currentDigit != "")
            list.add(currentDigit.toFloat())
        return list
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}