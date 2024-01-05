package com.dobodox.adaptivecaretscroll.services

import com.dobodox.adaptivecaretscroll.listeners.EditorScrollManager
import com.dobodox.adaptivecaretscroll.listeners.ScrollCaretListener
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project


/**
 * The [CaretScrollService] class is responsible for managing the caret scroll behavior in the IntelliJ*/
@Service(Service.Level.PROJECT)
class CaretScrollService(val project: Project): Disposable {

    /**
     * A lateinit variable to hold an instance of the EditorScrollManager.
     */
    private lateinit var editorScrollManager: EditorScrollManager

    /**
     * This method is responsible for initializing the CaretScrollService.
     * It fetches all open editors in the current project and attaches a CaretListener
     * to each open editor that is an instance of TextEditor. It also initializes
     * the EditorScrollManager with a ScrollCaretListener.
     */
    fun startup() {
        // Instantiate ScrollCaretListener
        val scrollListener = ScrollCaretListener()

        // Fetch all open editors in the current project
        val fileEditorManager = FileEditorManager.getInstance(project)
        val openEditors = fileEditorManager.allEditors

        // Add the CaretListener to each open editor that is an instance of TextEditor
        for (fileEditor in openEditors) {
            if (fileEditor is TextEditor) {
                attachScrollListenerToEditor(scrollListener, fileEditor.editor)
            }
        }

        // Initialize the EditorScrollManager with the scrollListener
        editorScrollManager = EditorScrollManager(scrollListener)
        EditorFactory.getInstance()
            .addEditorFactoryListener(editorScrollManager, this)

    }

    /**
     * Attaches a scroll listener to the given editor.
     *
     * This method adds a [ScrollCaretListener] to the caret model of the editor and also adds an editor mouse listener
     * to the editor. Whenever the caret is updated or the editor receives mouse events, the scroll listener will be notified.
     *
     * @param scrollListener The scroll listener to be attached.
     * @param editor The editor to attach the scroll listener to.
     */
    fun attachScrollListenerToEditor(scrollListener: ScrollCaretListener, editor: Editor) {
        editor.caretModel.addCaretListener(scrollListener, this)
        editor.addEditorMouseListener(scrollListener, this)
    }


    override fun dispose() {
        // Nothing to dispose
    }

}