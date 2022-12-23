package com.example.foodsapp.di

import com.example.foodsapp.data.datasourse.DataSource
import com.example.foodsapp.data.repository.Repository
import com.example.foodsapp.retrofit.FoodDao
import com.example.foodsapp.retrofit.RetrofitUtil
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
    fun provideFoodDao():FoodDao{
        return RetrofitUtil.getFoodDao()
    }

    @Provides
    @Singleton
    fun provideDataSource(foodDao: FoodDao):DataSource{
        return DataSource(foodDao)
    }

    @Provides
    @Singleton
    fun provideRepository(dataSource: DataSource):Repository{
        return  Repository(dataSource)
    }



}