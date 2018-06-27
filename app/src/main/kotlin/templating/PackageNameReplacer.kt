package templating

import io.reactivex.Completable
import models.Constants
import util.endsWith
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Class used for replacing the package names within the app files
 */
class PackageNameReplacer(
        var packageName: String,
        var destination: File
) {
    /**
     * How we find all files that we should search and replace our instance names
     */

    fun replace(): Completable {
        return Completable.create {
            val fileList: MutableList<File> = arrayListOf()

            recursiveEndingSearch(destination, fileList)

            for (file in fileList) {
                println("Replacing: ${file.absoluteFile}")
                replaceInstancesInFile(file)
            }

            it.onComplete()
        }
    }

    private fun recursiveEndingSearch(file: File, fileList: MutableList<File>) {
        if (file.endsWith(Constants.replaceFileTypes)) {
            fileList.add(file)
        }

        if (file.isDirectory) {
            val files = file.listFiles()

            for (f in files) {
                recursiveEndingSearch(f, fileList)
            }
        }
    }

    /**
     * Used to find instances of where our old package name exists and replace it using the new package name
     */

    private fun replaceInstancesInFile(file: File) {
        val path = Paths.get(file.toURI())
        val charset = StandardCharsets.UTF_8

        var content = String(Files.readAllBytes(path), charset)
        content = content.replace(Constants.defaultPackageName.toRegex(), packageName)
        Files.write(path, content.toByteArray(charset))
    }
}