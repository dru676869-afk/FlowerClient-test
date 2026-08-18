package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MinecraftVersion
import com.example.model.VersionChannel
import com.example.ui.theme.AccentWhite
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.MutedText
import com.example.ui.theme.MutedTextDark
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated

@Composable
fun VersionManagerScreen(
    versions: List<MinecraftVersion>,
    selectedVersion: MinecraftVersion,
    activeChannel: VersionChannel,
    onChannelSelected: (VersionChannel) -> Unit,
    onVersionSelected: (MinecraftVersion) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val filteredVersions = versions.filter { it.channel == activeChannel }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AmoledBlack)
            .statusBarsPadding()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Top Navigation Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .size(40.dp)
                    .testTag("version_manager_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = AccentWhite
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = "Version Manager",
                    color = AccentWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.3).sp,
                    modifier = Modifier.testTag("version_manager_title")
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Install or change between minecraft versions",
                    color = MutedText,
                    fontSize = 12.5.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Tab Filtering Strip: 'stable' vs 'beta' with active underline bar
        TabFilteringStrip(
            activeChannel = activeChannel,
            onChannelSelected = onChannelSelected
        )

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${activeChannel.label.uppercase()} BUILDS MATRIX",
                color = MutedText,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Text(
                text = "${filteredVersions.size} versions found",
                color = MutedTextDark,
                fontSize = 11.5.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("versions_matrix_list"),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(filteredVersions, key = { it.id }) { ver ->
                VersionMatrixNodeCard(
                    version = ver,
                    isSelected = ver.id == selectedVersion.id,
                    onSelect = { onVersionSelected(ver) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun TabFilteringStrip(
    activeChannel: VersionChannel,
    onChannelSelected: (VersionChannel) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("version_filter_strip")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ChannelTabItem(
                label = "stable",
                isActive = activeChannel == VersionChannel.STABLE,
                onClick = { onChannelSelected(VersionChannel.STABLE) },
                testTag = "channel_tab_stable",
                modifier = Modifier.weight(1f)
            )

            ChannelTabItem(
                label = "beta",
                isActive = activeChannel == VersionChannel.BETA,
                onClick = { onChannelSelected(VersionChannel.BETA) },
                testTag = "channel_tab_beta",
                modifier = Modifier.weight(1f)
            )
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = BorderSubtle
        )
    }
}

@Composable
private fun ChannelTabItem(
    label: String,
    isActive: Boolean,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, color = AccentWhite.copy(alpha = 0.1f)),
                onClick = onClick
            )
            .testTag(testTag)
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label.uppercase(),
            color = if (isActive) AccentWhite else MutedText,
            fontSize = 13.5.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
            letterSpacing = 0.8.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .height(2.5.dp)
                .fillMaxWidth(0.45f)
                .background(if (isActive) AccentWhite else Color.Transparent, RoundedCornerShape(2.dp))
        )
    }
}

@Composable
private fun VersionMatrixNodeCard(
    version: MinecraftVersion,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(
                1.dp,
                if (isSelected) AccentWhite else BorderSubtle,
                RoundedCornerShape(24.dp)
            )
            .testTag("version_card_${version.id}"),
        color = SurfaceDark,
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = version.name,
                            color = AccentWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.2).sp
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF2ECC71).copy(alpha = 0.15f))
                                    .border(1.dp, Color(0xFF2ECC71).copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "ACTIVE",
                                    color = Color(0xFF2ECC71),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "Build ${version.versionString} • ${version.releaseDate} • ${version.downloadSize}",
                        color = MutedText,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Features list in #1A1A1A container
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceElevated)
                    .padding(12.dp)
            ) {
                version.features.forEach { feature ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .background(AccentWhite, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = feature,
                            color = MutedText,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .clip(RoundedCornerShape(23.dp))
                        .background(SurfaceElevated)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(23.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Selected",
                            tint = Color(0xFF2ECC71),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Currently Selected Build",
                            color = AccentWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else {
                Button(
                    onClick = onSelect,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(23.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentWhite,
                        contentColor = AmoledBlack
                    )
                ) {
                    Text(
                        text = if (version.isInstalled) "Switch to this version" else "Install & Activate",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
