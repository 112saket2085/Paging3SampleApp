package com.example.paging3sampleapp.view.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.paging3sampleapp.util.removeFullscreen
import com.example.paging3sampleapp.util.setFullscreen
import com.example.paging3sampleapp.view.MainActivity

open class BaseFragment : Fragment {

    constructor(layoutResId: Int) : super(layoutResId)
    constructor() : super()

    protected val mainActivity by lazy {
        requireActivity() as MainActivity
    }

    protected val fragmentContext by lazy {
        requireContext()
    }

    private val navController by lazy {
        findNavController()
    }

    protected open fun isToolbarNeeded() = true

    protected open fun getTitle() = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.supportActionBar?.let {
            if (isToolbarNeeded()) it.show() else it.hide()
        }
        setActionBarTitle(getTitle())
    }

    protected fun setActionBarTitle(title: String) {
        mainActivity.supportActionBar?.let {
            it.title = title
        }
    }

    /**
     * @param actionId id used to navigate between fragment
     * @param navDirections navigate and exchange data between fragment using NavDirections
     */
    protected fun navigateTo(actionId: Int? = null, navDirections: NavDirections? = null) {
        actionId?.let {
            navController.navigate(it)
        }
        navDirections?.let {
            navController.navigate(it)
        }
    }

    /**
     * Method to clear current fragment from backstack
     */
    protected fun navigateUp() {
        navController.navigateUp()
    }
}