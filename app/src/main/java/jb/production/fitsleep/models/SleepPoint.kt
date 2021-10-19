package jb.production.fitsleep.models

data class SleepPoint(
    val startTime: Long,
    val endTime: Long,
    val stage: Int
) : BaseSleepData() {


    val duration: Long
        get() = endTime - startTime

    override fun toString(): String {
        val sleepStage = SleepStage.values().get(stage)
        return "${getTimeString(startTime)} - ${getTimeString(endTime)} ($startTime-$endTime): $sleepStage"
    }
}