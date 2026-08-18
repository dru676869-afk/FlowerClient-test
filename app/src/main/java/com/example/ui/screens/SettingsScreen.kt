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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DiscordAccount
import com.example.ui.theme.AccentWhite
import com.example.ui.theme.AlertCoralRed
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.BorderMedium
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.DiscordBlurple
import com.example.ui.theme.MutedText
import com.example.ui.theme.MutedTextDark
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated

@Composable
fun SettingsScreen(
    packageId: String,
    discordAccount: DiscordAccount,
    onPackageIdChanged: (String) -> Unit,
    onLoginDiscord: () -> Unit,
    onLogoutDiscord: () -> Unit,
    onOpenConfig: () -> Unit,
    onOpenThemes: () -> Unit,
    onOpenAbout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AmoledBlack)
            .statusBarsPadding()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Header Title
        Text(
            text = "Settings",
            color = AccentWhite,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.4).sp,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("settings_header_title")
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Integrations, package identifier & runtime preferences",
            color = MutedText,
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 1. OAuth SSO Card Layer (Discord Integration panel)
        DiscordIntegrationCard(
            account = discordAccount,
            onLogin = onLoginDiscord,
            onLogout = onLogoutDiscord
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 2. Minecraft Package ID Tracker: Dynamic editable input container
        PackageIdTrackerCard(
            currentPackageId = packageId,
            onPackageIdChanged = onPackageIdChanged
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 3. System Submenus: Modular navigation list elements
        Text(
            text = "CLIENT PREFERENCES",
            color = MutedText,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, BorderSubtle, RoundedCornerShape(24.dp))
                .background(SurfaceDark)
        ) {
            SubmenuItem(
                icon = Icons.Filled.Tune,
                title = "Configuration",
                description = "Configure the client",
                onClick = onOpenConfig,
                testTag = "submenu_item_config"
            )

            HorizontalDivider(thickness = 1.dp, color = BorderSubtle)

            SubmenuItem(
                icon = Icons.Filled.Palette,
                title = "Themes",
                description = "Customize your launcher appearance",
                onClick = onOpenThemes,
                testTag = "submenu_item_themes"
            )

            HorizontalDivider(thickness = 1.dp, color = BorderSubtle)

            SubmenuItem(
                icon = Icons.Filled.Info,
                title = "About",
                description = "App info, updates, and device details",
                onClick = onOpenAbout,
                testTag = "submenu_item_about"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun DiscordIntegrationCard(
    account: DiscordAccount,
    onLogin: () -> Unit,
    onLogout: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, BorderSubtle, RoundedCornerShape(24.dp))
            .testTag("discord_sso_card"),
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(SurfaceElevated),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🎮",
                            fontSize = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "Discord Integration",
                            color = AccentWhite,
                            fontSize = 15.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Rich Presence & Launcher SSO",
                            color = MutedText,
                            fontSize = 12.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (account.isConnected) Color(0xFF2ECC71).copy(alpha = 0.15f)
                            else SurfaceElevated
                        )
                        .border(
                            1.dp,
                            if (account.isConnected) Color(0xFF2ECC71).copy(alpha = 0.3f)
                            else BorderSubtle,
                            RoundedCornerShape(14.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = if (account.isConnected) "Connected" else "Not connected",
                        color = if (account.isConnected) Color(0xFF2ECC71) else MutedText,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            if (!account.isConnected) {
                Button(
                    onClick = onLogin,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("discord_login_button"),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DiscordBlurple,
                        contentColor = AccentWhite
                    )
                ) {
                    Text(
                        text = "Login with Discord",
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.3.sp
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceElevated)
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "${account.username}#${account.discriminator}",
                            color = AccentWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = account.statusText,
                            color = DiscordBlurple,
                            fontSize = 11.5.sp
                        )
                    }

                    Text(
                        text = "Disconnect",
                        color = AlertCoralRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .clickable(onClick = onLogout)
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun PackageIdTrackerCard(
    currentPackageId: String,
    onPackageIdChanged: (String) -> Unit
) {
    var textValue by remember(currentPackageId) { mutableStateOf(currentPackageId) }
    val focusManager = LocalFocusManager.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, BorderSubtle, RoundedCornerShape(24.dp))
            .testTag("package_id_tracker_card"),
        color = SurfaceDark,
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "MINECRAFT PACKAGE ID",
                color = MutedText,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceElevated)
                    .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 14.dp)
            ) {
                BasicTextField(
                    value = textValue,
                    onValueChange = {
                        textValue = it
                        onPackageIdChanged(it)
                    },
                    singleLine = true,
                    textStyle = TextStyle(
                        color = AccentWhite,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    cursorBrush = SolidColor(AccentWhite),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("package_id_input_field")
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PackagePresetChip(
                    label = "Retail",
                    isSelected = textValue == "com.mojang.minecraftpe",
                    onClick = {
                        textValue = "com.mojang.minecraftpe"
                        onPackageIdChanged("com.mojang.minecraftpe")
                    }
                )

                PackagePresetChip(
                    label = "Preview",
                    isSelected = textValue == "com.mojang.minecraftpepreview",
                    onClick = {
                        textValue = "com.mojang.minecraftpepreview"
                        onPackageIdChanged("com.mojang.minecraftpepreview")
                    }
                )

                PackagePresetChip(
                    label = "Edu",
                    isSelected = textValue == "com.mojang.minecraftedu",
                    onClick = {
                        textValue = "com.mojang.minecraftedu"
                        onPackageIdChanged("com.mojang.minecraftedu")
                    }
                )
            }
        }
    }
}

@Composable
private fun PackagePresetChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) AccentWhite else SurfaceElevated)
            .border(1.dp, if (isSelected) AccentWhite else BorderSubtle, RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) AmoledBlack else MutedText,
            fontSize = 11.5.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
private fun SubmenuItem(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit,
    testTag: String
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, color = AccentWhite.copy(alpha = 0.12f)),
                onClick = onClick
            )
            .testTag(testTag)
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(SurfaceElevated),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = AccentWhite,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = AccentWhite,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.2).sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                color = MutedText,
                fontSize = 12.sp
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = "Open $title",
            tint = MutedText,
            modifier = Modifier.size(14.dp)
        )
    }
}
