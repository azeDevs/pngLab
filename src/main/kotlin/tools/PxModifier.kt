package tools

import utils.Images
import models.PxData

object PxModifier {

    fun modifyAndSaveNewImage(pxData: PxData) {
        val averageColor = PxAnalyzer.getAverageRGB(pxData)

        val modifiedPixelData = modifyFirstPixel(pxData, averageColor)

        Images.savePng(modifiedPixelData)
    }

    private fun modifyFirstPixel(pxData: PxData, averageColor: PxData.RGBA): PxData {
        val rgbaArray = pxData.getData()
        if (rgbaArray.isNotEmpty() && rgbaArray[0].isNotEmpty()) {
            rgbaArray[0][0] = averageColor
        }
        return PxData(rgbaArray)
    }

}