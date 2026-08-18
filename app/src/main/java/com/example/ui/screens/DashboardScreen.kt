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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.outlined.Power
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentWhite
import com.example.ui.theme.AlertCoralRed
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.BorderMedium
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.MutedText
import com.example.ui.theme.MutedTextDark
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated

@Composable
fun DashboardScreen(
    minecraftDataFound: Boolean,
    optionsFound: Boolean,
    onFixStorage: () -> Unit,
    onCreateBackup: () -> Unit,
    onImportBackup: () -> Unit,
    onOpenModules: () -> Unit,
    onOpenResourceInstaller: () -> Unit,
    onOpenOptionsEditor: () -> Unit,
    onCreateOptionsTxtTemplate: () -> Unit,
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

        // Header Title: 'File Management' left-aligned layout
        Text(
            text = "File Management",
            color = AccentWhite,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.4).sp,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("dashboard_header_title")
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Bedrock storage, modifications & options engine",
            color = MutedText,
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Error Notification Banner: Conditional state info alert display block
        if (!minecraftDataFound) {
            ErrorNotificationBanner(
                onFixStorage = onFixStorage
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Action Center Split: [Create Backup] (solid white) and [Import Backup] (ghost outlined)
        ActionCenterSplit(
            onCreateBackup = onCreateBackup,
            onImportBackup = onImportBackup
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Configurable Modules Deck
        Text(
            text = "GAME ENHANCEMENTS",
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
            ModuleDeckItem(
                icon = Icons.Filled.Build,
                title = "Modules",
                description = "Configure your game modifications",
                onClick = onOpenModules,
                testTag = "deck_item_modules"
            )

            HorizontalDivider(thickness = 1.dp, color = BorderSubtle)

            ModuleDeckItem(
                icon = Icons.Filled.Extension,
                title = "Resource installer",
                description = "Install addons, texture packs, maps etc",
                onClick = onOpenResourceInstaller,
                testTag = "deck_item_resource_installer"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Configurations Section (Options.txt Editor)
        Text(
            text = "CONFIGURATIONS",
            color = MutedText,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        OptionsTxtDeckItem(
            optionsFound = optionsFound,
            onOpenOptionsEditor = onOpenOptionsEditor,
            onCreateOptionsTxt = onCreateOptionsTxtTemplate
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ErrorNotificationBanner(
    onFixStorage: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, BorderSubtle, RoundedCornerShape(24.dp))
            .testTag("error_notification_banner"),
        color = SurfaceDark,
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(SurfaceElevated),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Power,
                    contentDescription = "Socket Alert",
                    tint = MutedText,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "No Minecraft data found",
                    color = AccentWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "External directory storage permissions required",
                    color = MutedText,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceElevated)
                    .clickable(onClick = onFixStorage)
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Scan",
                    color = AccentWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ActionCenterSplit(
    onCreateBackup: () -> Unit,
    onImportBackup: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("action_center_split"),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // [Create Backup] (solid white pill button)
        Button(
            onClick = onCreateBackup,
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
                .testTag("create_backup_button"),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentWhite,
                contentColor = AmoledBlack
            )
        ) {
            Text(
                text = "Create Backup",
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // [Import Backup] (ghost transparent pill structure outlined in #1A1A1A)
        OutlinedButton(
            onClick = onImportBackup,
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
                .testTag("import_backup_button"),
            shape = RoundedCornerShape(26.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = AccentWhite
            )
        ) {
            Text(
                text = "Import Backup",
                fontSize = 13.5.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ModuleDeckItem(
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
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = "Navigate",
            tint = MutedText,
            modifier = Modifier.size(14.dp)
        )
    }
}

@Composable
private fun OptionsTxtDeckItem(
    optionsFound: Boolean,
    onOpenOptionsEditor: () -> Unit,
    onCreateOptionsTxt: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, BorderSubtle, RoundedCornerShape(24.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, color = AccentWhite.copy(alpha = 0.12f)),
                onClick = onOpenOptionsEditor
            )
            .testTag("options_txt_editor_block"),
        color = SurfaceDark,
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(SurfaceElevated),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Description,
                            contentDescription = "Options.txt",
                            tint = AccentWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "Options.txt Editor",
                            color = AccentWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = (-0.2).sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Direct Bedrock engine parameter tuning",
                            color = MutedText,
                            fontSize = 12.sp
                        )
                    }
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "Open Editor",
                    tint = MutedText,
                    modifier = Modifier.size(14.dp)
                )
            }

            if (!optionsFound) {
                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(AlertCoralRed.copy(alpha = 0.1f))
                        .border(1.dp, AlertCoralRed.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Column {
                        Text(
                            text = "options.txt not found, perhaps ur game storage isn't external",
                            color = AlertCoralRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tap to initialize template options.txt →",
                            color = AccentWhite,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable(onClick = onCreateOptionsTxt)
                        )
                    }
                }
            }
        }
    }
}
