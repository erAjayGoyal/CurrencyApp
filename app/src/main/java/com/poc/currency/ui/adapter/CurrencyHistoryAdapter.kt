package com.poc.currency.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.poc.currency.databinding.RatesHisotryItemBinding
import com.poc.currency.ui.model.RateItem

/**
 * Recycler view adapter class used to show currency history conversation rates
 * Last 3 days history conversation rate will be shown
 * @param rateItemList -> history rate list
 */
class CurrencyHistoryAdapter(
    private val rateItemList: List<RateItem>
) : RecyclerView.Adapter<CurrencyHistoryViewHolder>() {

    private lateinit var binding: RatesHisotryItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyHistoryViewHolder {
        binding =
            RatesHisotryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyHistoryViewHolder, position: Int) {
        val rate = rateItemList[position]
        holder.bind(rate)
    }

    override fun getItemCount(): Int = rateItemList.size

}
