package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.NavigationTab
import com.example.ui.components.FlowerBottomNavBar
import com.example.ui.dialogs.AboutSubmenuDialog
import com.example.ui.dialogs.BackupDialog
import com.example.ui.dialogs.ConfigSubmenuDialog
import com.example.ui.dialogs.ModulesDialog
import com.example.ui.dialogs.OptionsEditorDialog
import com.example.ui.dialogs.ResourceInstallerDialog
import com.example.ui.dialogs.ThemesSubmenuDialog
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.VersionManagerScreen
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.FlowerClientTheme
import com.example.viewmodel.ClientViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlowerClientTheme {
                FlowerClientApp()
            }
        }
    }
}

@Composable
fun FlowerClientApp(
    viewModel: ClientViewModel = viewModel()
) {
    val context = LocalContext.current

    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val packageId by viewModel.packageId.collectAsStateWithLifecycle()
    val selectedVersion by viewModel.selectedVersion.collectAsStateWithLifecycle()
    val versions by viewModel.versions.collectAsStateWithLifecycle()
    val versionChannel by viewModel.versionChannel.collectAsStateWithLifecycle()
    val logs by viewModel.logs.collectAsStateWithLifecycle()
    val isLaunching by viewModel.isLaunching.collectAsStateWithLifecycle()
    val minecraftDataFound by viewModel.minecraftDataFound.collectAsStateWithLifecycle()
    val optionsFound by viewModel.optionsFound.collectAsStateWithLifecycle()
    val discordAccount by viewModel.discordAccount.collectAsStateWithLifecycle()
    val modules by viewModel.modules.collectAsStateWithLifecycle()
    val resourcePacks by viewModel.resourcePacks.collectAsStateWithLifecycle()
    val optionsList by viewModel.optionsList.collectAsStateWithLifecycle()
    val backups by viewModel.backups.collectAsStateWithLifecycle()
    val allocatedRamGb by viewModel.allocatedRamGb.collectAsStateWithLifecycle()
    val customJvmArgs by viewModel.customJvmArgs.collectAsStateWithLifecycle()
    val autoCleanLogs by viewModel.autoCleanLogs.collectAsStateWithLifecycle()
    val activeThemeName by viewModel.activeThemeName.collectAsStateWithLifecycle()

    // Sheet visibility states
    val showVersionManager by viewModel.showVersionManager.collectAsStateWithLifecycle()
    val showModulesSheet by viewModel.showModulesSheet.collectAsStateWithLifecycle()
    val showResourceInstallerSheet by viewModel.showResourceInstallerSheet.collectAsStateWithLifecycle()
    val showOptionsEditorSheet by viewModel.showOptionsEditorSheet.collectAsStateWithLifecycle()
    val showConfigSubmenu by viewModel.showConfigSubmenu.collectAsStateWithLifecycle()
    val showThemesSubmenu by viewModel.showThemesSubmenu.collectAsStateWithLifecycle()
    val showAboutSubmenu by viewModel.showAboutSubmenu.collectAsStateWithLifecycle()
    val showBackupSheet by viewModel.showBackupSheet.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBlack)
    ) {
        if (showVersionManager) {
            VersionManagerScreen(
                versions = versions,
                selectedVersion = selectedVersion,
                activeChannel = versionChannel,
                onChannelSelected = { viewModel.setVersionChannel(it) },
                onVersionSelected = {
                    viewModel.selectVersion(it)
                    viewModel.openVersionManager(false)
                },
                onClose = { viewModel.openVersionManager(false) }
            )
        } else {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = AmoledBlack,
                bottomBar = {
                    FlowerBottomNavBar(
                        currentTab = currentTab,
                        onTabSelected = { viewModel.selectTab(it) }
                    )
                }
            ) { innerPadding ->
                AnimatedContent(
                    targetState = currentTab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "tab_navigation",
                    modifier = Modifier.padding(innerPadding)
                ) { targetTab ->
                    when (targetTab) {
                        NavigationTab.HOME -> {
                            HomeScreen(
                                selectedVersion = selectedVersion,
                                logs = logs,
                                isLaunching = isLaunching,
                                onOpenVersionManager = { viewModel.openVersionManager(true) },
                                onLaunchMinecraft = { viewModel.launchMinecraft(context) },
                                onClearLogs = { viewModel.clearLogs() }
                            )
                        }

                        NavigationTab.DASHBOARD -> {
                            DashboardScreen(
                                minecraftDataFound = minecraftDataFound,
                                optionsFound = optionsFound,
                                onFixStorage = { viewModel.fixStorageDirectory(context) },
                                onCreateBackup = { viewModel.openBackupSheet(true) },
                                onImportBackup = { viewModel.openBackupSheet(true) },
                                onOpenModules = { viewModel.openModulesSheet(true) },
                                onOpenResourceInstaller = { viewModel.openResourceInstallerSheet(true) },
                                onOpenOptionsEditor = { viewModel.openOptionsEditorSheet(true) },
                                onCreateOptionsTxtTemplate = { viewModel.createOptionsTxtTemplate() }
                            )
                        }

                        NavigationTab.SETTINGS -> {
                            SettingsScreen(
                                packageId = packageId,
                                discordAccount = discordAccount,
                                onPackageIdChanged = { viewModel.setPackageId(it) },
                                onLoginDiscord = { viewModel.loginWithDiscord(context) },
                                onLogoutDiscord = { viewModel.logoutDiscord() },
                                onOpenConfig = { viewModel.openConfigSubmenu(true) },
                                onOpenThemes = { viewModel.openThemesSubmenu(true) },
                                onOpenAbout = { viewModel.openAboutSubmenu(true) }
                            )
                        }
                    }
                }
            }
        }

        // Sub-sheets & Modals
        if (showModulesSheet) {
            ModulesDialog(
                modules = modules,
                onToggleModule = { viewModel.toggleModule(it) },
                onDismiss = { viewModel.openModulesSheet(false) }
            )
        }

        if (showResourceInstallerSheet) {
            ResourceInstallerDialog(
                packs = resourcePacks,
                onTogglePack = { viewModel.toggleResourcePack(it) },
                onInstallPack = { viewModel.installResourcePack(it) },
                onDismiss = { viewModel.openResourceInstallerSheet(false) }
            )
        }

        if (showOptionsEditorSheet) {
            OptionsEditorDialog(
                options = optionsList,
                onUpdateOption = { k, v -> viewModel.updateOption(k, v) },
                onDismiss = { viewModel.openOptionsEditorSheet(false) }
            )
        }

        if (showBackupSheet) {
            BackupDialog(
                backups = backups,
                onCreateBackup = { viewModel.createBackup(it) },
                onRestoreBackup = { viewModel.restoreBackup(it) },
                onDismiss = { viewModel.openBackupSheet(false) }
            )
        }

        if (showConfigSubmenu) {
            ConfigSubmenuDialog(
                allocatedRamGb = allocatedRamGb,
                customJvmArgs = customJvmArgs,
                autoCleanLogs = autoCleanLogs,
                onRamChanged = { viewModel.setRamAllocation(it) },
                onJvmArgsChanged = { viewModel.setJvmArgs(it) },
                onAutoCleanLogsChanged = { /* toggle */ },
                onDismiss = { viewModel.openConfigSubmenu(false) }
            )
        }

        if (showThemesSubmenu) {
            ThemesSubmenuDialog(
                activeTheme = activeThemeName,
                onSelectTheme = { viewModel.setTheme(it) },
                onDismiss = { viewModel.openThemesSubmenu(false) }
            )
        }

        if (showAboutSubmenu) {
            AboutSubmenuDialog(
                onDismiss = { viewModel.openAboutSubmenu(false) }
            )
        }
    }
}
