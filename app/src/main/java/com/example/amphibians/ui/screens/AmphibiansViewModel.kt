/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.amphibians.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.amphibians.AmphibiansApplication
import com.example.amphibians.data.AmphibiansRepository
import com.example.amphibians.model.Amphibian
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * UI untuk merepresentasikan berbagai status UI pada layar.
 */
sealed interface AmphibiansUiState {
    /**
     * Status UI jika data berhasil dimuat dengan daftar amfibi.
     */
    data class Success(val amphibians: List<Amphibian>) : AmphibiansUiState
    /**
     * Status UI jika terjadi kesalahan saat memuat data.
     */
    object Error : AmphibiansUiState
    /**
     * Status UI saat data sedang dimuat.
     */
    object Loading : AmphibiansUiState
}

/**
 * ViewModel untuk mengelola data dan logika UI terkait amfibi.
 *
 * @param amphibiansRepository Repositori yang digunakan untuk mengambil data amfibi.
 */
class AmphibiansViewModel(private val amphibiansRepository: AmphibiansRepository) : ViewModel() {
    /**
     * Status UI saat ini, dimulai dengan status `Loading`.
     */
    var amphibiansUiState: AmphibiansUiState by mutableStateOf(AmphibiansUiState.Loading)
        private set

    init {
        getAmphibians()
    }
    /**
     * Mengambil data amfibi dari repositori dan memperbarui status UI.
     */
    fun getAmphibians() {
        viewModelScope.launch {
            amphibiansUiState = AmphibiansUiState.Loading
            amphibiansUiState = try {
                AmphibiansUiState.Success(amphibiansRepository.getAmphibians())
            } catch (e: IOException) {
                AmphibiansUiState.Error
            } catch (e: HttpException) {
                AmphibiansUiState.Error
            }
        }
    }

    /**
     * Factory untuk [AmphibiansViewModel] yang menerima repositori sebagai dependensi.
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as AmphibiansApplication)
                val amphibiansRepository = application.container.amphibiansRepository
                AmphibiansViewModel(amphibiansRepository = amphibiansRepository)
            }
        }
    }
}
