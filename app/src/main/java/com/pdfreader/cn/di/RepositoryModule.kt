package com.pdfreader.cn.di

import com.pdfreader.cn.data.repository.FavoriteRepositoryImpl
import com.pdfreader.cn.data.repository.PdfFileRepositoryImpl
import com.pdfreader.cn.data.repository.PdfToolsRepositoryImpl
import com.pdfreader.cn.data.repository.PreferencesRepositoryImpl
import com.pdfreader.cn.data.repository.RecentRepositoryImpl
import com.pdfreader.cn.data.repository.UpdateRepositoryImpl
import com.pdfreader.cn.domain.repository.FavoriteRepository
import com.pdfreader.cn.domain.repository.PdfFileRepository
import com.pdfreader.cn.domain.repository.PdfToolsRepository
import com.pdfreader.cn.domain.repository.PreferencesRepository
import com.pdfreader.cn.domain.repository.RecentRepository
import com.pdfreader.cn.domain.repository.UpdateRepository
import com.pdfreader.cn.util.ApkDownloadManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single<PdfFileRepository> { PdfFileRepositoryImpl(androidContext()) }
    single<RecentRepository> { RecentRepositoryImpl(get()) }
    single<FavoriteRepository> { FavoriteRepositoryImpl(get()) }
    single<PreferencesRepository> { PreferencesRepositoryImpl(get()) }
    single<PdfToolsRepository> { PdfToolsRepositoryImpl(androidContext()) }
    single<UpdateRepository> { UpdateRepositoryImpl(get(), get()) }
    single { ApkDownloadManager(androidContext()) }
}
