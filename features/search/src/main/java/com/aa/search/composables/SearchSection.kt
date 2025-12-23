package com.aa.search.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aa.common.theme.Dimens
import com.aa.search.viewmodel.SearchUiState

@Composable
fun SearchSection(
    state: SearchUiState,
    onQueryChange: (String) -> Unit
) {
    Column {
        Text(
            text = "Weather Now & Later",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(Dimens.space16))

        OutlinedTextField(
            value = state.query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search City...") },
            singleLine = true,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(Dimens.cornerRadius),
            trailingIcon = {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(Dimens.iconSizeSmall),
                        strokeWidth = 2.dp
                    )
                }
            },
            isError = state.error != null
        )

        if (state.error != null) {
            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = Dimens.space4, start = Dimens.space4)
            )
        }
    }
}