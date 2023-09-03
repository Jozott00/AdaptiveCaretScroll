package com.dobodox.adaptivecaretscroll.settings

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.fields.IntegerField
import com.intellij.util.ui.FormBuilder
import java.awt.FlowLayout
import javax.swing.JComponent
import javax.swing.JPanel


/**
 * PluginSettingsComponent is responsible for creating and managing the GUI form
 * for setting the scroll padding at the top and bottom of the editor.
 */
class PluginSettingsComponent {

    var panel: JPanel? = null
    private val topLines = IntegerField()
    private val bottomLines = IntegerField()

    init {
        // Lambda function for creating a JPanel with an IntegerField and a label
        val input = { field: IntegerField ->
            val flowLayoutPanel = JPanel(FlowLayout(FlowLayout.LEFT, 0, 0))
            flowLayoutPanel.add(field)
            flowLayoutPanel.add(JBLabel(" lines"))
            flowLayoutPanel
        }

        // Create form using FormBuilder
        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Top padding:"), input(topLines), 1, false)
            .addLabeledComponent(JBLabel("Bottom padding:"), input(bottomLines), 1, false)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    /**
     * Returns the component that should receive focus initially.
     * @return JComponent that should be focused initially.
     */
    fun getPreferredFocusedComponent(): JComponent {
        return topLines
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
