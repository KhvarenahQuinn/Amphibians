package com.example.amphibians.network

import com.example.amphibians.model.Amphibian
import retrofit2.http.GET

/**
 * Interface untuk API service Amphibians.
 *
 * @GET("amphibians") Mengambil daftar amfibi dari endpoint server.
 */
interface AmphibiansApiService {
    @GET("amphibians")
    suspend fun getAmphibians(): List<Amphibian>
}
