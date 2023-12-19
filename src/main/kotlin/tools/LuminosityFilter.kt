package tools

import models.PixelData

object LuminosityFilter {

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

}
