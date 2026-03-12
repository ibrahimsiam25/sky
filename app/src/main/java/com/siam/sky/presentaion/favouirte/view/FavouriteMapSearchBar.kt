package com.siam.sky.presentaion.favouirte.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.siam.sky.R
import com.siam.sky.core.ResponseState
import com.siam.sky.data.models.CityModel
import com.siam.sky.data.models.CityResponse
import com.siam.sky.ui.theme.CardBg
import com.siam.sky.ui.theme.HourCardSelected
import com.siam.sky.ui.theme.NavStroke
import com.siam.sky.ui.theme.WhiteFaded

@Composable
fun FavouriteMapSearchBar(
    query: String,
    searchState: ResponseState<CityResponse>,
    onQueryChange: (String) -> Unit,
    onCitySelected: (CityModel) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = {
                Text(
                    text = stringResource(R.string.fav_search_hint),
                    color = WhiteFaded,
                    fontSize = 14.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                    tint = WhiteFaded
                )
            },
            trailingIcon = {
                if (searchState is ResponseState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = HourCardSelected,
                unfocusedBorderColor = NavStroke,
                cursorColor = Color.White,
                focusedContainerColor = CardBg,
                unfocusedContainerColor = CardBg,
            ),
            modifier = Modifier.fillMaxWidth()
        )


        when {
            searchState is ResponseState.Success -> {
                val cities = searchState.data
                if (cities.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 220.dp)
                            .background(
                                CardBg,
                                RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                            )
                    ) {
                        items(cities) { city ->
                            CityResultItem(
                                city = city,
                                onClick = {
                                    focusManager.clearFocus()
                                    onCitySelected(city)
                                }
                            )
                            HorizontalDivider(color = WhiteFaded.copy(alpha = 0.15f))
                        }
                    }
                } else {
                    EmptyDropdown()
                }
            }

            searchState is ResponseState.Error -> {
                EmptyDropdown()
            }
        }
    }
}

@Composable
private fun CityResultItem(city: CityModel, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = city.name, color = Color.White, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = city.country, color = WhiteFaded, fontSize = 12.sp)
        }
    }
}

@Composable
private fun EmptyDropdown() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardBg, RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.fav_search_no_results),
            color = WhiteFaded,
            fontSize = 14.sp
        )
    }
}
