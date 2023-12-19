import models.PixelData
import tools.PixelAnalyzer
import utils.Images
import utils.Printer

fun main() {

    val image = Images.loadImage()
    if (image != null) {

        val pixelData = PixelData(image)

        Printer.printPixelDataValues(pixelData)
        println("\nAverage: ${Printer.getColorStr(PixelAnalyzer.getAverageRGB(pixelData))}")

//        ColorModifier.modifyAndSaveNewImage(pixelData)
//        Loader.start()

    } else println("Failed to load the image.")

}

