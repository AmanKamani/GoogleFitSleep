package jb.production.fitsleep.models

enum class SleepStage(text: String) {
    UNUSED("Unused"),
    AWAKE("Awake (during sleep)"),
    SLEEP("Sleep"),
    OUT_OF_BED("Out-of-bed"),
    LIGHT_SLEEP("Light sleep"),
    DEEP_SLEEP("Deep sleep"),
    REM_SLEEP("REM sleep")
}