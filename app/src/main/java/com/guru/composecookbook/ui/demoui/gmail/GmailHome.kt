package com.guru.composecookbook.ui.demoui.gmail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.guru.composecookbook.R
import com.guru.composecookbook.data.DemoDataProvider
import com.guru.composecookbook.theme.typography
import com.guru.composecookbook.ui.Animations.FloatMultiStateAnimationExplode
import kotlin.math.absoluteValue

@Composable
fun GmailHome() {

    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()
    val expandFAB = remember { mutableStateOf(false) }

    Scaffold(

        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                },
                modifier = Modifier
                    .padding(16.dp)
                    .preferredHeight(48.dp)
                    .widthIn(min = 48.dp),
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.primary
            ) {
                AnimatingFabContent(
                    icon = {
                        androidx.compose.material.Icon(
                            asset = Icons.Outlined.Edit
                        )
                    },
                    text = {
                        Text(
                            text = "Compose",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    },
                    extended = expandFAB.value

                )
            }

        },
        drawerContent = { GmailDrawer() },
        drawerBackgroundColor = MaterialTheme.colors.background,
        drawerContentColor = MaterialTheme.colors.onBackground,
        scaffoldState = scaffoldState,
        bodyContent = {
            GmailContent(scrollState, scaffoldState.drawerState, expandFAB)
        }
    )
}

@Composable
fun GmailFloatingActionButton(rippleExplode: MutableState<Boolean>, lazyListState: LazyListState) {

    val isExpanded = remember { mutableStateOf(false) }

    val oldOffset = remember { mutableStateOf(0) }
    val oldIndex = remember { mutableStateOf(0) }

    // ensures that the user intents to have scroll gesture..
    val isVisibleScrolled =
        (oldOffset.value - lazyListState.firstVisibleItemScrollOffset).absoluteValue > 20

    if (oldIndex.value > lazyListState.firstVisibleItemIndex && isVisibleScrolled) {

        println("scroll down ")
        isExpanded.value = true

    } else if (oldIndex.value < lazyListState.firstVisibleItemIndex && isVisibleScrolled) {
        println("scroll up")
        isExpanded.value = false
    }

    if (!lazyListState.isAnimationRunning) {
        oldOffset.value = lazyListState.firstVisibleItemScrollOffset
        oldIndex.value = lazyListState.firstVisibleItemIndex
    }


}

@Composable
fun GmailContent(
    scrollState: ScrollState,
    drawerState: DrawerState,
    expandFAB: MutableState<Boolean>
) {

    val tweets = remember { DemoDataProvider.tweetList }

    Box {


        ScrollableColumn(
            modifier = Modifier,
            scrollState = scrollState,

            ) {

            Spacer(modifier = Modifier.preferredHeight(72.dp))

            for (i in tweets) {
                GmailListItem(item = i)
            }

        }

        SearchLayout(
            scroll = scrollState.value,
            expandFAB,
            drawerState = drawerState
        )


    }


}