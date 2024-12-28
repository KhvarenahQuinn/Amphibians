package com.example.amphibians.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.amphibians.R
import com.example.amphibians.model.Amphibian
import com.example.amphibians.ui.theme.AmphibiansTheme
/**
 * Fungsi home screen yang menampilkan status UI sesuai dengan data yang ada.
 *
 * @param amphibiansUiState Status UI yang sedang berlangsung.
 * @param retryAction Fungsi yang dijalankan saat mencoba kembali (retry).
 */
@Composable
fun HomeScreen(
    amphibiansUiState: AmphibiansUiState,
    selectedAmphibian: Amphibian?,
    onHomeSelected: () -> Unit,
    onCategorySelected: () -> Unit,
    onAmphibianSelected: (Amphibian) -> Unit,
    onBack: () -> Unit,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    if (selectedAmphibian != null) {
        AmphibianDetailScreen(
            amphibian = selectedAmphibian,
            modifier = modifier
        )
    } else {
        when (amphibiansUiState) {
            is AmphibiansUiState.Loading -> LoadingScreen(modifier.size(200.dp))// Menampilkan screen loading
            is AmphibiansUiState.Success ->
                AmphibianListKu(
                    amphibians = amphibiansUiState.amphibians,
                    onAmphibianSelected = onAmphibianSelected,
                    modifier = modifier.padding(
                        start = dimensionResource(R.dimen.padding_medium),
                        top = dimensionResource(R.dimen.padding_medium),
                        end = dimensionResource(R.dimen.padding_medium)
                    ),
                    contentPadding = contentPadding
                )

            else -> ErrorScreen(retryAction, modifier) // Menampilkan error screen jika gagal
        }
//        BottomBar(
//            onHomeSelected = onHomeSelected,
//            onCategorySelected = onCategorySelected,
//            modifier = Modifier.fillMaxWidth()
//        )
    }
}
@Composable
fun BottomBar(
    onHomeSelected: () -> Unit,
    onCategorySelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
//            .padding(8.dp) // Padding agar tidak terlalu rapat ke tepi
            .background(MaterialTheme.colorScheme.primary), // Memberikan background sesuai dengan tema
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Tombol Home
        Button(
            onClick = onHomeSelected,
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_home),
                contentDescription = stringResource(R.string.home),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.home))
        }

        // Tombol Category
        Button(
            onClick = onCategorySelected,
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_categories),
                contentDescription = stringResource(R.string.category),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.category))
        }
    }
}


@Composable
fun AmphibianListKu(
    amphibians: List<Amphibian>,
    onAmphibianSelected: (Amphibian) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(amphibians + amphibians + amphibians) { amphibian ->
            AmphibianCard(
                amphibian = amphibian,
                modifier = Modifier
                    .clickable { onAmphibianSelected(amphibian) }
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}


@Composable
fun AmphibianDetailScreen(
    amphibian: Amphibian,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp, top = 24.dp) // Margin sekitar kolom
            .verticalScroll(rememberScrollState()) // Agar konten bisa digulir
    ) {
        // Nama Item
        Text(
            text = amphibian.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp) // Padding bawah
        )

        // Gambar Item
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(bottom = 16.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(amphibian.imgSrc)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.small),
                contentScale = ContentScale.Crop
            )
        }
        // Type Item
        Text(
            text = amphibian.type,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        // Deskripsi Item
        Text(
            text = amphibian.description,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 14.dp)
        )
    }
}






@Composable
fun AmphibianList(
    amphibians: List<Amphibian>,
    onAmphibianSelected: (Amphibian) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(amphibians) { amphibian ->
            Text(
                text = amphibian.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAmphibianSelected(amphibian) }
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}

@Composable
fun AmphibianListCat(
    selectedAmphibian: Amphibian?,
    onAmphibianSelected: (Amphibian) -> Unit,
    onAmphibianDetailSelected: (Amphibian) -> Unit, // Parameter baru
    onBack: () -> Unit, // Tambahkan parameter untuk aksi kembali
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    // Periksa apakah ada amphibian yang dipilih
    if (selectedAmphibian != null) {
        val repeatedAmphibians = List(20) { selectedAmphibian }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier,
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(repeatedAmphibians) { amphibian ->
                AmphibianCard(
                    amphibian = amphibian,
                    modifier = Modifier
                        .clickable { onAmphibianDetailSelected(amphibian) }
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
        }
    } else {
        Text(
            text = stringResource(R.string.no_selection),
            modifier = modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


/**
 * Menampilkan pesan pemuatan saat data sedang dimuat.
 */
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading),
        modifier = modifier
    )
}

/**
 * Menampilkan pesan kesalahan dengan tombol untuk mencoba kembali.
 *
 * @param retryAction Fungsi yang dijalankan untuk mencoba kembali.
 */
@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.loading_failed))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}
/**
 * Menampilkan informasi amfibi dalam bentuk card.
 *
 * @param amphibian Data amfibi yang akan ditampilkan.
 */
@Composable
fun AmphibianCard(amphibian: Amphibian, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .heightIn(250.dp)
        ,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Menampilkan nama dengan maxLines dan overflow
            Text(
                text = stringResource(R.string.amphibian_title, amphibian.name, amphibian.type),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_medium)),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                maxLines = 1, // Membatasi nama hanya 1 baris
                overflow = TextOverflow.Ellipsis // Menambahkan titik-titik jika teks terlalu panjang
            )
            AsyncImage(
                modifier = Modifier.fillMaxWidth(),
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(amphibian.imgSrc)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                error = painterResource(id = R.drawable.ic_broken_image),
                placeholder = painterResource(id = R.drawable.loading_img)
            )
        }
    }
}


/**
 * Menampilkan daftar amfibi dalam bentuk LazyColumn.
 *
 * @param amphibians Daftar data amfibi yang akan ditampilkan.
 */
@Composable
private fun AmphibiansListScreen(
    amphibians: List<Amphibian>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(
            items = amphibians,
            key = { amphibian ->
                amphibian.name
            }
        ) { amphibian ->
            AmphibianCard(amphibian = amphibian, modifier = Modifier.fillMaxSize())
        }
    }
}

