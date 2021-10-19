package jb.production.fitsleep.screens

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import jb.production.fitsleep.R
import jb.production.fitsleep.databinding.FragmentFitAskPermissionBinding
import jb.production.fitsleep.utils.GoogleFitUtils
import jb.production.fitsleep.viewmodels.FitAskPermissionViewModel
import javax.inject.Inject

@AndroidEntryPoint
class FitAskPermissionFragment : BaseFragment<FragmentFitAskPermissionBinding>() {

    private val viewModel by viewModels<FitAskPermissionViewModel>()
    override fun getLayout(): Int = R.layout.fragment_fit_ask_permission
    override fun getViewModel(): ViewModel = viewModel

    @Inject
    lateinit var googleFitUtils: GoogleFitUtils

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()

        if (googleFitUtils.hasOAuthPermission()) {
            goToHomeScreen()
        }
    }

    private fun goToHomeScreen() {
        findNavController().navigate(
            FitAskPermissionFragmentDirections.actionFitAskPermissionFragmentToHomeFragment()
        )
    }

    private fun subscribeObservers() {
        viewModel.reconnectClick.observe(viewLifecycleOwner) {
            connectWithFit()
        }
    }

    private fun connectWithFit() {
        if (googleFitUtils.hasOAuthPermission()) {
            googleFitUtils.disableGoogleFit({
                Log.e("$$$", "Disabled")
            }, {
                Log.e("$$$", "failure = $it ${it.message}")
            })
        }
        googleFitUtils.requestPermission(this)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.e("$$$", "$requestCode = $resultCode")
        when (resultCode) {
            RESULT_OK -> {
                goToHomeScreen()
            }
            else -> {
                Toast.makeText(requireContext(), "Not able to connect", Toast.LENGTH_SHORT).show()
            }
        }
    }
}