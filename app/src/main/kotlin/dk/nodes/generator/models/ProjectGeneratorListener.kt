package dk.nodes.generator.models

interface ProjectGeneratorListener {
    fun onSuccess()
    fun onError(error: Throwable)
}