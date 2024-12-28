package com.example.amphibians.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.amphibians.R
import com.example.amphibians.model.Amphibian
import com.example.amphibians.ui.screens.AmphibianList
import com.example.amphibians.ui.screens.AmphibianListCat
import com.example.amphibians.ui.screens.AmphibiansUiState
import com.example.amphibians.ui.screens.AmphibiansViewModel
import com.example.amphibians.ui.screens.AmphibianDetailScreen
import com.example.amphibians.ui.screens.BottomBar
import com.example.amphibians.ui.screens.ErrorScreen
import com.example.amphibians.ui.screens.HomeScreen
import com.example.amphibians.ui.screens.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmphibiansApp() {
    val amphibiansViewModel: AmphibiansViewModel = viewModel(factory = AmphibiansViewModel.Factory)

    // State untuk menentukan layar yang ditampilkan
    val homeSelected = remember { mutableStateOf(true) }
    val selectedCategoryAmphibian = remember { mutableStateOf<Amphibian?>(null) }
    val selectedAmphibianDetail = remember { mutableStateOf<Amphibian?>(null) } // State untuk detail

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            /* Menyembunyikan TopAppBar saat berada di AmphibianDetailScreen, tetapi ada bug,
            jika dibukadari Home Screen (AmphibianListKu, maka TopAppBar akan tetap muncul.
            TopAppBar tidak muncul jika dibuka dari Category ->List berdasarkan nama (AmphibianListCat) */
            if (selectedAmphibianDetail.value == null) {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(R.string.app_name),
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                )
            }
        },
        bottomBar = {
            BottomBar(
                onHomeSelected = {
                    amphibiansViewModel.clearSelection() // Reset selectedAmphibian saat Home ditekan
                    homeSelected.value = true
                    selectedCategoryAmphibian.value = null // Reset state jika berpindah layar
                    selectedAmphibianDetail.value = null // Reset detail
                },
                onCategorySelected = {
                    homeSelected.value = false
                    selectedCategoryAmphibian.value = null // Reset state jika berpindah layar
                    selectedAmphibianDetail.value = null // Reset detail
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            when {
                homeSelected.value -> {
                    HomeScreen(
                        amphibiansUiState = amphibiansViewModel.amphibiansUiState,
                        selectedAmphibian = amphibiansViewModel.selectedAmphibian,
                        onAmphibianSelected = amphibiansViewModel::selectAmphibian,
                        onBack = amphibiansViewModel::clearSelection,
                        retryAction = amphibiansViewModel::getAmphibians,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = paddingValues,
                        onHomeSelected = { /* Tidak perlu aksi tambahan */ },
                        onCategorySelected = { homeSelected.value = false }
                    )
                }
                selectedAmphibianDetail.value != null -> {
                    // Menampilkan Detail Screen
                    AmphibianDetailScreen(
                        amphibian = selectedAmphibianDetail.value!!,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = paddingValues
                    )
                }
                selectedCategoryAmphibian.value != null -> {
                    AmphibianListCat(
                        selectedAmphibian = selectedCategoryAmphibian.value,
                        onAmphibianSelected = amphibiansViewModel::selectAmphibian,
                        onBack = { selectedCategoryAmphibian.value = null }, // Reset saat kembali
                        onAmphibianDetailSelected = { amphibian ->
                            selectedAmphibianDetail.value = amphibian // Arahkan ke detail
                        },
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = paddingValues
                    )
                }
                else -> {
                    when (val uiState = amphibiansViewModel.amphibiansUiState) {
                        is AmphibiansUiState.Success -> {
                            AmphibianList(
                                amphibians = uiState.amphibians,
                                onAmphibianSelected = {
                                    selectedCategoryAmphibian.value = it // Simpan amfibi yang dipilih
                                },
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = paddingValues
                            )
                        }
                        is AmphibiansUiState.Loading -> {
                            LoadingScreen(modifier = Modifier.fillMaxSize())
                        }
                        is AmphibiansUiState.Error -> {
                            ErrorScreen(retryAction = amphibiansViewModel::getAmphibians, modifier = Modifier.fillMaxSize())
                        }
                    }
                }
            }
        }
    }
}
