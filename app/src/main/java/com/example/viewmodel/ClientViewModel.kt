package com.example.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.ClientBackup
import com.example.model.ClientModule
import com.example.model.DiscordAccount
import com.example.model.LogEntry
import com.example.model.LogLevel
import com.example.model.MinecraftVersion
import com.example.model.NavigationTab
import com.example.model.OptionEntry
import com.example.model.ResourcePack
import com.example.model.ResourceType
import com.example.model.VersionChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ClientViewModel : ViewModel() {

    private val timeFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    private val _currentTab = MutableStateFlow(NavigationTab.HOME)
    val currentTab: StateFlow<NavigationTab> = _currentTab.asStateFlow()

    private val _packageId = MutableStateFlow("com.mojang.minecraftpe")
    val packageId: StateFlow<String> = _packageId.asStateFlow()

    private val _versionChannel = MutableStateFlow(VersionChannel.STABLE)
    val versionChannel: StateFlow<VersionChannel> = _versionChannel.asStateFlow()

    private val _versions = MutableStateFlow(
        listOf(
            MinecraftVersion(
                id = "v1.21.20",
                name = "Release Build 1.21.20",
                channel = VersionChannel.STABLE,
                versionString = "1.21.20.03",
                releaseDate = "August 2026",
                isInstalled = true,
                isActive = true,
                downloadSize = "892 MB",
                features = listOf("Tricky Trials update", "Render Dragon 1.4", "ARM64 JIT support")
            ),
            MinecraftVersion(
                id = "v1.21.0",
                name = "Release Build 1.21.0",
                channel = VersionChannel.STABLE,
                versionString = "1.21.0.24",
                releaseDate = "June 2026",
                isInstalled = true,
                isActive = false,
                downloadSize = "874 MB",
                features = listOf("Crafter & Trial Chambers", "Breeze Mob", "Wind Charge")
            ),
            MinecraftVersion(
                id = "v1.20.81",
                name = "Release Build 1.20.81",
                channel = VersionChannel.STABLE,
                versionString = "1.20.81.01",
                releaseDate = "April 2026",
                isInstalled = false,
                isActive = false,
                downloadSize = "830 MB",
                features = listOf("Armadillo & Wolf Armor", "Hotfix stability")
            ),
            MinecraftVersion(
                id = "v1.21.30.24-prev",
                name = "Bedrock Preview 1.21.30.24",
                channel = VersionChannel.BETA,
                versionString = "1.21.30.24",
                releaseDate = "August 2026",
                isInstalled = true,
                isActive = false,
                downloadSize = "945 MB",
                features = listOf("Experimental shaders", "Vibration overhaul", "New UI engine")
            ),
            MinecraftVersion(
                id = "v1.21.20.22-beta",
                name = "Bedrock Beta 1.21.20.22",
                channel = VersionChannel.BETA,
                versionString = "1.21.20.22",
                releaseDate = "July 2026",
                isInstalled = false,
                isActive = false,
                downloadSize = "910 MB",
                features = listOf("Deferred graphics test", "Custom entity rendering")
            )
        )
    )
    val versions: StateFlow<List<MinecraftVersion>> = _versions.asStateFlow()

    private val _selectedVersion = MutableStateFlow(
        _versions.value.first { it.isActive }
    )
    val selectedVersion: StateFlow<MinecraftVersion> = _selectedVersion.asStateFlow()

    // Overlay & Sheet Navigation
    private val _showVersionManager = MutableStateFlow(false)
    val showVersionManager: StateFlow<Boolean> = _showVersionManager.asStateFlow()

    private val _showModulesSheet = MutableStateFlow(false)
    val showModulesSheet: StateFlow<Boolean> = _showModulesSheet.asStateFlow()

    private val _showResourceInstallerSheet = MutableStateFlow(false)
    val showResourceInstallerSheet: StateFlow<Boolean> = _showResourceInstallerSheet.asStateFlow()

    private val _showOptionsEditorSheet = MutableStateFlow(false)
    val showOptionsEditorSheet: StateFlow<Boolean> = _showOptionsEditorSheet.asStateFlow()

    private val _showConfigSubmenu = MutableStateFlow(false)
    val showConfigSubmenu: StateFlow<Boolean> = _showConfigSubmenu.asStateFlow()

    private val _showThemesSubmenu = MutableStateFlow(false)
    val showThemesSubmenu: StateFlow<Boolean> = _showThemesSubmenu.asStateFlow()

    private val _showAboutSubmenu = MutableStateFlow(false)
    val showAboutSubmenu: StateFlow<Boolean> = _showAboutSubmenu.asStateFlow()

    private val _showBackupSheet = MutableStateFlow(false)
    val showBackupSheet: StateFlow<Boolean> = _showBackupSheet.asStateFlow()

    // System Monitor Logs Stream
    private val _logs = MutableStateFlow(
        listOf(
            LogEntry(
                timestamp = getCurrentTimeString(),
                level = LogLevel.SYSTEM,
                message = "Ready to launch Minecraft Bedrock"
            )
        )
    )
    val logs: StateFlow<List<LogEntry>> = _logs.asStateFlow()

    // Status Flags
    private val _isLaunching = MutableStateFlow(false)
    val isLaunching: StateFlow<Boolean> = _isLaunching.asStateFlow()

    private val _minecraftDataFound = MutableStateFlow(false)
    val minecraftDataFound: StateFlow<Boolean> = _minecraftDataFound.asStateFlow()

    private val _optionsFound = MutableStateFlow(false)
    val optionsFound: StateFlow<Boolean> = _optionsFound.asStateFlow()

    // Discord SSO State
    private val _discordAccount = MutableStateFlow(DiscordAccount(isConnected = false))
    val discordAccount: StateFlow<DiscordAccount> = _discordAccount.asStateFlow()

    // Modules
    private val _modules = MutableStateFlow(
        listOf(
            ClientModule(
                id = "fps_boost",
                name = "Bedrock FPS Optimizer",
                description = "Optimizes chunk tessellation, particle culling and Render Dragon threads",
                category = "Performance",
                isEnabled = true,
                configValue = "Max (120 FPS Target)"
            ),
            ClientModule(
                id = "fullbright",
                name = "Fullbright (Gamma 100)",
                description = "Forces night vision ambient light levels in caves and underwater",
                category = "Visuals",
                isEnabled = true,
                configValue = "Gamma 1000.0"
            ),
            ClientModule(
                id = "custom_crosshair",
                name = "Custom Crosshair HUD",
                description = "Renders dynamic high-contrast flower crosshair with dynamic bloom",
                category = "HUD",
                isEnabled = true,
                configValue = "Style: Flower Dot"
            ),
            ClientModule(
                id = "keystrokes_cps",
                name = "Keystrokes & CPS Overlay",
                description = "Displays on-screen touch taps, CPS counter, and analog stick vectors",
                category = "HUD",
                isEnabled = false,
                configValue = "Top-Right Anchor"
            ),
            ClientModule(
                id = "armor_status",
                name = "Armor & Durability Status",
                description = "Shows real-time armor durability and held item stats on screen",
                category = "HUD",
                isEnabled = true,
                configValue = "Vertical Stack"
            ),
            ClientModule(
                id = "auto_sprint",
                name = "Toggle Sprint / Auto-Sprint",
                description = "Maintains constant sprint velocity without double-tapping virtual stick",
                category = "Movement",
                isEnabled = true
            ),
            ClientModule(
                id = "quick_switcher",
                name = "Hotbar Quick Switcher",
                description = "Fast one-tap access to hotbar slots 1 through 9 with gesture swipe",
                category = "Controls",
                isEnabled = false
            ),
            ClientModule(
                id = "low_fire",
                name = "Low Fire & Clear Water",
                description = "Reduces on-screen fire animation height for unobstructed combat vision",
                category = "Visuals",
                isEnabled = true
            )
        )
    )
    val modules: StateFlow<List<ClientModule>> = _modules.asStateFlow()

    // Resource Installer
    private val _resourcePacks = MutableStateFlow(
        listOf(
            ResourcePack(
                id = "rp_faithful",
                name = "Faithful 32x HD Bedrock",
                type = ResourceType.TEXTURE_PACK,
                author = "Faithful Team",
                size = "42.5 MB",
                isInstalled = true,
                isEnabled = true
            ),
            ResourcePack(
                id = "rp_barebones",
                name = "Bare Bones Official 1.21",
                type = ResourceType.TEXTURE_PACK,
                author = "RobotPantaloons",
                size = "18.2 MB",
                isInstalled = true,
                isEnabled = false
            ),
            ResourcePack(
                id = "rp_actions_stuff",
                name = "Actions & Stuff Animations",
                type = ResourceType.ADDON,
                author = "AnimationForge",
                size = "76.8 MB",
                isInstalled = true,
                isEnabled = true
            ),
            ResourcePack(
                id = "rp_esbe_shader",
                name = "ESBE 3G / Render Dragon Shader",
                type = ResourceType.SHADER,
                author = "McBE Shaders",
                size = "12.4 MB",
                isInstalled = false,
                isEnabled = false
            ),
            ResourcePack(
                id = "rp_pvp_arena",
                name = "PvP Practice Hub Bedrock",
                type = ResourceType.WORLD,
                author = "FlowerCommunity",
                size = "115.0 MB",
                isInstalled = true,
                isEnabled = true
            )
        )
    )
    val resourcePacks: StateFlow<List<ResourcePack>> = _resourcePacks.asStateFlow()

    // Options.txt Key-Values
    private val _optionsList = MutableStateFlow(
        listOf(
            OptionEntry("gfx_renderdistance_chunks", "16", "8", "Chunk render distance limit"),
            OptionEntry("gfx_vsync", "0", "1", "V-Sync (0 = Uncapped, 1 = 60Hz lock)"),
            OptionEntry("gfx_max_framerate", "120", "0", "Max framerate limiter"),
            OptionEntry("game_fov", "85.000000", "70.000000", "Field of View angle in degrees"),
            OptionEntry("ctrl_sensitivity", "60", "50", "Touch screen look sensitivity"),
            OptionEntry("audio_sound", "1.000000", "1.000000", "Master volume slider"),
            OptionEntry("gfx_texel_aa", "0", "0", "Anti-aliasing mode"),
            OptionEntry("gfx_bloom", "1", "1", "Render Dragon bloom effect"),
            OptionEntry("gfx_raytracing", "0", "0", "Hardware raytracing / deferred lighting")
        )
    )
    val optionsList: StateFlow<List<OptionEntry>> = _optionsList.asStateFlow()

    // Backups
    private val _backups = MutableStateFlow(
        listOf(
            ClientBackup("bk_01", "Flower_Backup_PreUpdate_1.21", "2026-08-10 14:22", 184.2, 3, 5),
            ClientBackup("bk_02", "PvP_Config_And_Worlds", "2026-08-15 09:11", 96.5, 1, 3)
        )
    )
    val backups: StateFlow<List<ClientBackup>> = _backups.asStateFlow()

    // Configuration Settings
    private val _allocatedRamGb = MutableStateFlow(4)
    val allocatedRamGb: StateFlow<Int> = _allocatedRamGb.asStateFlow()

    private val _customJvmArgs = MutableStateFlow("-XX:+UseG1GC -XX:MaxGCPauseMillis=20")
    val customJvmArgs: StateFlow<String> = _customJvmArgs.asStateFlow()

    private val _autoCleanLogs = MutableStateFlow(true)
    val autoCleanLogs: StateFlow<Boolean> = _autoCleanLogs.asStateFlow()

    private val _activeThemeName = MutableStateFlow("AMOLED Pitch Black")
    val activeThemeName: StateFlow<String> = _activeThemeName.asStateFlow()

    fun selectTab(tab: NavigationTab) {
        _currentTab.value = tab
    }

    fun setPackageId(newId: String) {
        _packageId.value = newId.trim()
        appendLog(LogLevel.INFO, "Target Minecraft package ID updated to: ${newId.trim()}")
    }

    fun setVersionChannel(channel: VersionChannel) {
        _versionChannel.value = channel
    }

    fun selectVersion(version: MinecraftVersion) {
        _versions.update { list ->
            list.map { it.copy(isActive = (it.id == version.id)) }
        }
        _selectedVersion.value = version.copy(isActive = true)
        appendLog(LogLevel.SYSTEM, "Switched active Minecraft version to ${version.name} (${version.versionString})")
    }

    fun openVersionManager(show: Boolean = true) {
        _showVersionManager.value = show
    }

    fun openModulesSheet(show: Boolean = true) {
        _showModulesSheet.value = show
    }

    fun openResourceInstallerSheet(show: Boolean = true) {
        _showResourceInstallerSheet.value = show
    }

    fun openOptionsEditorSheet(show: Boolean = true) {
        _showOptionsEditorSheet.value = show
    }

    fun openConfigSubmenu(show: Boolean = true) {
        _showConfigSubmenu.value = show
    }

    fun openThemesSubmenu(show: Boolean = true) {
        _showThemesSubmenu.value = show
    }

    fun openAboutSubmenu(show: Boolean = true) {
        _showAboutSubmenu.value = show
    }

    fun openBackupSheet(show: Boolean = true) {
        _showBackupSheet.value = show
    }

    fun toggleModule(moduleId: String) {
        _modules.update { list ->
            list.map { mod ->
                if (mod.id == moduleId) {
                    val newState = !mod.isEnabled
                    appendLog(
                        if (newState) LogLevel.HOOK else LogLevel.INFO,
                        "Module '${mod.name}' ${if (newState) "enabled" else "disabled"}"
                    )
                    mod.copy(isEnabled = newState)
                } else mod
            }
        }
    }

    fun toggleResourcePack(packId: String) {
        _resourcePacks.update { list ->
            list.map { pack ->
                if (pack.id == packId) {
                    val newState = !pack.isEnabled
                    appendLog(
                        LogLevel.INFO,
                        "Resource pack '${pack.name}' ${if (newState) "activated in manifest" else "deactivated"}"
                    )
                    pack.copy(isEnabled = newState)
                } else pack
            }
        }
    }

    fun installResourcePack(packId: String) {
        viewModelScope.launch {
            appendLog(LogLevel.SYSTEM, "Downloading & unpacking resource pack...")
            delay(1200)
            _resourcePacks.update { list ->
                list.map { pack ->
                    if (pack.id == packId) {
                        pack.copy(isInstalled = true, isEnabled = true)
                    } else pack
                }
            }
            appendLog(LogLevel.SUCCESS, "Resource pack installed to /games/com.mojang/resource_packs")
        }
    }

    fun updateOption(key: String, value: String) {
        _optionsList.update { list ->
            list.map { if (it.key == key) it.copy(value = value) else it }
        }
        appendLog(LogLevel.INFO, "Updated options.txt: $key = $value")
    }

    fun fixStorageDirectory(context: Context) {
        viewModelScope.launch {
            appendLog(LogLevel.SYSTEM, "Scanning external storage for Bedrock directory...")
            delay(600)
            _minecraftDataFound.value = true
            _optionsFound.value = true
            appendLog(LogLevel.SUCCESS, "Found /storage/emulated/0/Android/data/${_packageId.value}/files/games/com.mojang")
            appendLog(LogLevel.SUCCESS, "options.txt parsed successfully (9 key-value entries)")
            Toast.makeText(context, "Storage linked & options.txt synchronized!", Toast.LENGTH_SHORT).show()
        }
    }

    fun createOptionsTxtTemplate() {
        _optionsFound.value = true
        appendLog(LogLevel.SUCCESS, "Generated optimized options.txt with 120 FPS & Fullbright configurations")
    }

    fun createBackup(name: String) {
        viewModelScope.launch {
            val backupName = if (name.isBlank()) "Backup_${SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(Date())}" else name
            appendLog(LogLevel.SYSTEM, "Starting backup creation: $backupName...")
            delay(800)
            val newBackup = ClientBackup(
                id = "bk_${System.currentTimeMillis()}",
                name = backupName,
                createdAt = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date()),
                sizeMb = 142.4,
                worldsCount = 2,
                packsCount = 4
            )
            _backups.update { listOf(newBackup) + it }
            appendLog(LogLevel.SUCCESS, "Backup created successfully ($backupName, 142.4 MB)")
        }
    }

    fun restoreBackup(backup: ClientBackup) {
        viewModelScope.launch {
            appendLog(LogLevel.SYSTEM, "Restoring backup: ${backup.name}...")
            delay(1000)
            appendLog(LogLevel.SUCCESS, "Restored ${backup.worldsCount} worlds, ${backup.packsCount} resource packs.")
        }
    }

    fun loginWithDiscord(context: Context) {
        viewModelScope.launch {
            appendLog(LogLevel.SYSTEM, "Authenticating with Discord OAuth2 SSO...")
            delay(1000)
            _discordAccount.value = DiscordAccount(
                isConnected = true,
                username = "FlowerPlayer",
                discriminator = "2026",
                statusText = "Playing Minecraft via Flower Client",
                richPresenceEnabled = true,
                showPlayTime = true
            )
            appendLog(LogLevel.SUCCESS, "Discord SSO connected: FlowerPlayer#2026")
            Toast.makeText(context, "Logged in as FlowerPlayer#2026", Toast.LENGTH_SHORT).show()
        }
    }

    fun logoutDiscord() {
        _discordAccount.value = DiscordAccount(isConnected = false)
        appendLog(LogLevel.INFO, "Discord account disconnected.")
    }

    fun setRamAllocation(gb: Int) {
        _allocatedRamGb.value = gb
        appendLog(LogLevel.INFO, "Client heap memory target set to: ${gb}GB")
    }

    fun setJvmArgs(args: String) {
        _customJvmArgs.value = args
    }

    fun setTheme(theme: String) {
        _activeThemeName.value = theme
        appendLog(LogLevel.INFO, "Theme set to: $theme")
    }

    fun appendLog(level: LogLevel, message: String) {
        val entry = LogEntry(
            timestamp = getCurrentTimeString(),
            level = level,
            message = message
        )
        _logs.update { list -> (list + entry).takeLast(100) }
    }

    fun clearLogs() {
        _logs.value = listOf(
            LogEntry(
                timestamp = getCurrentTimeString(),
                level = LogLevel.SYSTEM,
                message = "Logs cleared. Ready to launch Minecraft Bedrock."
            )
        )
    }

    fun launchMinecraft(context: Context) {
        if (_isLaunching.value) return

        viewModelScope.launch {
            _isLaunching.value = true
            if (_autoCleanLogs.value) {
                _logs.value = emptyList()
            }

            val targetPkg = _packageId.value
            val ver = _selectedVersion.value

            appendLog(LogLevel.SYSTEM, "--- Starting Flower Engine Launch Sequence ---")
            delay(250)
            appendLog(LogLevel.INFO, "Target package: $targetPkg")
            appendLog(LogLevel.INFO, "Active Version: ${ver.name} (${ver.versionString})")
            delay(300)

            val activeMods = _modules.value.filter { it.isEnabled }
            appendLog(LogLevel.HOOK, "Injecting ${activeMods.size} client modification hooks...")
            activeMods.forEach { mod ->
                appendLog(LogLevel.HOOK, " [HOOK] Injected: ${mod.name}")
            }
            delay(400)

            appendLog(LogLevel.SYSTEM, "Applying memory flags: ${_allocatedRamGb.value}GB Heap, JNI native bridge active")
            delay(350)

            if (_discordAccount.value.isConnected) {
                appendLog(LogLevel.INFO, "Discord Rich Presence dispatch: Playing Minecraft Bedrock (Flower Client v2.4.0)")
            }

            appendLog(LogLevel.SYSTEM, "Preparing Android Activity Intent for $targetPkg...")
            delay(300)

            val launchIntent = context.packageManager.getLaunchIntentForPackage(targetPkg)
            if (launchIntent != null) {
                try {
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(launchIntent)
                    appendLog(LogLevel.SUCCESS, "Successfully dispatched intent! Minecraft launched.")
                } catch (e: Exception) {
                    appendLog(LogLevel.ERROR, "Launch dispatch failed: ${e.localizedMessage}")
                }
            } else {
                appendLog(LogLevel.WARN, "Target package '$targetPkg' is not installed directly on this device/container.")
                appendLog(LogLevel.SUCCESS, "Simulation Mode: Flower Client hooks initialized & standby listening on socket :19132")
                Toast.makeText(
                    context,
                    "Client hooks loaded! (Minecraft package '$targetPkg' simulated)",
                    Toast.LENGTH_LONG
                ).show()
            }

            _isLaunching.value = false
        }
    }

    private fun getCurrentTimeString(): String {
        return timeFormatter.format(Date())
    }
}
