package models

import java.awt.image.BufferedImage

/*
    TODO: Account for empty pixels. Change the array to a list and remove them?
*/

class PxData {
    data class RGBA(val r: Int, val g: Int, val b: Int, val a: Int)

    private var rgbaArray: Array<Array<RGBA>>

    constructor(image: BufferedImage) {
        this.rgbaArray = convertImageToRGBAArray(image)
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

    fun getData(): Array<Array<RGBA>> = rgbaArray

}