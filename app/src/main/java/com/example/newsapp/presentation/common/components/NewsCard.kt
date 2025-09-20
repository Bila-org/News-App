package com.example.newsapp.presentation.common.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.newsapp.data.dto.Article
import com.example.newsapp.data.dto.Source
import com.example.newsapp.ui.theme.NewsAppTheme
import kotlin.math.roundToInt


@Composable
fun NewsCard(
    article: Article,
    onArticleClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    swipeToDelete: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    var offsetX by remember { mutableFloatStateOf(0f) }

    val gradientColors = listOf(
        Color.Transparent,
        MaterialTheme.colorScheme.background.copy(alpha = 0.7f),
        MaterialTheme.colorScheme.background
    )

    Column(
        modifier = modifier
            .padding(16.dp)
            .clickable {
                onArticleClick()
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        if (offsetX < -100f) {
                            swipeToDelete()
                            offsetX = 0f
                        } else
                            offsetX = 0f
                    }
                ) { change, dragAmount ->
                    change.consume()
                    offsetX = (offsetX + dragAmount).coerceIn(-300f, 0f)
                }
            },
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .offset { IntOffset(offsetX.roundToInt(), 0) },
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            )
        ) {
            Box(
                contentAlignment = Alignment.TopStart
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    model = article.urlToImage,
                    //model = ImageRequest.Builder(LocalContext.current).data(article.urlToImage).build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = gradientColors,
                                startY = 0f,
                                endY = Float.POSITIVE_INFINITY
                            )
                        ),
                )

                Text(
                    text = article.title ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 2,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .fillMaxWidth()
                        .padding(6.dp),
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                //  .align(Alignment.TopStart)
            )
            {
                Text(
                    text = article.source.name ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = if (article.isBookmarked)
                        Icons.Filled.Bookmark else
                        Icons.Outlined.BookmarkBorder,
                    contentDescription = "Bookmark",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                onBookmarkClick()
                            }
                        )
                )
            }
            Text(
                text = article.description ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(8.dp)
                //   .align(Alignment.End)
            )
        }
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    //showSystemUi = true
)
@Composable
fun NewsCardPreview() {
    NewsAppTheme(dynamicColor = false) {
        NewsCard(
            article = Article(
                author = "Buck Throckmorton",
                content = "And then there is Rivian. Not only is it not gaining sales from disaffected Tesla shoppers, but its sales are going the wrong way…rapidly. Having sold right at 50,000 units each of the past two full … [+913 chars]",
                description = "2025 has been a disaster for the electric vehicle industry in the United States. But as bad as it’s been, it just got a lot worse. As I documented a few days ago, The One Big Beautiful Bill that...",
                publishedAt = "2025-07-11T15:00:01Z",
                source = Source(id = "", name = "NPR"),
                title = "THE MORNING RANT: 2025 Has Been an Awful Year for EVs, and It Keeps Getting Worse",
                url = "https://acecomments.mu.nu/?post=415617",
                urlToImage = ""
            ),
            onArticleClick = { },
            onBookmarkClick = {},
        )
    }
}