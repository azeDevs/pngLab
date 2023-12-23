package utils

import GetStr.getColorName
import RGBA
import models.PxData
import tools.PxAnalyzer
import java.awt.Color

/*
    TODO: Account for empty pixels. instead of printing them, accumulate them and print the amount skipped
*/

object Printer {
    private const val columnWidth = 8 // Adjust the column width as needed
    private const val bar = "  │  "

    fun printPixelDataValues(pxData: PxData) {
        val rgbaArray = pxData.getData()
        val height = rgbaArray.size
        val width = if (height > 0) rgbaArray[0].size else 0
        val totalPixels = width * height
        var skippedPixels = 0
        var totalSkippedPixels = 0

        for (y in rgbaArray.indices) {
            for (x in rgbaArray[y].indices) {
                if (rgbaArray[y][x].a > 0) {
                    if (skippedPixels > 0) {
                        println("\rINVISIBLE PIXELS SKIPPED: $skippedPixels")
                        skippedPixels = 0
                    }
                    println(getCoordStr(x, y) + getColorStr(rgbaArray[y][x]))
                } else {
                    skippedPixels++
                    totalSkippedPixels++
                }
            }
        }

        if (skippedPixels > 0) println("\rINVISIBLE PIXELS SKIPPED: $skippedPixels")
        println("─────────────────────────────────────────────────────────────────────────")
        println("IMAGE DIMENSIONS: ${width}x$height")
        println("INVISIBLE PIXELS: $totalSkippedPixels / $totalPixels " +
                "(${String.format("%.0f%%", 100f * totalSkippedPixels / totalPixels)})")
        println("\nAVERAGE COLOR:  " +
                getColorName(PxAnalyzer.getAverageRGB(pxData)) + bar +
                getRGBAStr(PxAnalyzer.getAverageRGB(pxData)) +
                getHSBStr(PxAnalyzer.getAverageRGB(pxData)) +
                "\n")
    }

    fun getColorStr(rgba: RGBA) =
        getHexStr(rgba) +
        getRGBAStr(rgba) +
        getHSBStr(rgba)

    private fun getCoordStr(x: Int, y: Int): String {
        val coordFormat = "X:%-${columnWidth - 2}d Y:%-${columnWidth - 2}d$bar"
        return String.format(coordFormat, x + 1, y + 1)
    }

    private fun getRGBAStr(rgba: RGBA): String {
        val rgbaFormat = "%-${columnWidth}s %-${columnWidth}s %-${columnWidth}s %-${columnWidth}s$bar"
        return String.format(
            rgbaFormat,
            "R:${rgba.r}",
            "G:${rgba.g}",
            "B:${rgba.b}",
            "A:${rgba.a}"
        )
    }

    private fun getHSBStr(rgba: RGBA): String {
        val hsbVals = Color.RGBtoHSB(rgba.r, rgba.g, rgba.b, null)
        val hueDegrees = hsbVals[0] * 360
        val saturationPercentage = hsbVals[1] * 100
        val brightnessPercentage = hsbVals[2] * 100
        val opacityPercentage = (if (rgba.a > 0) (rgba.a / 255f * 100) else 0f)
        val hsbFormat = "%-${columnWidth}s %-${columnWidth}s %-${columnWidth}s %-${columnWidth}s$bar"
        return String.format(
            hsbFormat,
            "H:${"%.0f".format(hueDegrees)}°",
            "S:${"%.0f%%".format(saturationPercentage)}",
            "B:${"%.0f%%".format(brightnessPercentage)}",
            "O:${"%.0f%%".format(opacityPercentage)}"
        )
    }

    private fun getHexStr(rgba: RGBA): String {
        val hexFormat = "%-${columnWidth}s"
        val hex = GetStr.getHexFromRGBA(rgba) //getColorName(rgba)
        return String.format(hexFormat, "#$hex$bar")
    }

}