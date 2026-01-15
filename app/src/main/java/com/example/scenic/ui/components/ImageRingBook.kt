package com.example.scenic.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.scenic.data.model.Image
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageRingBook(
    images: List<Image>,
    currentIndex: Int,
    onIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (images.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val pagerState = rememberPagerState(
        initialPage = currentIndex,
        pageCount = { images.size }
    )

    // Sync pager state with external currentIndex (e.g. auto-transition)
    LaunchedEffect(currentIndex) {
        if (pagerState.currentPage != currentIndex) {
            pagerState.animateScrollToPage(currentIndex)
        }
    }

    // Sync external state with pager state (manually swiping)
    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != currentIndex) {
            onIndexChange(pagerState.currentPage)
        }
    }

    Box(
        modifier = modifier
            .aspectRatio(0.8f) // Book aspect ratio
            .background(Color(0xFFE0E0E0), RoundedCornerShape(16.dp)) // Book cover/page bg
            .padding(16.dp)
    ) {
        // Rings
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 8.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            repeat(5) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(Color.Black, CircleShape)
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Image Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 40.dp) // Space for rings
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val image = images[page]
                AsyncImage(
                    model = image.src,
                    contentDescription = image.alt,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    onError = { error ->
                        println("Image load error: ${image.src} - ${error.result.throwable}")
                    }
                )
            }
        }
    }
}
