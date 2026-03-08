package com.siam.sky

import android.content.Context
import com.siam.sky.core.helper.AppLanguage
import com.siam.sky.data.datasources.local.UserLocalDataSource
import com.siam.sky.data.repo.UserRepo

class MainViewModel(
    private val userRepo: UserRepo
) {

    fun getSavedAppLanguage(): AppLanguage {
        return userRepo.getSavedAppLanguage()
    }

    companion object {
        fun create(context: Context): MainViewModel {
            return MainViewModel(UserRepo(UserLocalDataSource(context)))
        }
    }
}