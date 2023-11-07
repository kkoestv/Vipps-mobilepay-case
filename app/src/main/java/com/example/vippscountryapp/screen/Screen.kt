package com.example.vippscountryapp.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.vippscountryapp.viewmodel.UiState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vippscountryapp.R
import com.example.vippscountryapp.network.CountryName
import com.example.vippscountryapp.viewmodel.AppViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunApp(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) {
        Surface(
            modifier = Modifier
                .background(colorResource(id = R.color.light_blue_background))
                .fillMaxSize()
                .padding(it),
        ) {
            val viewModel: AppViewModel = viewModel()
            HomeScreen(
                viewModel = viewModel,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.light_blue_background)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        CountrySearchComponent(modifier, viewModel) { query ->
            viewModel.searchCountry(query)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 60.dp, end = 60.dp, top = 5.dp)
                .height(160.dp)
                .background(
                    colorResource(id = R.color.vipps_blue),
                    shape = RoundedCornerShape(10.dp)
                ),

        ) {
            when (viewModel.uiState) {
                is UiState.Loading -> LoadingComponent(modifier)
                is UiState.Success -> {
                    if (viewModel.searchResults.isNotEmpty()) {
                        ResultComponent(viewModel.searchResults, modifier)
                    } else {
                        ResultComponent((viewModel.uiState as UiState.Success).countryName, modifier)
                    }
                }
                is UiState.Error -> ErrorComponent(modifier)
            }
        }
    }
}



@Composable
fun LoadingComponent(modifier: Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(text = "")

    }
}

@Composable
fun ErrorComponent(modifier: Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(text = "Error fetching data.")
    }
}

@Composable
fun ResultComponent(uiState: List<CountryName>, modifier: Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            uiState.forEach { countryName ->
                CountryInfoItem(countryName, modifier)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun CountryInfoItem(countryName: CountryName, modifier: Modifier) {
    var textFieldState by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "Country name: ${countryName.name}",
                color = Color.White,

            )
            Text(
                text = "Capital: ${countryName.capital}",
                color = Color.White,

            )
            Text(
                text = "Number of alternative Spellings: ${countryName.altSpellings.size}",
                color = Color.White,
            )
        }
    }
}

@Composable
fun CountrySearchComponent(modifier: Modifier, viewModel: AppViewModel, onSearch: (String) -> Unit) {
    var textFieldState by remember { mutableStateOf("") }

    TextField (
        value = textFieldState,
        onValueChange = {textFieldState = it},
        label = { Text (text = "Search for a country")},
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.textFieldColors(

            textColor = Color.DarkGray,
            disabledTextColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
    
    Spacer(modifier = Modifier.size(5.dp))

    Button(
        onClick = { onSearch(textFieldState) },
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.width(280.dp),
        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.vipps_orange)))

     {
        Text("Search")
    }
}

