package tools

import models.PixelData
import java.awt.Color

object PixelFilter {

    fun filterTopLuminosity(pixelData: PixelData): PixelData {
        val rgbaArray = pixelData.getRGBAArray()
        val luminosityThreshold = findLuminosityThreshold(rgbaArray)

        val filteredArray = rgbaArray.map { row ->
            row.map { rgba ->
                if (calculateLuminosity(rgba) >= luminosityThreshold) rgba else PixelData.RGBA(0, 0, 0, 0)
            }.toTypedArray()
        }.toTypedArray()

        return PixelData(filteredArray)
    }

    private fun calculateLuminosity(rgba: PixelData.RGBA): Double {
        // Using a common formula for luminance
        return 0.299 * rgba.r + 0.587 * rgba.g + 0.114 * rgba.b
    }

    private fun findLuminosityThreshold(rgbaArray: Array<Array<PixelData.RGBA>>): Double {
        val luminosities = rgbaArray.flatten().map { calculateLuminosity(it) }
        val sortedLuminosities = luminosities.sorted()
        val thresholdIndex = (sortedLuminosities.size * 0.8).toInt() // Top 20%
        return sortedLuminosities.getOrElse(thresholdIndex) { 0.0 }
    }

    fun filterBrightnessRange(pixelData: PixelData, lowerBound: Float = 0.2f, upperBound: Float = 0.8f): PixelData {
        val rgbaArray = pixelData.getRGBAArray()
        val brightnessThresholds = findBrightnessThresholds(rgbaArray, lowerBound, upperBound)

        val filteredArray = rgbaArray.map { row ->
            row.map { rgba ->
                val brightness = calculateBrightness(rgba)
                if (brightness in brightnessThresholds) rgba else PixelData.RGBA(0, 0, 0, 0)
            }.toTypedArray()
        }.toTypedArray()

        return PixelData(filteredArray)
    }

    private fun calculateBrightness(rgba: PixelData.RGBA): Float {
        val hsbVals = Color.RGBtoHSB(rgba.r, rgba.g, rgba.b, null)
        return hsbVals[2] // Brightness is the third value in HSB array
    }

    private fun findBrightnessThresholds(rgbaArray: Array<Array<PixelData.RGBA>>, lowerBound: Float, upperBound: Float): ClosedFloatingPointRange<Float> {
        val brightnessValues = rgbaArray.flatten().map { calculateBrightness(it) }
        val sortedBrightness = brightnessValues.sorted()
        val lowerIndex = (sortedBrightness.size * lowerBound).toInt().coerceIn(0, sortedBrightness.size)
        val upperIndex = (sortedBrightness.size * upperBound).toInt().coerceIn(0, sortedBrightness.size)

        val lowerThreshold = sortedBrightness.getOrElse(lowerIndex) { 0.0f }
        val upperThreshold = sortedBrightness.getOrElse(upperIndex) { 1.0f }

        return lowerThreshold..upperThreshold
    }

}
