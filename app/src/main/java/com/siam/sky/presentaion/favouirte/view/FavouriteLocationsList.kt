package com.siam.sky.presentaion.favouirte.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.siam.sky.R
import com.siam.sky.data.models.FavouriteLocationEntity

@Composable
 fun FavouriteLocationsList(
    favourites: List<FavouriteLocationEntity>,
    onCardClick: (FavouriteLocationEntity) -> Unit,
    onDeleteClick: (FavouriteLocationEntity) -> Unit,
    modifier: Modifier = Modifier.Companion
) {
    if (favourites.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Companion.Center) {
            Text(
                text = stringResource(R.string.favourite_empty),
                color = Color.Companion.White.copy(alpha = 0.7f),
                fontSize = 16.sp
            )
        }
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(favourites, key = { it.id }) { item ->
            FavouriteLocationCard(
                item = item,
                onClick = { onCardClick(item) },
                onDelete = { onDeleteClick(item) }
            )
        }
    }
}