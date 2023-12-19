package tools

import models.PixelData

object PixelAnalyzer {

    fun getAverageRGB(pixelData: PixelData): PixelData.RGBA {
        val rgbaArray = pixelData.getRGBAArray()
        var totalR = 0.0
        var totalG = 0.0
        var totalB = 0.0
        var totalWeight = 0.0

        for (row in rgbaArray) {
            for (rgba in row) {
                val weight = rgba.a / 255.0 // Normalizing alpha to be between 0 and 1
                totalR += rgba.r * weight
                totalG += rgba.g * weight
                totalB += rgba.b * weight
                totalWeight += weight
            }
        }

        // Avoid division by zero in case of all pixels being fully transparent
        if (totalWeight == 0.0) {
            return PixelData.RGBA(0, 0, 0, 0)
        }

        // Calculating average values
        val averageR = (totalR / totalWeight).toInt()
        val averageG = (totalG / totalWeight).toInt()
        val averageB = (totalB / totalWeight).toInt()

        return PixelData.RGBA(averageR, averageG, averageB, 255) // Alpha set to 255 (opaque) for the average color
    }

}