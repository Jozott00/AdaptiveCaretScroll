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
        return settings.bottomDistance != component?.getBottomPadding()
                || settings.topDistance != component?.getTopPadding()
                || settings.paddingUnit != component?.getPaddingUnit()
                || settings.scrollMode != component?.getScrollMode()
                || settings.enabled != component?.isEnabled()


    }

    /**
     * Applies the modified settings.
     */
    override fun apply() {
        val bottom = component?.getBottomPadding() ?: return
        val top = component?.getTopPadding() ?: return
        val paddingUnit = component?.getPaddingUnit() ?: return
        val scrollMode = component?.getScrollMode() ?: return
        val enabled = component?.isEnabled() ?: return


        ScrollPluginSettingsService
            .getInstance()
            .loadState(ScrollPluginSettings(
                enabled = enabled,
                bottomDistance = bottom,
                topDistance = top,
                paddingUnit = paddingUnit,
                scrollMode = scrollMode
            ))
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

        component?.setEnabled(settings.state.enabled)
        component?.setScrollMode(settings.state.scrollMode)
        component?.setPaddingUnit(settings.state.paddingUnit)
        component?.setTopPadding(settings.state.topDistance)
        component?.setBottomPadding(settings.state.bottomDistance)
    }

    /**
     * Disposes of UI resources to free up memory.
     */
    override fun disposeUIResources() {
        component = null
    }
}
