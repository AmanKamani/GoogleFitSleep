package jb.production.fitsleep.screens.tabs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import dagger.hilt.android.AndroidEntryPoint
import jb.production.fitsleep.R
import jb.production.fitsleep.databinding.DailyTabFragmentBinding
import jb.production.fitsleep.screens.BaseFragment
import jb.production.fitsleep.viewmodels.DailyTabViewModel

@AndroidEntryPoint
class DailyTabFragment : BaseFragment<DailyTabFragmentBinding>() {

    private val viewModel by viewModels<DailyTabViewModel>()

    override fun getLayout(): Int = R.layout.daily_tab_fragment
    override fun getViewModel(): ViewModel = viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}