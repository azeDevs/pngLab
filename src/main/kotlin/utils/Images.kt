package utils

import models.PxData
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Paths
import javax.imageio.ImageIO

object Images {
    private val DIR_HOME = Paths.get(System.getProperty("user.dir")).toString()
    private const val FILENAME_IN = "test" // Replace with the path to your PNG image
    private const val FILENAME_OUT = "out"

    fun loadPng(fileName: String = FILENAME_IN): BufferedImage? {
        return try {
            ImageIO.read(getPath("$fileName.png"))
        } catch (e: Exception) {
            println("Error loading image: ${e.message}")
            null
        }
    }

    fun savePng(pxData: PxData, fileName: String = FILENAME_OUT) {
        val image = createImageFromPxData(pxData)
        val outputFile = File(getPath("$fileName.png").absolutePath)
        ImageIO.write(image, "png", outputFile)
    }

    private fun getPath(fileName: String): File {
        val imagesDir = "images"
        return File(Paths.get(DIR_HOME, imagesDir, fileName).toUri())
    }

    private fun createImageFromPxData(pxData: PxData): BufferedImage {
        val rgbaArray = pxData.getData()
        val height = rgbaArray.size
        val width = if (height > 0) rgbaArray[0].size else 0
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

        for (y in rgbaArray.indices) {
            for (x in rgbaArray[y].indices) {
                val rgba = rgbaArray[y][x]
                val color = (rgba.a shl 24) or (rgba.r shl 16) or (rgba.g shl 8) or rgba.b
                image.setRGB(x, y, color)
            }
        }
        return image
    }

}