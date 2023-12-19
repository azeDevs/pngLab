package utils

import models.PixelData
import java.awt.Color

/*
    TODO: Account for empty pixels. instead of printing them, accumulate them and print the amount skipped
*/

object Printer {
    private const val columnWidth = 8 // Adjust the column width as needed
    private const val bar = "  │  "

    fun printPixelDataValues(pixelData: PixelData) {
        val rgbaArray = pixelData.getRGBAArray()
        val height = rgbaArray.size
        val width = if (height > 0) rgbaArray[0].size else 0

        for (y in rgbaArray.indices) {
            for (x in rgbaArray[y].indices) {
                println(getCoordStr(x, y) + getColorStr(rgbaArray[y][x]))
            }
        }

        println("Image Width: $width, Height: $height")
    }

    fun getColorStr(rgba: PixelData.RGBA) =
        getHexStr(rgba) + getRGBAStr(rgba) + getHSBStr(rgba) + getLABStr(rgba)

    private fun getCoordStr(x: Int, y: Int): String {
        val coordFormat = "X:%-${columnWidth - 2}d Y:%-${columnWidth - 2}d$bar"
        return String.format(coordFormat, x + 1, y + 1)
    }

    private fun getRGBAStr(rgba: PixelData.RGBA): String {
        val rgbaFormat = "%-${columnWidth}s %-${columnWidth}s %-${columnWidth}s %-${columnWidth}s$bar"
        return String.format(
            rgbaFormat,
            "R:${rgba.r}",
            "G:${rgba.g}",
            "B:${rgba.b}",
            "A:${rgba.a}"
        )
    }

    private fun getHSBStr(rgba: PixelData.RGBA): String {
        val hsbVals = Color.RGBtoHSB(rgba.r, rgba.g, rgba.b, null)
        val hsbFormat = "%-${columnWidth}s %-${columnWidth}s %-${columnWidth}s$bar"
        return String.format(
            hsbFormat,
            "H:${"%.2f".format(hsbVals[0])}",
            "S:${"%.2f".format(hsbVals[1])}",
            "B:${"%.2f".format(hsbVals[2])}"
        )
    }

    private fun getLABStr(rgba: PixelData.RGBA): String {
        val labL = 0.299 * rgba.r + 0.587 * rgba.g + 0.114 * rgba.b
        val labFormat = "%-${columnWidth}s$bar"
        return String.format(labFormat, "L:${"%.2f".format(labL)}")
    }

    private fun getHexStr(rgba: PixelData.RGBA): String {
        val hexFormat = "%-${columnWidth}s"
        val hex = String.format("#%02x%02x%02x%02x", rgba.r, rgba.g, rgba.b, rgba.a).toUpperCase()
        return String.format(hexFormat, "$hex$bar")
    }

}