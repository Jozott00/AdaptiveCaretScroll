package com.dobodox.adaptivecaretscroll

import com.dobodox.adaptivecaretscroll.services.CaretScrollService
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

/**
 * ScrollPluginStartup is responsible for initializing the Adaptive Caret Scroll plugin
 * upon the startup of an IntelliJ IDEA project. It extends the StartupActivity interface.
 */
class ScrollPluginStartup : StartupActivity {

    /**
     * This method is invoked when the IntelliJ IDEA project starts up.
     * It calls the project service to manage everything.
     *
     * @param project The project that is being opened.
     */
    override fun runActivity(project: Project) {
        project.service<CaretScrollService>().startup()
    }
}
