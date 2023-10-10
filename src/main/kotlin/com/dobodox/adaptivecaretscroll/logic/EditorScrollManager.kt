package com.dobodox.adaptivecaretscroll.logic

import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener

/**
 * EditorScrollManager is responsible for managing the scroll behavior of editors in the IntelliJ/CLion environment.
 * It listens to the creation and release events of editors and attaches or detaches a ScrollCaretListener accordingly.
 *
 * @param scrollListener The ScrollCaretListener instance that will be added to or removed from the editor's CaretModel.
 */
class EditorScrollManager(private val scrollListener: ScrollCaretListener) : EditorFactoryListener, Disposable {

    init {
        // Register this instance as a listener for editor creation and release events
        val editorFactory = EditorFactory.getInstance()
        editorFactory.addEditorFactoryListener(this, this)
    }

    companion object {
        fun attachToEditor( scrollListener: ScrollCaretListener, editor: Editor) {
            editor.caretModel.addCaretListener(scrollListener)
            editor.contentComponent.addMouseListener(scrollListener)
        }
    }


    /**
     * Called when a new editor is created.
     * @param event An instance of EditorFactoryEvent containing details about the newly created editor.
     */
    override fun editorCreated(event: EditorFactoryEvent) {
        val editor = event.editor
        // Attach the scrollListener to the newly created editor
        attachToEditor(scrollListener, editor)
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
        editor.contentComponent.removeMouseListener(scrollListener)
    }

    /**
     * Called when resources should be released, although no resources are currently being managed.
     */
    override fun dispose() {
        // No resources to release
    }
}
