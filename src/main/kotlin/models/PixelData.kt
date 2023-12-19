package models

import java.awt.Color
import java.awt.image.BufferedImage

/*
    TODO: Account for empty pixels. Change the array to a list and remove them?
*/

class PixelData {
    data class RGBA(val r: Int, val g: Int, val b: Int, val a: Int)
    data class HSB(val h: Float, val s: Float, val b: Float)
    data class LAB(val l: Float)

    private var rgbaArray: Array<Array<RGBA>>
    private lateinit var hsbArray: Array<Array<HSB>>
    private lateinit var labArray: Array<Array<LAB>>

    constructor(image: BufferedImage) {
        rgbaArray = convertImageToRGBAArray(image)
        hsbArray = convertToHSBArray(rgbaArray)
        labArray = convertToLABArray(rgbaArray)
    }

    constructor(rgbaArray: Array<Array<RGBA>>) {
        this.rgbaArray = rgbaArray
    }

    private fun convertImageToRGBAArray(image: BufferedImage): Array<Array<RGBA>> {
        val width = image.width
        val height = image.height
        val array = Array(height) { Array(width) { RGBA(0, 0, 0, 0) } }

        for (y in 0 until height) {
            for (x in 0 until width) {
                val pixel = image.getRGB(x, y)
                val r = (pixel shr 16) and 0xFF
                val g = (pixel shr 8) and 0xFF
                val b = pixel and 0xFF
                val a = (pixel shr 24) and 0xFF
                array[y][x] = RGBA(r, g, b, a)
            }
        }
        return array
    }

    private fun convertToHSBArray(rgbaArray: Array<Array<RGBA>>): Array<Array<HSB>> {
        val height = rgbaArray.size
        val width = rgbaArray[0].size
        val array = Array(height) { Array(width) { HSB(0f, 0f, 0f) } }

        for (y in rgbaArray.indices) {
            for (x in rgbaArray[y].indices) {
                val r = rgbaArray[y][x].r / 255f
                val g = rgbaArray[y][x].g / 255f
                val b = rgbaArray[y][x].b / 255f
                val hsbVals = Color.RGBtoHSB(r.toInt(), g.toInt(), b.toInt(), null)
                array[y][x] = HSB(hsbVals[0], hsbVals[1], hsbVals[2])
            }
        }
        return array
    }

    private fun convertToLABArray(rgbaArray: Array<Array<RGBA>>): Array<Array<LAB>> {
        val height = rgbaArray.size
        val width = rgbaArray[0].size
        val array = Array(height) { Array(width) { LAB(0f) } }

        for (y in rgbaArray.indices) {
            for (x in rgbaArray[y].indices) {
                val r = rgbaArray[y][x].r
                val g = rgbaArray[y][x].g
                val b = rgbaArray[y][x].b
                val l = 0.299f * r + 0.587f * g + 0.114f * b
                array[y][x] = LAB(l)
            }
        }
        return array
    }

    private fun calculateLuminosity(rgba: RGBA): Double {
        // Using a common formula for luminance
        return 0.299 * rgba.r + 0.587 * rgba.g + 0.114 * rgba.b
    }

    fun getRGBAArray(): Array<Array<RGBA>> = rgbaArray
    fun getHSBArray(): Array<Array<HSB>> = hsbArray
    fun getLABArray(): Array<Array<LAB>> = labArray

}