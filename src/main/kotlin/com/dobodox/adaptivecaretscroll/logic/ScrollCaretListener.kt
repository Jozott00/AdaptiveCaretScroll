package com.dobodox.adaptivecaretscroll.logic

import com.dobodox.adaptivecaretscroll.settings.PaddingUnit
import com.dobodox.adaptivecaretscroll.settings.ScrollMode
import com.dobodox.adaptivecaretscroll.settings.ScrollPluginSettings
import com.dobodox.adaptivecaretscroll.settings.ScrollPluginSettingsService
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.editor.event.CaretEvent
import com.intellij.openapi.editor.event.CaretListener
import com.intellij.openapi.editor.event.EditorMouseEvent
import com.intellij.openapi.editor.event.EditorMouseListener
import kotlin.math.abs

/**
 * ScrollCaretListener listens to changes in caret position in an editor and adjusts the scrolling behavior.
 *
 * Additionally, it also listens for mouse down and release events, to handle the scroll on click/drag/selection behaviour.
 */
class ScrollCaretListener : CaretListener, EditorMouseListener {

    // true as long as mouse is pressed.
    // Used to determine click and scroll behaviour.
    private var isMousePressed = false

    /**
     * Called when the caret position changes in the editor.
     * @param event An instance of CaretEvent containing details about the caret position change.
     */
    override fun caretPositionChanged(event: CaretEvent) {
        if (abortAdaptiveScroll(event)) return

        // Check if executing center or custom scroll mode
        if (this.settings().scrollMode == ScrollMode.Centered)
            this.scrollToCenter(event)
        else
            this.scrollCustom(event)
    }

    /**
     * Sets the internal mouse pressed state to true.
     */
    override fun mousePressed(event: EditorMouseEvent) {
        this.isMousePressed = true
        super.mousePressed(event)
    }

    /**
     * This method is called when the mouse is released on the editor.
     * It performs certain actions based on the mouse release event.
     *
     * E.i. it won't trigger a scroll if text is selected. And will explicitly
     * trigger one otherwise.
     *
     * @param event The EditorMouseEvent representing the mouse release event.
     *              It contains information about the editor and the mouse event.
     */
    override fun mouseReleased(event: EditorMouseEvent) {
        super.mouseReleased(event)
        val editor = event.editor

        // Return if there is a selection or if there are multiple carets
        if(!this.settings().enabled || editor.selectionModel.hasSelection() || editor.caretModel.caretCount != 1) {
            this.isMousePressed = false
            return
        }

        // Shift the caret up/down then back to force a caret position changed event.
        // Release the mouse after the first event to prevent a needless double scroll.
        val logicalPosition = editor.caretModel.logicalPosition
        val visibleArea = editor.scrollingModel.visibleArea
        val yLinePosInView = editor.logicalPositionToXY(logicalPosition).y - visibleArea.y

        // If caret in upper half of document, shift down, otherwise shift up
        val inUpperHalf = yLinePosInView < visibleArea.height / 2
        var shift = if (inUpperHalf) 1 else -1

        // Check if in last line of document and in upper half -> no shift. Fixes https://github.com/Jozott00/AdaptiveCaretScroll/issues/4
        if (inUpperHalf && logicalPosition.line + 1 == editor.document.lineCount) shift = 0

        // This event will not trigger a scroll (as internally mouse is still pressed)
        editor.caretModel.moveCaretRelatively(0,shift,false,false,false)

        // Release mouse internally
        this.isMousePressed = false
        // This event will trigger the scroll to the current caret position
        editor.caretModel.moveCaretRelatively(0,-shift,false,false,false)
    }

    // Determine if adaptive scroll takes action or not
    private fun abortAdaptiveScroll(event: CaretEvent): Boolean {
        val settings = settings()
        return isMousePressed || !settings.enabled || !inCaretMovementThreshold(event, settings)
    }

    // Implement scroll to center
    private fun scrollToCenter(event: CaretEvent) {
        val caretModel = event.editor.caretModel
        event.editor.scrollingModel.scrollTo(caretModel.logicalPosition, ScrollType.CENTER)
    }


    // Implement custom scroll behaviour
    private fun scrollCustom(event: CaretEvent) {
        val settings = this.settings()
        val editor = event.editor

        // Get distance of caret to top and bottom of editor in lines
        val (linesToTop, linesToBottom) = getLinesToEdges(editor)

        // if relative calculate lines based on editor's visible area
        val (desiredLinesTop, desiredLinesBottom) = when(settings.paddingUnit) {
            PaddingUnit.Line -> Pair(settings.topDistance, settings.bottomDistance)
            PaddingUnit.Relative -> Pair(
                calculateLinesWithRelativePadding(settings.topDistance, editor),
                calculateLinesWithRelativePadding(settings.bottomDistance, editor)
            )
        }

        // check if paddings collide in center. if so -> use centered scrolling
        val editorVisibleHeight = calculateLinesWithRelativePadding(100, editor)
        if ((desiredLinesTop + desiredLinesBottom) >= editorVisibleHeight) {
            return scrollToCenter(event)
        }

        adjustScrollingPosition(event, desiredLinesTop, linesToTop, true)
        adjustScrollingPosition(event, desiredLinesBottom, linesToBottom, false)
    }



    // Adjust scrolling position based on caret's position and movement direction
    private fun adjustScrollingPosition(event: CaretEvent, desiredLines: Int, linesToEdge: Int, checkForTop: Boolean) {
        val editor = event.editor

        // Check if caret is closer to the edge (top/bottom) than desired and if it is moving in the right direction (upwards/downwards)
        if (linesToEdge < desiredLines && ((checkForTop && event.newPosition < event.oldPosition) || (!checkForTop && event.newPosition > event.oldPosition))) {
            // Calculate how much scrolling is needed
            val deltaY = (linesToEdge - desiredLines) * editor.lineHeight
            // Adjust the current scroll position
            var newScrollY = editor.scrollingModel.verticalScrollOffset
            newScrollY += if (checkForTop) deltaY else -deltaY
            // Scroll to the new position
            editor.scrollingModel.scrollVertically(newScrollY)
        }
    }

    /**
     * Calculates the number of lines with relative padding based on the given padding percentage and editor.
     *
     * @param paddingPercentage the percentage of padding to apply on the visible lines
     * @param editor the editor for which the number of lines is calculated
     * @return the number of lines with relative padding
     */
    private fun calculateLinesWithRelativePadding(paddingPercentage: Int, editor: Editor): Int {
        val relativePadding = paddingPercentage.toFloat() / 100
        val visibleLines = editor.scrollingModel.visibleArea.height / editor.lineHeight
        return (visibleLines * relativePadding).toInt()
    }

    // Returns the number of lines from the caret to the top and bottom edge.
    private fun getLinesToEdges(editor: Editor): Pair<Int, Int> {
        val logicalPosition = editor.caretModel.logicalPosition
        val logicalLineY = editor.logicalPositionToXY(logicalPosition).y
        val visibleArea = editor.scrollingModel.visibleArea

        // Calculate distance from the caret to the bottom and top of the visible area in pixels
        val distanceToBottom = visibleArea.y + visibleArea.height - logicalLineY
        val distanceToTop = logicalLineY - visibleArea.y

        // Convert the distances to line numbers
        val linesToBottom = distanceToBottom / editor.lineHeight
        val linesToTop = distanceToTop / editor.lineHeight
        return Pair(linesToTop, linesToBottom)
    }

    // Get plugin settings
    private fun settings(): ScrollPluginSettings {
        return ScrollPluginSettingsService.getInstance().state
    }

    // Checks if caret moved within threshold
    private fun inCaretMovementThreshold(event: CaretEvent, settings: ScrollPluginSettings): Boolean {
        val maxMovement = settings.topDistance.coerceAtMost(settings.bottomDistance).coerceAtLeast(3)
        val positionChange = abs(event.oldPosition.line - event.newPosition.line)
        return positionChange < maxMovement
    }

}
