package models

interface ProjectGeneratorListener {
    fun onSuccess()
    fun onError(error: Throwable)
}