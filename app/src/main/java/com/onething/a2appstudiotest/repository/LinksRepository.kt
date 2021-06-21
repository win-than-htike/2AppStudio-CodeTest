package com.onething.a2appstudiotest.repository

import com.onething.a2appstudiotest.exception.DataException
import com.onething.a2appstudiotest.model.Links
import com.onething.a2appstudiotest.utils.Either

interface LinksRepository {

    suspend fun getLinks(url: String) : Either<DataException, Links>

}