package com.dobodox.adaptivecaretscroll.settings

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

/**
 * ScrollPluginConfigurable is a class responsible for managing the configuration
 * settings of the Adaptive Caret Scroll plugin.
 */
class ScrollPluginConfigurable : Configurable {

    private var component: PluginSettingsComponent? = null

    /**
     * Creates the UI component for configuring settings.
     * @return JComponent which represents the configuration UI.
     */
    override fun createComponent(): JComponent? {
        component = PluginSettingsComponent()
        return component?.panel
    }

    /**
     * Checks if the settings have been modified by the user.
     * @return Boolean indicating whether the settings have been modified.
     */
    override fun isModified(): Boolean {
        val settings = ScrollPluginSettingsService.getInstance().state
        return settings.bottomDistance != component?.getBottomLines()
                || settings.topDistance != component?.getTopLines()
    }

    /**
     * Applies the modified settings.
     */
    override fun apply() {
        val bottom = component?.getBottomLines() ?: return
        val top = component?.getTopLines() ?: return
        ScrollPluginSettingsService
            .getInstance()
            .loadState(ScrollPluginSettings(bottomDistance = bottom, topDistance = top))
    }

    /**
     * Returns the display name for this configurable setting.
     * @return String representing the display name.
     */
    override fun getDisplayName(): String {
        return "Adaptive Caret Scroll"
    }

    /**
     * Returns the UI component that should get focus when the form is displayed.
     * @return JComponent that should be focused.
     */
    override fun getPreferredFocusedComponent(): JComponent? {
        return component?.getPreferredFocusedComponent()
    }

    /**
     * Resets the form fields to the initial settings.
     */
    override fun reset() {
        val settings = ScrollPluginSettingsService.getInstance()
        component?.setTopLines(settings.state.topDistance)
        component?.setBottomLines(settings.state.bottomDistance)
    }

    /**
     * Disposes of UI resources to free up memory.
     */
    override fun disposeUIResources() {
        component = null
    }
}
