package dk.nodes.generator.templating

import io.reactivex.Completable
import dk.nodes.generator.models.Constants
import java.io.File

/**
 * Class used for renaming the folder structure used to hold the application
 */
class DirectoryNameReplacer(
        var packageName: String,
        var source: File,
        var destination: File
) {

    fun replace(): Completable {
        return Completable.create {
            source.copyRecursively(destination, true)

            val splitDefaultPackageName = Constants.defaultPackageName.split("")
            val splitPackageName = packageName.split("")

            if (splitPackageName.size != 3 || splitDefaultPackageName.size != 3) {
                println("Invalid Package Name")
                it.onError(Exception("Invalid Package Name"))
                return@create
            }

            for (i in 0..2) {
                val defaultName = splitDefaultPackageName[i]
                val resultDirectories = searchForDirectory(defaultName)
                val resultName = splitPackageName[i]
                renameInPlace(resultName, resultDirectories)
            }

            it.onComplete()
        }
    }

    /**
     * Takes the old file name and turns into the new file name yaay
     */
    private fun renameInPlace(name: String, files: MutableList<File>) {
        for (file in files) {
            val newPath = File(file.parentFile, name)
            file.renameTo(newPath)
        }
    }

    /**
     * The root method for searching for directories with matching names
     * currently not case sensitive but...i don't think we need it to be
     */
    private fun searchForDirectory(name: String): MutableList<File> {
        val fileList: MutableList<File> = arrayListOf()

        recursiveNameSearch(name, destination, fileList)

        return fileList
    }

    /**
     * The recursive method that the container method uses to search for folders matching the structure name
     * there might be a better way to do this
     */
    private fun recursiveNameSearch(name: String, file: File, fileList: MutableList<File>) {
        if (file.name.equals(name, true)) {
            fileList.add(file)
        }

        if (file.isDirectory) {
            val files = file.listFiles()

            for (f in files) {
                recursiveNameSearch(name, f, fileList)
            }
        }
    }
}