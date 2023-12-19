object Loader {
    fun start() {
        for (i in 0..100) { // to test, taken from Aleksandar (slight modification)
            testProgress(100, i)
            try {
                Thread.sleep(30)
            } catch (e: InterruptedException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
        }
    }

    private fun testProgress(all: Int, now: Int) {
        // █▓▒░
        val MAX_PIPE_CHAR = 10
        val num = now * MAX_PIPE_CHAR * 1.01f // 1.01f to account for any round off
        val current = (num / all).toInt()
        val rest = MAX_PIPE_CHAR - current
        print("\r█")
        for (a in 1..current) {
            print("░")
        }
        for (b in 1..rest) {
            print(" ")
        }
        print("█")
    }
}