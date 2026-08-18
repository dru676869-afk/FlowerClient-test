package com.example.model

enum class NavigationTab {
    HOME,
    DASHBOARD,
    SETTINGS
}

enum class VersionChannel(val label: String) {
    STABLE("stable"),
    BETA("beta")
}

data class MinecraftVersion(
    val id: String,
    val name: String,
    val channel: VersionChannel,
    val versionString: String,
    val releaseDate: String,
    val isInstalled: Boolean,
    val isActive: Boolean,
    val downloadSize: String,
    val features: List<String>
)

enum class LogLevel {
    INFO,
    SYSTEM,
    HOOK,
    SUCCESS,
    WARN,
    ERROR
}

data class LogEntry(
    val id: Long = System.nanoTime(),
    val timestamp: String,
    val level: LogLevel,
    val message: String
)

data class ClientModule(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val isEnabled: Boolean,
    val configValue: String? = null
)

enum class ResourceType(val label: String, val extension: String) {
    TEXTURE_PACK("Texture Pack", ".mcpack"),
    ADDON("Behavior & Addon", ".mcaddon"),
    WORLD("World Map", ".mcworld"),
    SHADER("Shader Pack", ".mcpack"),
    SKIN_PACK("Skin Pack", ".mcpack")
}

data class ResourcePack(
    val id: String,
    val name: String,
    val type: ResourceType,
    val author: String,
    val size: String,
    val isInstalled: Boolean,
    val isEnabled: Boolean
)

data class OptionEntry(
    val key: String,
    val value: String,
    val defaultValue: String,
    val description: String
)

data class ClientBackup(
    val id: String,
    val name: String,
    val createdAt: String,
    val sizeMb: Double,
    val worldsCount: Int,
    val packsCount: Int
)

data class DiscordAccount(
    val isConnected: Boolean = false,
    val username: String = "FlowerUser",
    val discriminator: String = "1337",
    val statusText: String = "Playing Minecraft Bedrock",
    val richPresenceEnabled: Boolean = true,
    val showPlayTime: Boolean = true
)
