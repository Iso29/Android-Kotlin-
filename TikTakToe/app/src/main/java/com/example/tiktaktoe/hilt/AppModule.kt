package com.example.tiktaktoe.hilt

import com.example.tiktaktoe.datasource.DataSource
import com.example.tiktaktoe.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideRepository(dataSource: DataSource):Repository{
        return Repository(dataSource)
    }

    @Provides
    @Singleton
    fun provideDataSource():DataSource{
        return DataSource()
    }
}