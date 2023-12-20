import models.PixelData
import tools.*
import utils.Images
import utils.Printer
import utils.Printer.getColorStr

fun main() {

    val image = Images.loadImage()
    if (image != null) {

        val pixelData = PixelData(image)

//        Printer.printPixelDataValues(pixelData)

        println("Avg Lumin: ${getColorStr(
            PixelAnalyzer.getAverageRGB(PixelFilter.filterTopLuminosity(pixelData))
        )}")
        println("Avg light: ${getColorStr(
            PixelAnalyzer.getAverageRGB(PixelFilter.filterBrightnessRange(pixelData, 0.75f, 1.0f))
        )}")
        println("Avg midtn: ${getColorStr(
            PixelAnalyzer.getAverageRGB(PixelFilter.filterBrightnessRange(pixelData, 0.25f, 0.75f))
        )}")
        println("Avg shade: ${getColorStr(
            PixelAnalyzer.getAverageRGB(PixelFilter.filterBrightnessRange(pixelData, 0.0f, 0.25f))
        )}")
        Images.saveImageAsPNG(PixelFilter.filterBrightnessRange(pixelData, 0.75f, 1.0f), "out_h")
        Images.saveImageAsPNG(PixelFilter.filterBrightnessRange(pixelData, 0.25f, 0.75f), "out_m")
        Images.saveImageAsPNG(PixelFilter.filterBrightnessRange(pixelData, 0.0f, 0.25f), "out_s")

        ColorModifier.modifyAndSaveNewImage(PixelFilter.filterBrightnessRange(pixelData))
//        Loader.start()

    } else println("Failed to load the image.")

}

