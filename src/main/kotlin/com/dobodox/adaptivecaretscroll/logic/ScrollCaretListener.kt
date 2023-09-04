package com.dobodox.adaptivecaretscroll.logic

import com.dobodox.adaptivecaretscroll.settings.ScrollMode
import com.dobodox.adaptivecaretscroll.settings.ScrollPluginSettings
import com.dobodox.adaptivecaretscroll.settings.ScrollPluginSettingsService
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.editor.event.CaretEvent
import com.intellij.openapi.editor.event.CaretListener
import kotlin.math.abs

/**
 * ScrollCaretListener listens to changes in caret position in an editor and adjusts the scrolling behavior.
 */
class ScrollCaretListener : CaretListener {

    /**
     * Called when the caret position changes in the editor.
     * @param event An instance of CaretEvent containing details about the caret position change.
     */
    override fun caretPositionChanged(event: CaretEvent) {
        // Retrieve user-defined settings for the distance to the top and bottom
        val settings = ScrollPluginSettingsService.getInstance().state
        // do nothing if disabled
        if (!settings.enabled) return

        val editor = event.editor
        val caretModel = editor.caretModel
        val scrollingModel = editor.scrollingModel

        if (settings.scrollMode == ScrollMode.Centered) {
            scrollingModel.scrollTo(caretModel.logicalPosition, ScrollType.CENTER)
            return
        }

        if (!inCaretMovementThreshold(event, settings)) return

        val logicalPosition = caretModel.logicalPosition
        val logicalLineY = editor.logicalPositionToXY(logicalPosition).y
        val visibleArea = scrollingModel.visibleArea

        // Calculate distance from the caret to the bottom and top of the visible area in pixels
        val distanceToBottom = visibleArea.y + visibleArea.height - logicalLineY
        val distanceToTop = logicalLineY - visibleArea.y

        // Convert the distances to line numbers
        val linesToBottom = distanceToBottom / editor.lineHeight
        val linesToTop = distanceToTop / editor.lineHeight


        val desiredLinesToBottom = settings.bottomDistance
        val desiredLinesToTop = settings.topDistance

        // Adjust scrolling position when the caret is closer to the top than desired and is moving upwards
        if (linesToTop < desiredLinesToTop && event.newPosition < event.oldPosition) {
            val deltaY = (linesToTop - desiredLinesToTop) * editor.lineHeight
            val newScrollY = scrollingModel.verticalScrollOffset + deltaY
            scrollingModel.scrollVertically(newScrollY)
        }

        // Adjust scrolling position when the caret is closer to the bottom than desired and is moving downwards
        if (linesToBottom < desiredLinesToBottom && event.newPosition > event.oldPosition) {
            val deltaY = (linesToBottom - desiredLinesToBottom) * editor.lineHeight
            val newScrollY = scrollingModel.verticalScrollOffset - deltaY
            scrollingModel.scrollVertically(newScrollY)
        }
    }

    private fun inCaretMovementThreshold(event: CaretEvent, settings: ScrollPluginSettings): Boolean {
        val maxMovement = settings.topDistance.coerceAtMost(settings.bottomDistance).coerceAtLeast(3)
        val positionChange = abs(event.oldPosition.line - event.newPosition.line)
        return positionChange < maxMovement
    }
}
