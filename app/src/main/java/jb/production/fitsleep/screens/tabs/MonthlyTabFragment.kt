package jb.production.fitsleep.screens.tabs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import dagger.hilt.android.AndroidEntryPoint
import jb.production.fitsleep.R
import jb.production.fitsleep.databinding.MonthlyTabFragmentBinding
import jb.production.fitsleep.screens.BaseFragment
import jb.production.fitsleep.viewmodels.MonthlyTabViewModel

@AndroidEntryPoint
class MonthlyTabFragment : BaseFragment<MonthlyTabFragmentBinding>() {

    private val viewModel by viewModels<MonthlyTabViewModel>()

    override fun getLayout(): Int = R.layout.monthly_tab_fragment
    override fun getViewModel(): ViewModel = viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}