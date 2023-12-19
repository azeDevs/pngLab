package tools

import utils.Images
import models.PixelData

object ColorModifier {

    fun modifyAndSaveNewImage(pixelData: PixelData) {
        val averageColor = PixelAnalyzer.getAverageRGB(pixelData)

        val modifiedPixelData = modifyFirstPixel(pixelData, averageColor)

        Images.saveImageAsPNG(modifiedPixelData)
    }

    private fun modifyFirstPixel(pixelData: PixelData, averageColor: PixelData.RGBA): PixelData {
        val rgbaArray = pixelData.getRGBAArray()
        if (rgbaArray.isNotEmpty() && rgbaArray[0].isNotEmpty()) {
            rgbaArray[0][0] = averageColor
        }
        return PixelData(rgbaArray)
    }

}