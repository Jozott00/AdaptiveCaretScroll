package com.dobodox.adaptivecaretscroll.settings

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.TitledSeparator
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.fields.IntegerField
import com.intellij.util.ui.FormBuilder
import java.awt.Component
import java.awt.FlowLayout
import java.awt.Font
import javax.swing.*


/**
 * PluginSettingsComponent is responsible for creating and managing the GUI form
 * for setting the scroll padding at the top and bottom of the editor.
 */
class PluginSettingsComponent {

    var panel: JPanel? = null
    private val activateCheckbox = JCheckBox("Enable adaptive caret scrolling").apply {
        addItemListener {
            updateFieldAccessibility()
        }
    }
    private val topLines = IntegerField()
    private val bottomLines = IntegerField()
    private val scrollModeDropdown = ComboBox(ScrollMode.values()).apply {
        addItemListener {
            updatePaddingFieldsVisibility()
        }
    }


    private val paddingForm = FormBuilder.createFormBuilder().apply {
        val input = { field: IntegerField ->
            val flowLayoutPanel = JPanel(FlowLayout(FlowLayout.LEFT, 0, 0))
            flowLayoutPanel.add(field)
            flowLayoutPanel.add(JBLabel(" lines"))
            flowLayoutPanel
        }

        addComponent(TitledSeparator("Padding Settings"))
        addComponent(FormBuilder.createFormBuilder().apply {
            addLabeledComponent("Top padding:", input(topLines))
            addLabeledComponent("Bottom padding:", input(bottomLines))

            panel.border = BorderFactory.createEmptyBorder(0, 20, 0, 0)
        }.panel)

    }.panel

    init {

        val generalForm = FormBuilder.createFormBuilder().apply {
            addComponent(activateCheckbox)
            addVerticalGap(5)
            addLabeledComponent(JBLabel("Scrolling mode:"), scrollModeDropdown, 1, false)

            panel.border = BorderFactory.createEmptyBorder(0, 20, 0, 0)
        }


        // Create form using FormBuilder
        panel = FormBuilder.createFormBuilder()
            .addComponent(TitledSeparator("General"))
            .addComponent(generalForm.panel)
            .addComponent(paddingForm)
            .addComponentFillVertically(JPanel(), 0)
            .panel

        updatePaddingFieldsVisibility()
        updateFieldAccessibility()
    }

    private fun updatePaddingFieldsVisibility() {
        val isCustomMode = scrollModeDropdown.selectedItem as ScrollMode == ScrollMode.Custom
        paddingForm.isVisible = isCustomMode
    }

    private fun updateFieldAccessibility() {
        val isActivated = activateCheckbox.isSelected
        topLines.isEnabled = isActivated
        bottomLines.isEnabled = isActivated
        scrollModeDropdown.isEnabled = isActivated
    }

    /**
     * Returns the component that should receive focus initially.
     * @return JComponent that should be focused initially.
     */
    fun getPreferredFocusedComponent(): JComponent? {
        return null
    }

    fun isEnabled(): Boolean {
        return activateCheckbox.isSelected
    }

    fun setEnabled(checked: Boolean) {
        activateCheckbox.isSelected = checked
    }

    fun getScrollMode(): ScrollMode {
        return scrollModeDropdown.selectedItem as ScrollMode
    }

    fun setScrollMode(mode: ScrollMode) {
        scrollModeDropdown.selectedItem = mode
    }

    /**
     * Retrieves the user-defined bottom padding in lines.
     * @return Integer value of bottom padding or null if input is not a valid integer.
     */
    fun getBottomLines(): Int? {
        return bottomLines.text.toIntOrNull()
    }

    /**
     * Sets the bottom padding input field.
     * @param lines Integer value for bottom padding.
     */
    fun setBottomLines(lines: Int) {
        bottomLines.text = lines.toString()
    }

    /**
     * Retrieves the user-defined top padding in lines.
     * @return Integer value of top padding or null if input is not a valid integer.
     */
    fun getTopLines(): Int? {
        return topLines.text.toIntOrNull()
    }

    /**
     * Sets the top padding input field.
     * @param lines Integer value for top padding.
     */
    fun setTopLines(lines: Int) {
        topLines.text = lines.toString()
    }
}
