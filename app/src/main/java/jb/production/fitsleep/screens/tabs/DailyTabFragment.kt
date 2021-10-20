package jb.production.fitsleep.screens.tabs

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.fitness.request.SessionReadRequest
import dagger.hilt.android.AndroidEntryPoint
import jb.production.fitsleep.R
import jb.production.fitsleep.databinding.DailyTabFragmentBinding
import jb.production.fitsleep.models.SleepPoint
import jb.production.fitsleep.models.SleepSession
import jb.production.fitsleep.models.SleepStage
import jb.production.fitsleep.screens.BaseFragment
import jb.production.fitsleep.utils.GoogleFitUtils
import jb.production.fitsleep.viewmodels.DailyTabViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class DailyTabFragment : BaseFragment<DailyTabFragmentBinding>() {

    companion object {
        private const val goalDuration = 28800L
    }

    private val viewModel by viewModels<DailyTabViewModel>()
    val endDate = LocalDateTime.now().atZone(ZoneId.systemDefault())
    val startDate = endDate.minusDays(10)

    val sleepData = mutableMapOf<Long, MutableList<SleepSession>>()

    @Inject
    lateinit var googleFitUtils: GoogleFitUtils

    override fun getLayout(): Int = R.layout.daily_tab_fragment
    override fun getViewModel(): ViewModel = viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        readThroughSessionApi()
    }


    fun readThroughSessionApi() {
        val request = SessionReadRequest.Builder()
            .setTimeInterval(startDate.toEpochSecond(), endDate.toEpochSecond(), TimeUnit.SECONDS)
            .read(DataType.TYPE_SLEEP_SEGMENT)
            .readSessionsFromAllApps()
            .includeSleepSessions()
            .build()

        Fitness.getSessionsClient(requireContext(), googleFitUtils.getGoogleAccount())
            .readSession(request)
            .addOnFailureListener {
                println("failure = $it = ${it.message}")
            }
            .addOnSuccessListener { response ->
                println("succeed = ${response.status}")
                for (session in response.sessions) {

                    val sessionStart = session.getStartTime(TimeUnit.SECONDS)
                    val sessionEnd = session.getEndTime(TimeUnit.SECONDS)

                    // To Store Data
                    val ss = SleepSession(sessionStart, sessionEnd)

                    val dataSet = response.getDataSet(session)
                    for (data in dataSet) {
                        for (point in data.dataPoints) {
                            val sleepStageVal =
                                point.getValue(Field.FIELD_SLEEP_SEGMENT_TYPE).asInt()

                            // To Store Data
                            val sp = SleepPoint(
                                point.getStartTime(TimeUnit.SECONDS),
                                point.getEndTime(TimeUnit.SECONDS),
                                sleepStageVal
                            )
                            ss.addPoint(sp)
                        }
                    }
                    // To Store Data
                    val startDateInSecond =
                        Instant.ofEpochSecond(sessionStart).atZone(ZoneId.systemDefault())
                            .toLocalDate()

                    this.sleepData.get(startDateInSecond.toEpochDay())?.add(ss) ?: run {
                        this.sleepData.put(startDateInSecond.toEpochDay(), mutableListOf(ss))
                    }
                }
                dumpData()
                evaluateData()
            }
    }

    private fun evaluateData() {
        println("\n\n========= Data Evaluation ========")
        sleepData.entries.forEach {

            var totalAwake = 0
            var totalAwakeTimeDuration = 0L
            var totalBedTimeDuration = 0L

            it.value.forEach { sleepSession ->
                totalBedTimeDuration += sleepSession.duration
                totalAwake += sleepSession.countBySleepStage(SleepStage.AWAKE)
                totalAwakeTimeDuration += sleepSession.totalDurationBySleepStage(SleepStage.AWAKE)
            }

            val totalSleepTimeDuration = totalBedTimeDuration - totalAwakeTimeDuration
            val sleepDeficit = totalSleepTimeDuration - goalDuration
            val sleepDeficitDecision = when {
                sleepDeficit > 0 -> ">"
                sleepDeficit < 0 -> "<"
                else -> "="
            }

            printDivider()
            println(
                "Date: ${LocalDate.ofEpochDay(it.key)}   SleepGoal: ${
                    LocalTime.ofSecondOfDay(
                        goalDuration
                    )
                }"
            )
            println("Total Bed Time: ${LocalTime.ofSecondOfDay(totalBedTimeDuration)}")
            println("Total Awake Time: ${LocalTime.ofSecondOfDay(totalAwakeTimeDuration)}")
            println("Total Sleep Time: ${LocalTime.ofSecondOfDay(totalSleepTimeDuration)}")
            println(
                "Total Sleep Deficit: $sleepDeficitDecision ${
                    LocalTime.ofSecondOfDay(
                        Math.abs(
                            sleepDeficit
                        )
                    )
                }"
            )
            println("No. of Awakenings: $totalAwake")
        }
    }

    private fun dumpData() {
        this.sleepData.entries.forEach {
            printDivider()
            val date = LocalDate.ofEpochDay(it.key)
            println("Date: $date")
            it.value.forEach {
                println("\n" + it.toString())
            }
        }
    }


    fun readThroughHistoryApi() {

        println("$startDate - $endDate")
        printDivider()

        val request = DataReadRequest.Builder()
            .read(DataType.TYPE_SLEEP_SEGMENT)
            .setTimeRange(startDate.toEpochSecond(), endDate.toEpochSecond(), TimeUnit.SECONDS)
            .build()

        Fitness.getHistoryClient(requireContext(), googleFitUtils.getGoogleAccount())
            .readData(request)
            .addOnFailureListener {
                println("fail to read = $it = ${it.message}")
            }
            .addOnSuccessListener {
                println("succeed")
                for (dataSet in it.buckets.flatMap { it.dataSets }) {
                    for (points in dataSet.dataPoints) {
                        println("type = ${points.dataType.name}")
                        println("start = ${getDateTimeString(points.getStartTime(TimeUnit.SECONDS))}")
                        println("end = ${getDateTimeString(points.getEndTime(TimeUnit.SECONDS))}")

                        for (field in points.dataType.fields) {
                            println("field = ${field.format}")
                            println("field = ${field.isOptional}")
                            println("field = ${points.getValue(field)}")
                        }
                    }
                    printDivider()
                }
            }
    }

    fun getDateTimeString(seconds: Long): String {
        return Instant.ofEpochSecond(seconds).atZone(
            ZoneId.systemDefault()
        ).toLocalDateTime().toString()
    }

    fun println(text: String) {
        Log.e("$$$", text)
    }

    fun printDivider() {
        Log.e("$$$", "===================")
    }

}