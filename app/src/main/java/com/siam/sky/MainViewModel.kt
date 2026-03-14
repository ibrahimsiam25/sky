package com.siam.sky

import com.siam.sky.core.helper.AppLanguage
import com.siam.sky.data.repo.UserRepo

class MainViewModel(
    private val userRepo: UserRepo
) {

    fun getSavedAppLanguage(): AppLanguage {
        return userRepo.getSavedAppLanguage()
    }
}