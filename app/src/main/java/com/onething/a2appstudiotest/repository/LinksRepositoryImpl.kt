package com.onething.a2appstudiotest.repository

import com.onething.a2appstudiotest.exception.DataException
import com.onething.a2appstudiotest.model.Links
import com.onething.a2appstudiotest.utils.Either
import com.onething.a2appstudiotest.utils.removeFirstSlash
import com.onething.a2appstudiotest.utils.removeLastSlash
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import org.jsoup.Jsoup
import javax.inject.Inject


class LinksRepositoryImpl @Inject constructor() : LinksRepository {

    override suspend fun getLinks(url: String): Either<DataException, Links> {
        return try {
            val document = withContext(Dispatchers.IO) {
                Jsoup.connect(url).get()
            }
            val title = document.title()
            var imageElement = document.head().select("link[href~=.*\\.(ico|png)]")
            if (imageElement.isNullOrEmpty()) {
                Either.Right(Links(url, title, "${url.removeLastSlash()}/favicon.ico"))
            } else {
                val urlImage = imageElement.first().attr("href")
                if (urlImage.startsWith("http://") || urlImage.startsWith("https://")) {
                    Either.Right(Links(url, title, urlImage))
                } else {
                    Either.Right(Links(url, title, "${url.removeLastSlash()}/${urlImage.removeFirstSlash()}"))
                }
            }
        } catch (e: Exception) {
            if (e is IOException) {
                Either.Left(DataException.Network)
            } else {
                Either.Left(DataException.Conversion)
            }
        }
    }

}