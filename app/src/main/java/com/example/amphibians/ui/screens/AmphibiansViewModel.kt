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
    var selectedAmphibian: Amphibian? by mutableStateOf(null)
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

    fun onHomeSelected() {
        // Reset selectedAmphibian menjadi null ketika tombol home dipilih
        selectedAmphibian = null
    }
    /**
     * Menyeting amfibi yang dipilih.
     * Digunakan untuk memilih amfibi dari daftar.
     */
    fun selectAmphibian(amphibian: Amphibian) {
        selectedAmphibian = amphibian
    }
    /**
     * Menghapus pemilihan amfibi.
     * Digunakan untuk membatalkan pemilihan amfibi.
     */
    fun clearSelection() {
        selectedAmphibian = null
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
