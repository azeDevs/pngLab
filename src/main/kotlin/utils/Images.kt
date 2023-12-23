package utils

import models.PxData
import java.awt.image.BufferedImage
import java.io.*
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

    fun saveTga(pxData: PxData, fileName: String = FILENAME_OUT) {
        val image = createImageFromPxData(pxData)
        val outputFile = File(getPath("$fileName.tga").absolutePath)
        writeTGA(image, outputFile)
    }

    private fun writeTGA(image: BufferedImage, file: File) {
        DataOutputStream(BufferedOutputStream(FileOutputStream(file))).use { out ->
            val width = image.width
            val height = image.height

            // TGA Header
            val header = ByteArray(18)
            header[2] = 2 // Truecolor image
            header[12] = (width and 0xFF).toByte()
            header[13] = ((width shr 8) and 0xFF).toByte()
            header[14] = (height and 0xFF).toByte()
            header[15] = ((height shr 8) and 0xFF).toByte()
            header[16] = 32 // 32 bits per pixel

            out.write(header)

            // Image Data
            for (y in height - 1 downTo 0) { // TGA stores pixels bottom to top
                for (x in 0 until width) {
                    val rgba = image.getRGB(x, y)
                    out.writeByte((rgba and 0xFF))        // Blue
                    out.writeByte((rgba shr 8) and 0xFF) // Green
                    out.writeByte((rgba shr 16) and 0xFF) // Red
                    out.writeByte((rgba shr 24) and 0xFF) // Alpha
                }
            }
        }
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

    /*
        HEADER: 00 00 0A / 00 00 00 00 00 00 00 00 00 / 40 01 30 00 20 00

        viewer: https://schmittl.github.io/tgajs/
        useful: https://docs.fileformat.com/image/tga/
        metadata: https://www.metadata2go.com/result#j=348584d7-a4db-4442-bdd3-ea696da745ff
    */

}