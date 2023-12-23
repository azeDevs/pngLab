import models.PxData

object GetStr {
    fun getColorName(targetColor: RGBA, moreInfo: Boolean = false): String {
        var closestPair: Pair<String, String>? = null
        var minDistance = Int.MAX_VALUE

        for ((key, value) in colorMap) {
            val colorRGBA = parseHexToRGBA(key)
            val distance = calculateRGBDistance(targetColor, colorRGBA)

            if (distance < minDistance) {
                minDistance = distance
                closestPair = key to value
            }
        }

        closestPair?.let { (_, name) ->
            val deviationR = targetColor.r - parseHexToRGBA(closestPair.first).r
            val deviationG = targetColor.g - parseHexToRGBA(closestPair.first).g
            val deviationB = targetColor.b - parseHexToRGBA(closestPair.first).b

            val devRStr = if (deviationR < 0) "-${-deviationR}" else "+${deviationR}"
            val devGStr = if (deviationG < 0) "-${-deviationG}" else "+${deviationG}"
            val devBStr = if (deviationB < 0) "-${-deviationB}" else "+${deviationB}"

            return "#${getHexFromRGB(targetColor)} \"$name\"" +
                    if (moreInfo) " ($devRStr $devGStr $devBStr) #${closestPair.first}" else ""
        }

        // If no match is found, return an empty string with an error message.
        return "No closest color found."
    }

    fun getHexFromRGB(rgba: RGBA): String {
        return String.format("%02X%02X%02X", rgba.r, rgba.g, rgba.b)
    }

    fun getHexFromRGBA(rgba: RGBA, aSpace: Boolean = true): String {
        return String.format("%02X%02X%02X${if (aSpace) " " else ""}%02X",
            rgba.r, rgba.g, rgba.b, rgba.a)
    }

    private fun parseHexToRGBA(hex: String): RGBA {
        val color = hex.substring(1).toLong(16)
        val r = (color shr 16 and 0xFF).toInt()
        val g = (color shr 8 and 0xFF).toInt()
        val b = (color and 0xFF).toInt()
        return RGBA(r, g, b, 255) // Set alpha to 255 (fully opaque)
    }

    private fun calculateRGBDistance(color1: RGBA, color2: RGBA): Int {
        val dr = color1.r - color2.r
        val dg = color1.g - color2.g
        val db = color1.b - color2.b
        return dr * dr + dg * dg + db * db // Euclidean distance
    }

    // https://www.color-name.com/complete-list-of-html-color-names.color
    private val colorMap = mapOf(
        "FF0000" to "Red",
        "CD5C5C" to "Indian Red",
        "F08080" to "Light Coral",
        "FA8072" to "Salmon",
        "E9967A" to "Dark Salmon",
        "FFA07A" to "Light Salmon",
        "DC143C" to "Crimson",
        "B22222" to "Fire Brick",
        "8B0000" to "Dark Red",
        "00FF00" to "Lime",
        "008000" to "Green",
        "ADFF2F" to "Green Yellow",
        "7FFF00" to "Chartreuse",
        "7CFC00" to "Lawn Green",
        "32CD32" to "Lime Green",
        "98FB98" to "Pale Green",
        "90EE90" to "Light Green",
        "00FA9A" to "Medium Spring Green",
        "00FF7F" to "Spring Green",
        "3CB371" to "Medium Sea Green",
        "2E8B57" to "Sea Green",
        "228B22" to "Forest Green",
        "006400" to "Dark Green",
        "9ACD32" to "Yellow Green",
        "6B8E23" to "Olive Drab",
        "808000" to "Olive",
        "556B2F" to "Dark Olive Green",
        "66CDAA" to "Medium Aquamarine",
        "8FBC8B" to "Dark Sea Green",
        "20B2AA" to "Light Sea Green",
        "008B8B" to "Dark Cyan",
        "008080" to "Teal",
        "0000FF" to "Blue",
        "0000CD" to "Medium Blue",
        "00008B" to "Dark Blue",
        "000080" to "Navy",
        "191970" to "Midnight Blue",
        "4169E1" to "Royal Blue",
        "00FFFF" to "Cyan",
        "E0FFFF" to "Light Cyan",
        "AFEEEE" to "Pale Turquoise",
        "7FFFD4" to "Aquamarine",
        "40E0D0" to "Turquoise",
        "48D1CC" to "Medium Turquoise",
        "00CED1" to "Dark Turquoise",
        "5F9EA0" to "Cadet Blue",
        "4682B4" to "Steel Blue",
        "B0C4DE" to "Light Steel Blue",
        "B0E0E6" to "Powder Blue",
        "ADD8E6" to "Light Blue",
        "87CEEB" to "Sky Blue",
        "87CEFA" to "Light Sky Blue",
        "00BFFF" to "Deep Sky Blue",
        "1E90FF" to "Sky Blue",
        "6495ED" to "Cornflower Blue",
        "7B68EE" to "Medium Slate Blue",
        "FFC0CB" to "Pink",
        "FFB6C1" to "Light Pink",
        "FF69B4" to "Hot Pink",
        "FF1493" to "Deep Pink",
        "C71585" to "Medium Violet Red",
        "DB7093" to "Pale Violet Red",
        "FFA500" to "Orange",
        "FF8C00" to "Dark Orange",
        "FF4500" to "Orange Red",
        "FF6347" to "Tomato",
        "FF7F50" to "Coral",
        "FFFF00" to "Yellow",
        "FFD700" to "Gold",
        "FFFFE0" to "Light Yellow",
        "FFFACD" to "Lemon Chiffon",
        "FAFAD2" to "Light Goldenrod Yellow",
        "FFEFD5" to "Papaya Whip",
        "FFE4B5" to "Moccasin",
        "FFDAB9" to "Peach Puff",
        "EEE8AA" to "Pale Goldenrod",
        "F0E68C" to "Khaki",
        "BDB76B" to "Dark Khaki",
        "800080" to "Purple",
        "9400D3" to "Dark Violet",
        "9932CC" to "Dark Orchid",
        "8B008B" to "Dark Magenta",
        "E6E6FA" to "Lavender",
        "D8BFD8" to "Thistle",
        "DDA0DD" to "Plum",
        "EE82EE" to "Violet",
        "DA70D6" to "Orchid",
        "FF00FF" to "Magenta",
        "BA55D3" to "Medium Orchid",
        "9370DB" to "Medium Purple",
        "663399" to "Rebecca Purple",
        "8A2BE2" to "Blue Violet",
        "4B0082" to "Indigo",
        "6A5ACD" to "Slate Blue",
        "483D8B" to "Dark Slate Blue",
        "A52A2A" to "Brown",
        "8B4513" to "Saddle Brown",
        "FFF8DC" to "Cornsilk",
        "FFEBCD" to "Blanched Almond",
        "FFE4C4" to "Bisque",
        "FFDEAD" to "Navajo White",
        "F5DEB3" to "Wheat",
        "DEB887" to "Burly Wood",
        "D2B48C" to "Tan",
        "BC8F8F" to "Rosy Brown",
        "F4A460" to "Sandy Brown",
        "DAA520" to "Goldenrod",
        "B8860B" to "Dark Goldenrod",
        "CD853F" to "Peru",
        "D2691E" to "Chocolate",
        "A0522D" to "Sienna",
        "800000" to "Maroon",
        "FFFFFF" to "White",
        "FFFAFA" to "Snow",
        "F0FFF0" to "Honey Dew",
        "F5FFFA" to "Mint Cream",
        "F0FFFF" to "Azure",
        "F0F8FF" to "Alice Blue",
        "F8F8FF" to "Ghost White",
        "F5F5F5" to "White Smoke",
        "FFF5EE" to "Sea Shell",
        "F5F5DC" to "Beige",
        "FDF5E6" to "Old Lace",
        "FFFAF0" to "Floral White",
        "FFFFF0" to "Ivory",
        "FAEBD7" to "Antique White",
        "FAF0E6" to "Linen",
        "FFF0F5" to "Lavender Blush",
        "FFE4E1" to "Misty Rose",
        "808080" to "Gray",
        "696969" to "Dim Gray",
        "778899" to "Light Slate Gray",
        "708090" to "Slate Gray",
        "DCDCDC" to "Gainsboro",
        "D3D3D3" to "Light Gray",
        "C0C0C0" to "Silver",
        "A9A9A9" to "Dark Gray",
        "2F4F4F" to "Dark Slate Gray",
        "000000" to "Black",
    )
}