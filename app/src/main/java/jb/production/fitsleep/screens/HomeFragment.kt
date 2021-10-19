package jb.production.fitsleep.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import jb.production.fitsleep.R
import jb.production.fitsleep.databinding.FragmentHomeBinding
import jb.production.fitsleep.viewmodels.HomeViewModel


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val viewModel by viewModels<HomeViewModel>()

    override fun getLayout(): Int = R.layout.fragment_home

    override fun getViewModel(): ViewModel = viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.goToVisualization.observe(viewLifecycleOwner) {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToVisualizationFragment())
        }

        viewModel.toVisualization()
    }

}