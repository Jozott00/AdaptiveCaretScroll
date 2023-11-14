package com.dobodox.adaptivecaretscroll.settings

/**
 * Data class representing the configurable settings for the Adaptive Caret Scroll plugin.
 *
 * @property bottomDistance The number of lines to maintain between the caret and the bottom edge of the editor.
 * @property topDistance The number of lines to maintain between the caret and the top edge of the editor.
 */
data class ScrollPluginSettings(
    var enabled: Boolean = true,
    var scrollMode: ScrollMode = ScrollMode.Custom,
    var paddingUnit: PaddingUnit = PaddingUnit.Line,
    var bottomDistance: Int = 10,
    var topDistance: Int = 10,

)

enum class ScrollMode {
    Centered,
    Custom
}

enum class PaddingUnit(var str: String) {
    Line("Line"),
    Relative("Relative (%)"),;

    override fun toString(): String {
        return str
    }
}