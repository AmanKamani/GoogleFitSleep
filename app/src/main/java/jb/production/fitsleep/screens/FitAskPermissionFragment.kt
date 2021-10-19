package jb.production.fitsleep.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import jb.production.fitsleep.R
import jb.production.fitsleep.databinding.FragmentFitAskPermissionBinding
import jb.production.fitsleep.viewmodels.FitAskPermissionViewModel

@AndroidEntryPoint
class FitAskPermissionFragment : BaseFragment<FragmentFitAskPermissionBinding>() {

    private val viewModel by viewModels<FitAskPermissionViewModel>()
    override fun getLayout(): Int = R.layout.fragment_fit_ask_permission
    override fun getViewModel(): ViewModel = viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        viewModel.reconnect()
    }

    private fun subscribeObservers() {
        viewModel.reconnectClick.observe(viewLifecycleOwner) {
            connectWithFit()
        }
    }

    private fun connectWithFit() {
        findNavController().navigate(
            FitAskPermissionFragmentDirections.actionFitAskPermissionFragmentToHomeFragment()
        )
    }

}