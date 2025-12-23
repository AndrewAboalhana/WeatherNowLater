package com.aa.weathernowlater.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aa.common.theme.Dimens
import com.aa.current_weather.composables.CurrentWeatherSection
import com.aa.current_weather.viewmodel.CurrentWeatherUiState
import com.aa.current_weather.viewmodel.CurrentWeatherViewModel
import com.aa.forecast.composables.ForecastItem
import com.aa.forecast.viewmodel.ForecastIntentAction
import com.aa.forecast.viewmodel.ForecastUiState
import com.aa.forecast.viewmodel.ForecastViewModel
import com.aa.search.composables.SearchSection
import com.aa.search.viewmodel.SearchUiState
import com.aa.search.viewmodel.SearchViewModel

@Composable
fun HomeScreen(
    searchViewModel: SearchViewModel = hiltViewModel(),
    currentWeatherViewModel: CurrentWeatherViewModel = hiltViewModel(),
    forecastViewModel: ForecastViewModel = hiltViewModel()
) {
    val searchState by searchViewModel.state.collectAsStateWithLifecycle()
    val currentWeatherState by currentWeatherViewModel.state.collectAsStateWithLifecycle()
    val forecastState by forecastViewModel.state.collectAsStateWithLifecycle()

    HomeScreenContent(
        searchState = searchState,
        currentWeatherState = currentWeatherState,
        forecastState = forecastState,
        onQueryChange = searchViewModel::onQueryChange,
        onForecastAction = forecastViewModel::onAction
    )
}

@Composable
fun HomeScreenContent(
    searchState: SearchUiState,
    currentWeatherState: CurrentWeatherUiState,
    forecastState: ForecastUiState,
    onQueryChange: (String) -> Unit,
    onForecastAction: (ForecastIntentAction) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Dimens.space16),
            contentPadding = PaddingValues(bottom = Dimens.space24),
            verticalArrangement = Arrangement.spacedBy(Dimens.space16)
        ) {

            item {
                Spacer(modifier = Modifier.height(Dimens.space24))
                SearchSection(state = searchState, onQueryChange = onQueryChange)
            }

            item {
                CurrentWeatherSection(state = currentWeatherState)
            }

            item {
                Text(
                    text = "7-Day Forecast",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = Dimens.space8)
                )
            }

            if (forecastState.isLoading) {
                item { LinearProgressIndicator(modifier = Modifier.fillMaxWidth()) }
            } else {
                items(forecastState.forecastList) { weather ->
                    ForecastItem(weather = weather)
                }
            }

            if (forecastState.errorMessage.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.space16),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = forecastState.errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(bottom = Dimens.space8)
                        )

                        Button(
                            onClick = {
                                val currentCity = currentWeatherState.weatherInfo?.cityName
                                if (currentCity != null) {
                                    onForecastAction(ForecastIntentAction.OnLoadForecast(currentCity))
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            )
                        ) {
                            Text(text = "Retry")
                        }
                    }
                }
            }
        }
    }
}