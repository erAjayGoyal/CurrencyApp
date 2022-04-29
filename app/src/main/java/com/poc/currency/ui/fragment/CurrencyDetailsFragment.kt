package com.poc.currency.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.faris.data.util.NetworkConstants
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.poc.currency.R
import com.poc.currency.databinding.CurrencyDetailsFragmentBinding
import com.poc.currency.domain.response.currency.CurrencyEntity
import com.poc.currency.ui.adapter.CurrencyHistoryAdapter
import com.poc.currency.ui.adapter.OtherCountryCurrencyAdapter
import com.poc.currency.ui.model.RateItem
import com.poc.currency.ui.vm.CurrencyDetailsViewModel
import com.poc.currency.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class CurrencyDetailsFragment : BaseFragment() {

    private var _binding: CurrencyDetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CurrencyDetailsViewModel by viewModels()
    private val args: CurrencyDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Utils.isConnectedToNetwork(requireContext())) {
            viewModel.getCurrencyHistoryData(args.fromCurrency, args.toCurrency)
        } else {
            showErrorMessage(NetworkConstants.NetworkErrorMessages.NO_INTERNET)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CurrencyDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.executePendingBindings()
        binding.currencyHistoryChart.setNoDataText("Chart is loading..., Please wait")
        binding.currencyHistoryChart.setNoDataTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.purple_200
            )
        )
        observeCurrencyHistoryData()

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
    }

    private fun observeCurrencyHistoryData() {
        viewModel.historyRateList.observe(viewLifecycleOwner) { rateList ->
            initHistoryRatesRecyclerView(rateList)
        }
        viewModel.countryCurrencyRateItems.observe(viewLifecycleOwner) { currencyList ->
            initOtherCountryRatesRecyclerView(currencyList)
        }

        viewModel.chartHistoryRatesList.observe(viewLifecycleOwner) { historyRateList ->
            initChartView(historyRateList)
        }
    }

    /**
     * Method used for init last 3 days currency conversation history recycler view
     * @param rateList currency conversation history list
     */
    private fun initHistoryRatesRecyclerView(rateList: List<RateItem>) {
        binding.rvCurrencyHistoryRates.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    (layoutManager as LinearLayoutManager).orientation
                )
            )
            adapter = CurrencyHistoryAdapter(rateList)
            setHasFixedSize(true)
        }
    }

    /**
     * Method use for init favourite 10 countries currency conversation recycler view
     * @param currencyList country conversation list
     */
    private fun initOtherCountryRatesRecyclerView(currencyList: List<CurrencyEntity.Currency>) {
        binding.rvOtherCountryRates.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    (layoutManager as LinearLayoutManager).orientation
                )
            )
            adapter = OtherCountryCurrencyAdapter(currencyList)
            setHasFixedSize(true)
        }
    }

    /**
     * Method used for init line chart view for showing history currency data for last 3 days
     * @param ratesResponseList currency conversation history list
     */
    private fun initChartView(ratesResponseList: ArrayList<RateItem>) {
        lifecycleScope.launch(Dispatchers.Default) {
            val entries = mutableListOf<Entry>()

            for (ratesResponse in ratesResponseList) {
                entries.add(
                    Entry(
                        entries.count().toFloat(),
                        ratesResponse.rate?.toFloat() ?: 0f,
                        args.toCurrency
                    )
                )
            }
            val dataSet = LineDataSet(entries, args.toCurrency)
            dataSet.color = ContextCompat.getColor(requireContext(), R.color.teal_700)
            val lineData = LineData(dataSet)

            withContext(Dispatchers.Main) {
                binding.currencyHistoryChart.data = lineData
                binding.currencyHistoryChart.invalidate()
            }
        }
    }

}