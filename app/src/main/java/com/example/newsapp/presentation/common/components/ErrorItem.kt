package com.example.newsapp.presentation.common.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ErrorItem(
    errorMessage: String,
    onRetryClick: () -> Unit,
    showRefreshIcon: Boolean = false,
    fontSize: Int = 14,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(16.dp)
    ) {
        if(showRefreshIcon){
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = "Refresh error",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = { onRetryClick() },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Retry")
        }
    }
}
