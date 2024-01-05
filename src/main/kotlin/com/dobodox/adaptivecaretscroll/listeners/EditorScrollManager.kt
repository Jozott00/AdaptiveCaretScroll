package com.dobodox.adaptivecaretscroll.listeners

import com.dobodox.adaptivecaretscroll.services.CaretScrollService
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener

/**
 * EditorScrollManager is responsible for managing the scroll behavior of editors in the IntelliJ/CLion environment.
 * It listens to the creation and release events of editors and attaches or detaches a ScrollCaretListener accordingly.
 *
 * @param scrollListener The ScrollCaretListener instance that will be added to or removed from the editor's CaretModel.
 */

class EditorScrollManager(private val scrollListener: ScrollCaretListener) : EditorFactoryListener {


    /**
     * Called when a new editor is created.
     * @param event An instance of EditorFactoryEvent containing details about the newly created editor.
     */
    override fun editorCreated(event: EditorFactoryEvent) {
        val editor = event.editor
        // Attach the scrollListener to the newly created editor
        editor.project?.service<CaretScrollService>()
            ?.attachScrollListenerToEditor(scrollListener, editor)
    }

    /**
     * Called when an existing editor is released.
     * @param event An instance of EditorFactoryEvent containing details about the released editor.
     */
    override fun editorReleased(event: EditorFactoryEvent) {
        val editor = event.editor
        val caretModel = editor.caretModel
        // Remove the scrollListener from the released editor
        caretModel.removeCaretListener(scrollListener)
    }

}
