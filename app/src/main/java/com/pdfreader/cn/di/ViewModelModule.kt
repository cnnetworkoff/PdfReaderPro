package com.pdfreader.cn.di

import com.pdfreader.cn.presentation.screens.folder.FolderDetailViewModel
import com.pdfreader.cn.presentation.screens.home.HomeViewModel
import com.pdfreader.cn.presentation.screens.onboarding.OnboardingViewModel
import com.pdfreader.cn.presentation.screens.reader.ReaderViewModel
import com.pdfreader.cn.presentation.screens.search.SearchViewModel
import com.pdfreader.cn.presentation.screens.settings.SettingsViewModel
import com.pdfreader.cn.presentation.screens.tools.compress.CompressViewModel
import com.pdfreader.cn.presentation.screens.tools.merge.MergeViewModel
import com.pdfreader.cn.presentation.screens.tools.lock.LockViewModel
import com.pdfreader.cn.presentation.screens.tools.reorder.ReorderViewModel
import com.pdfreader.cn.presentation.screens.tools.unlock.UnlockViewModel
import com.pdfreader.cn.presentation.screens.tools.removepages.RemovePagesViewModel
import com.pdfreader.cn.presentation.screens.tools.rotate.RotateViewModel
import com.pdfreader.cn.presentation.screens.tools.watermark.WatermarkViewModel
import com.pdfreader.cn.presentation.screens.tools.pagenumbers.PageNumbersViewModel
import com.pdfreader.cn.presentation.screens.tools.imagetopdf.ImageToPdfViewModel
import com.pdfreader.cn.presentation.screens.tools.pdftoimage.PdfToImageViewModel
import com.pdfreader.cn.presentation.screens.tools.split.SplitViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::FolderDetailViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::OnboardingViewModel)
    viewModel { params -> ReaderViewModel(get(), get(), get(), get(), androidContext(), params.get()) }
    viewModel { MergeViewModel(get(), androidContext()) }
    viewModel { SplitViewModel(get(), androidContext()) }
    viewModel { CompressViewModel(get(), androidContext()) }
    viewModel { RotateViewModel(get(), androidContext()) }
    viewModel { ReorderViewModel(get(), androidContext()) }
    viewModel { LockViewModel(get(), androidContext()) }
    viewModel { UnlockViewModel(get(), androidContext()) }
    viewModel { RemovePagesViewModel(get(), androidContext()) }
    viewModel { WatermarkViewModel(get(), androidContext()) }
    viewModel { PageNumbersViewModel(get(), androidContext()) }
    viewModel { ImageToPdfViewModel(get(), androidContext()) }
    viewModel { PdfToImageViewModel(get(), androidContext()) }
}
