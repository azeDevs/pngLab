import models.PxData
import tools.*
import utils.Images
import utils.Printer
import utils.Printer.getColorStr

fun main() {

    val pngImage = Images.loadPng()
    if (pngImage != null) {

        val pngData = PxData(pngImage)
        Printer.printPixelDataValues(pngData)

        println("Avg highlight: ${getColorStr(
            PxAnalyzer.getAverageRGB(pngData.highlights)
        )}")
        println("Avg midtone:   ${getColorStr(
            PxAnalyzer.getAverageRGB(pngData.midtones)
        )}")
        println("Avg shadow:    ${getColorStr(
            PxAnalyzer.getAverageRGB(pngData.shadows)
        )}")
        Images.savePng(pngData.highlights, "out_h")
        Images.savePng(pngData.midtones, "out_m")
        Images.savePng(pngData.shadows, "out_s")
        Images.saveTga(pngData, "tgatest")

    } else println("Failed to load the image.")

}

data class HMS(val highlights: PxData, val midtones: PxData, val shadows: PxData)
data class RGBA(val r: Int, val g: Int, val b: Int, val a: Int)
