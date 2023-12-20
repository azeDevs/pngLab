import models.PxData
import tools.*
import utils.Images
import utils.Printer.getColorStr

fun main() {

    val image = Images.loadPng()
    if (image != null) {

        val pxData = PxData(image)

//        Printer.printPixelDataValues(pixelData)

        println("Avg Lumin: ${getColorStr(
            PxAnalyzer.getAverageRGB(PxFilter.filterTopLuminosity(pxData))
        )}")
        println("Avg light: ${getColorStr(
            PxAnalyzer.getAverageRGB(PxFilter.byBrightness(pxData, 0.75f, 1.0f))
        )}")
        println("Avg midtn: ${getColorStr(
            PxAnalyzer.getAverageRGB(PxFilter.byBrightness(pxData, 0.25f, 0.75f))
        )}")
        println("Avg shade: ${getColorStr(
            PxAnalyzer.getAverageRGB(PxFilter.byBrightness(pxData, 0.0f, 0.25f))
        )}")
        Images.savePng(PxFilter.byBrightness(pxData, 0.75f, 1.0f), "out_h")
        Images.savePng(PxFilter.byBrightness(pxData, 0.25f, 0.75f), "out_m")
        Images.savePng(PxFilter.byBrightness(pxData, 0.0f, 0.25f), "out_s")

//        Loader.start()

    } else println("Failed to load the image.")

}

