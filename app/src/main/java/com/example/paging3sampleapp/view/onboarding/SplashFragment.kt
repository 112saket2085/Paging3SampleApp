package com.example.paging3sampleapp.view.onboarding

import com.example.paging3sampleapp.R
import com.example.paging3sampleapp.view.base.BaseFragment

class SplashFragment : BaseFragment() {

    override fun isToolbarNeeded() = false

    override fun onResume() {
        super.onResume()
        navigateTo(R.id.action_splashFragment_to_moviesListFragment)
    }
}