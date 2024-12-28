

package com.example.amphibians.data

import com.example.amphibians.network.AmphibiansApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

/**
 * Interface untuk Dependency Injection container di tingkat aplikasi.
 */
interface AppContainer {
    /* Menyediakan instance dari AmphibiansRepository untuk digunakan di seluruh aplikasi. */

    val amphibiansRepository: AmphibiansRepository
}

/**
 * Implementasi DefaultAppContainer menggunakan Retrofit untuk menyediakan layanan API.
 */
class DefaultAppContainer : AppContainer {
    private val BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com/"

    /**
     * Retrofit digunakan untuk membuat objek layanan API dengan konfigurasi serialization JSON.
     */
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    /**
     * Objek layanan API dibuat secara malas (lazy) untuk efisiensi.
     */
    private val retrofitService: AmphibiansApiService by lazy {
        retrofit.create(AmphibiansApiService::class.java)
    }

    /**
     * Menyediakan instance AmphibiansRepository dengan implementasi DefaultAmphibiansRepository.
     */
    override val amphibiansRepository: AmphibiansRepository by lazy {
        DefaultAmphibiansRepository(retrofitService)
    }
}
