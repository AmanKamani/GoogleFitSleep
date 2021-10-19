package jb.production.fitsleep.models

data class SleepSession(
    val startTime: Long,
    val endTime: Long
) : BaseSleepData() {
    private val _points: MutableList<SleepPoint> = mutableListOf()

    val duration: Long
        get() = endTime - startTime

    val sleepPoints: List<SleepPoint>
        get() = _points

    fun addPoint(sleepPoint: SleepPoint) {
        _points.add(sleepPoint)
    }

    fun filterBySleepStage(sleepStage: SleepStage): List<SleepPoint> {
        return _points.filter { it.stage == sleepStage.ordinal }
    }

    fun countBySleepStage(sleepStage: SleepStage): Int {
        return _points.count { it.stage == sleepStage.ordinal }
    }

    fun totalDurationBySleepStage(sleepStage: SleepStage): Long {
        return _points.filter { it.stage == sleepStage.ordinal }
            .sumOf { it.duration }
    }


    override fun toString(): String {
        var str =
            "\n===== ${getTimeString(startTime)} - ${getTimeString(endTime)} ($startTime - $endTime) =====\n"

        _points.forEach {
            str += "$it\n"
        }
        return str
    }
}
