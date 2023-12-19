package utils

import models.PixelData
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Paths
import javax.imageio.ImageIO

object Images {
    private val DIR_HOME = Paths.get(System.getProperty("user.dir")).toString()
    private const val FILENAME_IN = "test.png" // Replace with the path to your PNG image
    private const val FILENAME_OUT = "out.png"


    fun loadImage(): BufferedImage? {
        return try {
            ImageIO.read(getPath(FILENAME_IN))
        } catch (e: Exception) {
            println("Error loading image: ${e.message}")
            null
        }
    }

    private fun getPath(fileName: String): File {
        val imagesDir = "images"
        return File(Paths.get(DIR_HOME, imagesDir, fileName).toUri())
    }

    private fun createImageFromPixelData(pixelData: PixelData): BufferedImage {
        val rgbaArray = pixelData.getRGBAArray()
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

    fun saveImageAsPNG(pixelData: PixelData) {
        val image = createImageFromPixelData(pixelData)
        val outputFile = File(getPath(FILENAME_OUT).absolutePath)
        ImageIO.write(image, "png", outputFile)
    }

}