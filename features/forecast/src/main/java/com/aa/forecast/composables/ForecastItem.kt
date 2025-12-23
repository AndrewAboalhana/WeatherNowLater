package com.aa.forecast.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.aa.common.theme.Dimens
import com.aa.domain.models.WeatherInfo
import com.aa.weather_lib.WeatherFormatter

@Composable
fun ForecastItem(weather: WeatherInfo) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.elevation),
        shape = RoundedCornerShape(Dimens.cornerRadius),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.space16, vertical = Dimens.space12),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = WeatherFormatter.formatForecastDate(weather.date),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )


            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = weather.iconUrl,
                    contentDescription = weather.conditionText,
                    modifier = Modifier.size(Dimens.iconSizeMedium),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(Dimens.space4))
                Text(
                    text = weather.conditionText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "${weather.temperature}°",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(0.5f),
                textAlign = androidx.compose.ui.text.style.TextAlign.End
            )
        }
    }
}