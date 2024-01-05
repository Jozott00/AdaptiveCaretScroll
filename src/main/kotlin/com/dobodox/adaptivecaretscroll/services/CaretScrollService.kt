package com.dobodox.adaptivecaretscroll.services

import com.dobodox.adaptivecaretscroll.logic.EditorScrollManager
import com.dobodox.adaptivecaretscroll.logic.ScrollCaretListener
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project


@Service(Service.Level.PROJECT)
class CaretScrollService(val project: Project): Disposable {

    /**
     * A lateinit variable to hold an instance of the EditorScrollManager.
     */
    private lateinit var editorScrollManager: EditorScrollManager

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

    fun attachScrollListenerToEditor(scrollListener: ScrollCaretListener, editor: Editor) {
        editor.caretModel.addCaretListener(scrollListener, this)
        editor.addEditorMouseListener(scrollListener, this)
    }

    override fun dispose() {
        // Nothing to dispose
    }

}