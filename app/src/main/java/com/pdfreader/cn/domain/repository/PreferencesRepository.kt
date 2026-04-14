package com.pdfreader.cn.domain.repository

import com.pdfreader.cn.domain.model.AppPreferences
import com.pdfreader.cn.domain.model.QuickZoomPreset
import com.pdfreader.cn.domain.model.ReadingTheme
import com.pdfreader.cn.domain.model.ScreenOrientation
import com.pdfreader.cn.domain.model.ScrollMode
import com.pdfreader.cn.domain.model.SortOption
import com.pdfreader.cn.domain.model.ThemeMode
import com.pdfreader.cn.domain.model.UpdateCheckInterval
import com.pdfreader.cn.domain.model.ViewMode
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val preferences: Flow<AppPreferences>

    // App settings
    suspend fun setFirstLaunch(isFirst: Boolean)
    suspend fun setOnboardingCompleted(completed: Boolean)
    suspend fun setThemeMode(mode: ThemeMode)
    suspend fun setDefaultViewMode(mode: ViewMode)
    suspend fun setDefaultSortOption(option: SortOption)
    suspend fun setRememberPasswords(enabled: Boolean)
    suspend fun setUpdateCheckInterval(interval: UpdateCheckInterval)

    // Reader settings
    suspend fun setReaderBrightness(brightness: Float)
    suspend fun setReaderScrollMode(mode: ScrollMode)
    suspend fun setReaderAutoHideToolbar(enabled: Boolean)
    suspend fun setReaderQuickZoomPreset(preset: QuickZoomPreset)
    suspend fun setReaderKeepScreenOn(enabled: Boolean)
    suspend fun setReaderTheme(theme: ReadingTheme)
    suspend fun setReaderSnapToPages(enabled: Boolean)
    suspend fun setReaderScreenOrientation(orientation: ScreenOrientation)
}
