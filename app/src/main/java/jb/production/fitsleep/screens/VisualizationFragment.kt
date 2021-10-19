package jb.production.fitsleep.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import jb.production.fitsleep.R
import jb.production.fitsleep.databinding.FragmentVisualizationBinding
import jb.production.fitsleep.screens.adapters.VisualizationTabAdapter
import jb.production.fitsleep.screens.tabs.DailyTabFragment
import jb.production.fitsleep.screens.tabs.MonthlyTabFragment
import jb.production.fitsleep.screens.tabs.WeeklyTabFragment
import jb.production.fitsleep.viewmodels.VisualizationViewModel
import javax.inject.Inject

@AndroidEntryPoint
class VisualizationFragment : BaseFragment<FragmentVisualizationBinding>() {

    @Inject
    lateinit var tabAdapter: VisualizationTabAdapter

    private val viewModel by viewModels<VisualizationViewModel>()

    override fun getLayout(): Int = R.layout.fragment_visualization
    override fun getViewModel(): ViewModel = viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewpager.apply {
            isUserInputEnabled = false
            tabAdapter.setFragments(
                listOf(
                    DailyTabFragment(),
                    WeeklyTabFragment(),
                    MonthlyTabFragment()
                )
            )
            adapter = tabAdapter

            TabLayoutMediator(
                binding.tabLayout,
                this,
                TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                    val text = when (position) {
                        0 -> "Day"
                        1 -> "Week"
                        2 -> "Month"
                        else -> ""
                    }
                    tab.text = text
                }).attach()
        }
    }
}