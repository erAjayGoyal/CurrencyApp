package com.poc.currency.ui.fragment

import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.poc.currency.ui.activity.CurrencyActivity

abstract class BaseFragment : Fragment() {

    fun setWindowFlag(isLoading: Boolean) {
        if (isLoading) {
            (requireActivity() as CurrencyActivity).showLoading()
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else {
            (requireActivity() as CurrencyActivity).hideLoading()
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    fun showErrorMessage(errorMsg: String?) {
        Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_LONG).show()
    }

}