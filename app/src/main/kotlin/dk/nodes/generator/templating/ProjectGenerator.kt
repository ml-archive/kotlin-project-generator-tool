package dk.nodes.generator.templating

import io.reactivex.schedulers.Schedulers
import javafx.application.Platform
import dk.nodes.generator.models.ProjectGeneratorListener
import java.io.File
import java.util.concurrent.TimeUnit


class ProjectGenerator(private val listener: ProjectGeneratorListener) {
    var packageName: String = "com.example.project"
    // If our out folder does not exist we should create it
    val source = File("./projectSource")
        get() {
            field.deleteRecursively()
            field.mkdirs()
            return field
        }
    var destination = File("./project")
        get() {
            field.deleteRecursively()
            field.mkdirs()
            return field
        }
    private val gitCloner = GitCloner(source)
    private val directoryNameReplacer = DirectoryNameReplacer(packageName, source, destination)
    private val packageNameReplacer = PackageNameReplacer(packageName, destination)

    fun generate() {
        runGitCloner()
    }

    private fun runGitCloner() {
        gitCloner.clone()
                .andThen(packageNameReplacer.replace())
                .andThen(directoryNameReplacer.replace())
                .delay(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(
                        {
                            println("generate: Success")
                            Platform.runLater {
                                listener.onSuccess()
                            }
                        },
                        {
                            println("generate: Error")
                            it.printStackTrace()
                            Platform.runLater {
                                listener.onError(it)
                            }
                        }
                )
    }
}