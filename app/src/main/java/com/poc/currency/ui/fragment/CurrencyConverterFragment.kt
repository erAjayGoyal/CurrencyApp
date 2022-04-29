package com.poc.currency.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.poc.currency.data.util.NetworkConstants
import com.poc.currency.databinding.CurrencyConverterFragmnetBinding
import com.poc.currency.ui.vm.CurrencyConverterViewModel
import com.poc.currency.utils.Constants
import com.poc.currency.utils.Utils
import com.poc.currency.utils.extensions.onImeActionDone
import com.poc.currency.utils.extensions.onItemSelected
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencyConverterFragment : BaseFragment() {

    private val viewModel: CurrencyConverterViewModel by viewModels()

    private var _binding: CurrencyConverterFragmnetBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Utils.isConnectedToNetwork(requireContext())) {
            viewModel.getCurrencyList()
        } else {
            showErrorMessage(NetworkConstants.NetworkErrorMessages.NO_INTERNET)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CurrencyConverterFragmnetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        observeCurrencyData()

        binding.editTxtFrom.onImeActionDone(requireActivity()) { v ->
            v?.text?.toString()?.takeIf { it.isNotEmpty() }?.let {
                viewModel.convert(it)
            }
        }

        binding.editTxtTo.onImeActionDone(requireActivity()) { v ->
            v?.text?.toString()?.takeIf { it.isNotEmpty() }?.let {
                viewModel.convert(it, true)
            }
        }

        viewModel.errorEvent.observe(viewLifecycleOwner) {
            it?.let {
                showErrorMessage(it.errorMessage)
            }
        }

        viewModel.loadingEvent.observe(viewLifecycleOwner) {
            it?.let { loading ->
                setWindowFlag(loading)
            }
        }

        viewModel.switchCurrency.observe(viewLifecycleOwner) {
            val fromItemPosition: Int =
                viewModel.currencyList.value?.indexOf(viewModel.fromCurrency.value) ?: -1
            val toItemPosition: Int =
                viewModel.currencyList.value?.indexOf(viewModel.toCurrency.value) ?: -1

            if (fromItemPosition != -1) {
                binding.spinnerToCurrency.setSelection(fromItemPosition)
            }
            if (toItemPosition != -1) {
                binding.spinnerFromCurrency.setSelection(toItemPosition)
            }
        }

        viewModel.detailsScreenEvent.observe(viewLifecycleOwner) {
            findNavController().navigate(
                CurrencyConverterFragmentDirections.actionConverterFragmentToCurrencyHistoryFragment(
                    fromCurrency = viewModel.fromCurrency.value?.code ?: "",
                    toCurrency = viewModel.toCurrency.value?.code ?: ""
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Observer method used to observe currency symbols, currency conversation rate
     * Update from & to currency spinner once data is received
     */
    private fun observeCurrencyData() {
        viewModel.currencyList.observe(viewLifecycleOwner) {
            binding.spinnerFromCurrency.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                it.map { currency ->
                    currency.code
                }
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            binding.spinnerFromCurrency.onItemSelected { position ->
                viewModel.currencyList.value?.let { currencyList ->
                    val selectedCurrency = currencyList[position]
                    viewModel.setFromCurrencyValue(selectedCurrency)
                }
            }
            binding.spinnerToCurrency.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                it.map { currency ->
                    currency.code
                }
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            setDefaultCurrencyToFrom()
            binding.spinnerToCurrency.onItemSelected { position ->
                viewModel.currencyList.value?.let { currencyList ->
                    val selectedCurrency = currencyList[position]
                    viewModel.setToCurrencyValue(selectedCurrency)
                }
            }
        }

        viewModel.fromCurrency.observe(viewLifecycleOwner) {
            it?.let {
                viewModel.convert(binding.editTxtFrom.text.toString())
            }
        }
        viewModel.toCurrency.observe(viewLifecycleOwner) {
            it?.let {
                viewModel.convert(binding.editTxtFrom.text.toString())
            }
        }

        viewModel.fromAmount.observe(viewLifecycleOwner) {
            it?.let {
                binding.editTxtFrom.setText(it)
            }
        }
        viewModel.toAmount.observe(viewLifecycleOwner) {
            it?.let {
                binding.editTxtTo.setText(it)
            }
        }
    }

    /**
     * Method show default selected country in from currency spinner
     */
    private fun setDefaultCurrencyToFrom() {
        val defaultPosition =
            viewModel.currencyList.value?.indexOfFirst { currency -> currency.code == Constants.BASE_CURRENCY }
                .takeIf { index -> index != -1 }
        defaultPosition?.let {
            binding.spinnerFromCurrency.setSelection(it)
        }
    }
}