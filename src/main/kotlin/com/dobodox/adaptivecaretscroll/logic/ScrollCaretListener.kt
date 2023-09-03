package com.dobodox.adaptivecaretscroll.logic

import com.dobodox.adaptivecaretscroll.settings.ScrollPluginSettingsService
import com.intellij.openapi.editor.ScrollingModel
import com.intellij.openapi.editor.event.CaretEvent
import com.intellij.openapi.editor.event.CaretListener

/**
 * ScrollCaretListener listens to changes in caret position in an editor and adjusts the scrolling behavior.
 */
class ScrollCaretListener : CaretListener {

    /**
     * Called when the caret position changes in the editor.
     * @param event An instance of CaretEvent containing details about the caret position change.
     */
    override fun caretPositionChanged(event: CaretEvent) {
        val editor = event.editor
        val caretModel = editor.caretModel
        val scrollingModel = editor.scrollingModel

        val logicalPosition = caretModel.logicalPosition
        val logicalLineY = editor.logicalPositionToXY(logicalPosition).y
        val visibleArea = scrollingModel.visibleArea

        // Calculate distance from the caret to the bottom and top of the visible area in pixels
        val distanceToBottom = visibleArea.y + visibleArea.height - logicalLineY
        val distanceToTop = logicalLineY - visibleArea.y

        // Convert the distances to line numbers
        val linesToBottom = distanceToBottom / editor.lineHeight
        val linesToTop = distanceToTop / editor.lineHeight

        // Retrieve user-defined settings for the distance to the top and bottom
        val settings = ScrollPluginSettingsService.getInstance().state
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

    /**
     * Adjusts the vertical scroll position.
     * @param scrollingModel The ScrollingModel instance of the editor.
     * @param lineHeight The height of a line in the editor.
     * @param currentLines The current number of lines between the caret and the edge.
     * @param desiredLines The desired number of lines between the caret and the edge.
     */
    private fun adjustScrolling(scrollingModel: ScrollingModel, lineHeight: Int, currentLines: Int, desiredLines: Int) {
        val deltaY = (currentLines - desiredLines) * lineHeight
        val newScrollY = scrollingModel.verticalScrollOffset - deltaY
        scrollingModel.scrollVertically(newScrollY)
    }
}
