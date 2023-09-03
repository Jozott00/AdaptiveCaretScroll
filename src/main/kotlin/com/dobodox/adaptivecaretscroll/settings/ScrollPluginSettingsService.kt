package com.dobodox.adaptivecaretscroll.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

/**
 * ScrollPluginSettingsService is responsible for persisting and loading the settings
 * of the Adaptive Caret Scroll plugin. The settings are stored in an XML file.
 *
 * This class is annotated with @State to declare that it will use IntelliJ's persistence framework
 * to manage the state of the settings.
 */
@State(name = "ScrollPluginSettings", storages = [Storage("ScrollPluginSettings.xml")])
class ScrollPluginSettingsService : PersistentStateComponent<ScrollPluginSettings> {

    /**
     * A variable to hold the current settings for the plugin.
     */
    private var settings = ScrollPluginSettings()

    /**
     * Fetches the current state of the settings.
     * @return The current state as a ScrollPluginSettings object.
     */
    override fun getState(): ScrollPluginSettings {
        return settings
    }

    /**
     * Loads a given state into the settings.
     * @param state The new state to load.
     */
    override fun loadState(state: ScrollPluginSettings) {
        settings = state
    }

    companion object {
        /**
         * Fetches an instance of this service.
         * @return An instance of ScrollPluginSettingsService.
         */
        fun getInstance(): ScrollPluginSettingsService {
            return ApplicationManager.getApplication().getService(ScrollPluginSettingsService::class.java)
        }
    }
}
