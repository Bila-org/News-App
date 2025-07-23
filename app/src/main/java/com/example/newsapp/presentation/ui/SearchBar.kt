package com.example.newsapp.presentation.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.example.newsapp.ui.theme.NewsAppTheme
import kotlinx.coroutines.flow.debounce

@Composable
fun SearchBar(
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var textState = rememberSaveable { mutableStateOf("") }

    val currentSearch by rememberUpdatedState(onSearch)

    LaunchedEffect(Unit) {
            snapshotFlow { textState.value }
                .debounce(500L)
                .collect {
                    currentSearch(it)
                }
    }

    TextField(
        value = textState.value,
        onValueChange = { newText ->
            textState.value = newText
        },
        textStyle = MaterialTheme.typography.bodySmall,
        readOnly = false,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        },
        placeholder = {
            Text(
                text = "Search",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        shape = MaterialTheme.shapes.medium,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
        ),
        singleLine = true,
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch(textState.value)
            }
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        modifier = modifier
            .fillMaxWidth()
    )
}


@Preview(
    showBackground = true,
)
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun SearchBarPreview() {
    NewsAppTheme(dynamicColor = false) {
        SearchBar(
            onSearch = {},
        )
    }
}
