package jb.production.fitsleep.utils

import android.content.Context
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import javax.inject.Inject

class GoogleFitUtils @Inject constructor(private val context: Context) {

    companion object {
        const val FITNESS_DATA_REQUEST_CODE = 101
    }

    private val fitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_SLEEP_SEGMENT, FitnessOptions.ACCESS_READ)
        .build()

    fun getGoogleAccount() = GoogleSignIn.getAccountForExtension(context, fitnessOptions)

    fun hasOAuthPermission() = GoogleSignIn.hasPermissions(getGoogleAccount(), fitnessOptions)

    fun requestPermission(fragment: Fragment) {
        GoogleSignIn.requestPermissions(
            fragment,
            FITNESS_DATA_REQUEST_CODE,
            getGoogleAccount(),
            fitnessOptions,
        )
    }

    fun disableGoogleFit(
        successListener: OnSuccessListener<in Void>,
        failureListener: OnFailureListener
    ) {
        Fitness.getConfigClient(context, getGoogleAccount())
            .disableFit()
            .addOnSuccessListener(successListener)
            .addOnFailureListener(failureListener)
    }
}