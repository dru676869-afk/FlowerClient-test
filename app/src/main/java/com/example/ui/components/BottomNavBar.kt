package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.NavigationTab
import com.example.ui.theme.AccentWhite
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.BorderDivider

@Composable
fun FlowerBottomNavBar(
    currentTab: NavigationTab,
    onTabSelected: (NavigationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AmoledBlack)
            .navigationBarsPadding()
    ) {
        HorizontalDivider(
            thickness = 1.dp,
            color = BorderDivider
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(76.dp)
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavBarItem(
                label = "Home",
                icon = Icons.Filled.Home,
                isSelected = currentTab == NavigationTab.HOME,
                onClick = { onTabSelected(NavigationTab.HOME) },
                testTag = "nav_home_tab"
            )

            NavBarItem(
                label = "Dashboard",
                icon = Icons.Filled.Dashboard,
                isSelected = currentTab == NavigationTab.DASHBOARD,
                onClick = { onTabSelected(NavigationTab.DASHBOARD) },
                testTag = "nav_dashboard_tab"
            )

            NavBarItem(
                label = "Settings",
                icon = Icons.Filled.Settings,
                isSelected = currentTab == NavigationTab.SETTINGS,
                onClick = { onTabSelected(NavigationTab.SETTINGS) },
                testTag = "nav_settings_tab"
            )
        }
    }
}

@Composable
private fun NavBarItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = false, radius = 28.dp, color = AccentWhite.copy(alpha = 0.2f)),
                onClick = onClick
            )
            .testTag(testTag)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isSelected) {
            // Active pill: h-8 w-12 bg-white rounded-full flex items-center justify-center
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(AccentWhite),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = AmoledBlack,
                    modifier = Modifier.size(20.dp)
                )
            }
        } else {
            // Inactive icon without pill, opacity 40%
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = AccentWhite.copy(alpha = 0.4f),
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label.uppercase(),
            color = if (isSelected) AccentWhite else AccentWhite.copy(alpha = 0.4f),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
        )
    }
}
