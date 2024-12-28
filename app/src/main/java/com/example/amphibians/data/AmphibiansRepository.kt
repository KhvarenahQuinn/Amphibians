package com.example.amphibians.data

import com.example.amphibians.model.Amphibian
import com.example.amphibians.network.AmphibiansApiService

/**
 * Interface repositori untuk mendapatkan data amfibi dari sumber data.
 */
interface AmphibiansRepository {
    /**Fungsi untuk mengambil daftar amfibi dari sumber data.

     */
    suspend fun getAmphibians(): List<Amphibian>
}

/**
 * Implementasi repositori untuk mengambil data amfibi dari API jaringan.
 */
class DefaultAmphibiansRepository(
    private val amphibiansApiService: AmphibiansApiService
) : AmphibiansRepository {
    /**
    * Mengambil daftar amfibi dari API menggunakan AmphibiansApiService. */
    override suspend fun getAmphibians(): List<Amphibian> = amphibiansApiService.getAmphibians()
}
