package jb.production.fitsleep.screens

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.people.v1.PeopleService
import dagger.hilt.android.AndroidEntryPoint
import jb.production.fitsleep.R
import jb.production.fitsleep.databinding.FragmentFitAskPermissionBinding
import jb.production.fitsleep.utils.GoogleFitUtils
import jb.production.fitsleep.viewmodels.FitAskPermissionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FitAskPermissionFragment : BaseFragment<FragmentFitAskPermissionBinding>() {

    private val viewModel by viewModels<FitAskPermissionViewModel>()
    override fun getLayout(): Int = R.layout.fragment_fit_ask_permission
    override fun getViewModel(): ViewModel = viewModel

    lateinit var client: GoogleSignInClient

    @Inject
    lateinit var googleFitUtils: GoogleFitUtils

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(getString(R.string.web_client_id))
            .requestScopes(
                Scope("https://www.googleapis.com/auth/user.birthday.read"),
                Scope("https://www.googleapis.com/auth/user.gender.read")
            )
            .addExtension(googleFitUtils.fitnessOptions)
            .build()

        client = GoogleSignIn.getClient(requireContext(), gso)


//        if (googleFitUtils.hasOAuthPermission()) {
//            goToHomeScreen()
//        }
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (account != null) {
            // already signed in
            client.signOut().addOnSuccessListener {
                Log.e("$$$", "signed out")
                activityLauncher.launch(client.signInIntent)
            }
                .addOnFailureListener {
                    Log.e("$$$", "fail to signout $it")
                }
        } else {
            activityLauncher.launch(client.signInIntent)
        }
    }

    private var activityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                RESULT_OK -> {
                    GoogleSignIn.getSignedInAccountFromIntent(it.data)
                        .addOnSuccessListener { account ->
                            Log.e("$$$", "success = ${account.serverAuthCode}")
                            val clientId = getString(R.string.web_client_id)
                            val clientSecret = getString(R.string.web_client_secret)

                            val httpTransport = NetHttpTransport()
                            val converter = GsonFactory()
                            lifecycleScope.launch(Dispatchers.IO) {

                                val tokenResponse = GoogleAuthorizationCodeTokenRequest(
                                    httpTransport,
                                    converter,
                                    clientId,
                                    clientSecret,
                                    account.serverAuthCode,
                                    "urn:ietf:wg:oauth:2.0:oob"
                                ).execute()


                                val googleCreds = GoogleCredential.Builder()
                                    .setClientSecrets(clientId, clientSecret)
                                    .setTransport(httpTransport)
                                    .setJsonFactory(converter)
                                    .build()

                                googleCreds.setFromTokenResponse(tokenResponse)

                                val peopleService =
                                    PeopleService(NetHttpTransport(), GsonFactory(), googleCreds)
                                val person = peopleService.people()
                                    .get("people/me")
                                    .setPersonFields("genders,birthdays")
                                    .execute()


                                Log.e("$$$", "data = $person")
                                Log.e("$$$", "gender = ${person.genders[0].value}")
                                Log.e("$$$", "bday = ${person.birthdays[0].date}")
                            }
                        }
                        .addOnFailureListener {
                            Log.e("$$$", "unable to et the account = $it")
                        }
                    Log.e("$$$", "success got account")

                    val name = GoogleSignIn.getLastSignedInAccount(requireContext())?.givenName
                    Log.e("$$$", "name = $name")
                }
                RESULT_CANCELED -> {
                    Log.e("$$$", "cancled")
                }
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