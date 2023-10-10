package com.dobodox.adaptivecaretscroll

import com.dobodox.adaptivecaretscroll.logic.EditorScrollManager
import com.dobodox.adaptivecaretscroll.logic.ScrollCaretListener
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

/**
 * ScrollPluginStartup is responsible for initializing the Adaptive Caret Scroll plugin
 * upon the startup of an IntelliJ IDEA project. It extends the StartupActivity interface.
 */
class ScrollPluginStartup : StartupActivity {

    /**
     * A lateinit variable to hold an instance of the EditorScrollManager.
     */
    lateinit var editorScrollManager: EditorScrollManager

    /**
     * This method is invoked when the IntelliJ IDEA project starts up.
     * It initializes the CaretListener for all open editors.
     *
     * @param project The project that is being opened.
     */
    override fun runActivity(project: Project) {

        // Instantiate ScrollCaretListener
        val scrollListener = ScrollCaretListener()

        // Fetch all open editors in the current project
        val fileEditorManager = FileEditorManager.getInstance(project)
        val openEditors = fileEditorManager.allEditors

        // Add the CaretListener to each open editor that is an instance of TextEditor
        for (fileEditor in openEditors) {
            if (fileEditor is TextEditor) {
                EditorScrollManager.attachToEditor(scrollListener, fileEditor.editor)
            }
        }

        // Initialize the EditorScrollManager with the scrollListener
        editorScrollManager = EditorScrollManager(scrollListener)
    }
}
