package templating

import io.reactivex.Completable
import models.Constants
import org.eclipse.jgit.api.Git
import java.io.File

class GitCloner(private val source: File) {
    fun clone(): Completable {

        return Completable.create {
            try {
                Git.cloneRepository()
                        .setURI(Constants.baseRepositoryUrl)
                        .setDirectory(source)
                        .call()

            } catch (e: Exception) {
                it.onError(e)
            }

            it.onComplete()
        }
    }
}