object Loader {
    private const val MAX_BLOCK_CHAR = 10

    fun updateProgress(total: Int, progress: Int) {
        val ratio = progress.toDouble() / total.toDouble()
        val current = (ratio * MAX_BLOCK_CHAR).toInt()
        val rest = MAX_BLOCK_CHAR - current

        print("\r█")
        repeat(current) { print("░") }
        repeat(rest) { print(" ") }
        print("█")
    }
}
