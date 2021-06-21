package com.onething.a2appstudiotest.di

import com.onething.a2appstudiotest.repository.LinksRepository
import com.onething.a2appstudiotest.repository.LinksRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideLinksRepository(
        linksRepositoryImpl: LinksRepositoryImpl
    ) : LinksRepository

}