package com.siam.sky.presentaion.favouirte.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.siam.sky.R
import com.siam.sky.core.common.Background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.siam.sky.core.db.WeatherDataBase
import com.siam.sky.data.datasources.local.FavouriteLocalDataSource
import com.siam.sky.data.datasources.local.WeatherLocalDataSource
import com.siam.sky.data.datasources.remote.WeatherRemoteDataSource
import com.siam.sky.core.network.NetworkMonitor
import com.siam.sky.data.models.FavouriteLocationEntity
import com.siam.sky.data.repo.WeatherRepo
import com.siam.sky.presentaion.favouirte.viewmodel.FavouriteViewModel

@Composable
fun FavouriteView(
    onNavigateToMap: () -> Unit,
    onNavigateToDetails: (Double, Double) -> Unit
) {
    val context = LocalContext.current
    val database = WeatherDataBase.getInstance(context)
    val networkMonitor = NetworkMonitor(context)
    val viewModel: FavouriteViewModel = viewModel(
        factory = FavouriteViewModel.factory(
            WeatherRepo(
                WeatherRemoteDataSource(),
                WeatherLocalDataSource(database.getWeatherDao()),
                FavouriteLocalDataSource(database.getFavouriteLocationDao()),
                networkMonitor
            )
        )
    )
    val favourites by viewModel.favouritesState.collectAsState()
    var pendingDelete by remember { mutableStateOf<FavouriteLocationEntity?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Background()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Text(
                text = stringResource(R.string.nav_favourite),
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            FavouriteLocationsList(
                favourites = favourites,
                onCardClick = { onNavigateToDetails(it.lat, it.lon) },
                onDeleteClick = { pendingDelete = it },
                modifier = Modifier.weight(1f)
            )
        }

        FloatingActionButton(
            onClick = onNavigateToMap,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 80.dp, end = 20.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        ) {
            Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.add_alert))
        }

        pendingDelete?.let { location ->
            DeleteFavouriteDialog(
                onDismiss = { pendingDelete = null },
                onConfirm = {
                    viewModel.deleteFavourite(location.id)
                    pendingDelete = null
                }
            )
        }
    }
}

