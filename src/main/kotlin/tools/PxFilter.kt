package tools

import HMS
import RGBA
import models.PxData
import java.awt.Color

object PxFilter {

    fun getHMS(pxData: PxData, lowerBound: Float = 0.16f, upperBound: Float = 0.84f, fuzziness: Float = 0.16f): HMS {
        val highlights = byBrightness(pxData, upperBound, 1.0f, fuzziness)
        val midtones = byBrightness(pxData, lowerBound, upperBound, fuzziness)
        val shadows = byBrightness(pxData, 0.0f, lowerBound, fuzziness)

        return HMS(highlights, midtones, shadows)
    }

    private fun calculateMidtones(original: PxData, highlights: PxData, shadows: PxData): PxData {
        val originalArray = original.getData()
        val highlightsArray = highlights.getData()
        val shadowsArray = shadows.getData()

        val midtonesArray = originalArray.mapIndexed { y, row ->
            row.mapIndexed { x, rgba ->
                val originalAlpha = rgba.a
                val highlightAlpha = highlightsArray[y][x].a
                val shadowAlpha = shadowsArray[y][x].a

                val maxAlphaAdjustment = maxOf(highlightAlpha, shadowAlpha)
                val midtoneAlpha = if (originalAlpha > maxAlphaAdjustment) originalAlpha - maxAlphaAdjustment else 0

                RGBA(rgba.r, rgba.g, rgba.b, midtoneAlpha)
            }.toTypedArray()
        }.toTypedArray()

        return PxData(midtonesArray)
    }

    private fun byBrightness(pxData: PxData, lowerBound: Float, upperBound: Float, fuzziness: Float): PxData {
        val rgbaArray = pxData.getData()
        val brightnessThresholds = findBrightnessThresholds(rgbaArray, lowerBound, upperBound, fuzziness)

        val filteredArray = rgbaArray.map { row ->
            row.map { rgba ->
                val brightness = calculateBrightness(rgba)
                val alphaAdjustment = calculateAlphaAdjustment(brightness, brightnessThresholds, fuzziness)
                RGBA(rgba.r, rgba.g, rgba.b, (rgba.a * alphaAdjustment).toInt())
            }.toTypedArray()
        }.toTypedArray()

        return PxData(filteredArray)
    }

    private fun calculateAlphaAdjustment(brightness: Float, thresholds: ClosedFloatingPointRange<Float>, fuzziness: Float): Float {
        val lowerThreshold = thresholds.start - fuzziness
        val upperThreshold = thresholds.endInclusive + fuzziness

        return when {
            brightness < lowerThreshold -> 0.0f
            brightness in lowerThreshold..thresholds.start -> (brightness - lowerThreshold) / fuzziness
            brightness in thresholds.endInclusive..upperThreshold -> (upperThreshold - brightness) / fuzziness
            brightness > upperThreshold -> 0.0f
            else -> 1.0f
        }.coerceIn(0.0f, 1.0f)
    }

    private fun findBrightnessThresholds(rgbaArray: Array<Array<RGBA>>, lowerBound: Float, upperBound: Float, fuzziness: Float): ClosedFloatingPointRange<Float> {
        val brightnessValues = rgbaArray.flatten().map { calculateBrightness(it) }
        val sortedBrightness = brightnessValues.sorted()
        val size = sortedBrightness.size
        val lowerIndex = (size * (lowerBound - fuzziness)).coerceIn(0f, size.toFloat()).toInt()
        val upperIndex = (size * (upperBound + fuzziness)).coerceIn(0f, size.toFloat()).toInt()

        val lowerThreshold = sortedBrightness.getOrElse(lowerIndex) { 0.0f }
        val upperThreshold = sortedBrightness.getOrElse(upperIndex) { 1.0f }

        return lowerThreshold..upperThreshold
    }

    private fun calculateBrightness(rgba: RGBA): Float {
        val hsbVals = Color.RGBtoHSB(rgba.r, rgba.g, rgba.b, null)
        return hsbVals[2] // Brightness is the third value in HSB array
    }

}
