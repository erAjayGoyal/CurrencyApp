package com.poc.currency.ui.fragment

import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.poc.currency.ui.activity.CurrencyActivity

/**
 * Abstract class can hold all the common work used in fragments
 * Showing/Hiding progress bar, Show error message
 * Common DI classes reference used through out all the fragments
 */
abstract class BaseFragment : Fragment() {

    /**
     * Method use to show/hide progress bar
     * Also handle window flag touch event when loading bar showing
     * @param isLoading -> flag to show/hide progress bar
     */
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