package com.vashisthg.githubstatus

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.tasks.TaskInstantiationException
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin

class GithubStatusPlugin implements Plugin<Project> {
    void apply(Project project) {
        verifyRequiredPlugins project
        applyExtensions(project)
        applyTasks(project)
    }

    // check if 'android' plugin is applied to the project
    private static void verifyRequiredPlugins(Project project) {
        if (!project.plugins.hasPlugin(AppPlugin) && !project.plugins.hasPlugin(LibraryPlugin)) {
            throw new TaskInstantiationException("'android' or 'android-library' plugin has to be applied before")
        }
    }

    void applyExtensions(final Project project) {
        project.extensions.create("githubstatus", GithubStatusExtension)
    }

    void applyTasks(final Project project) {
        GithubStatusTask successTask = project.tasks.create("setGithubStatusSuccess", GithubStatusTask) {
            status = GithubUtils.stateSuccess
        }
        GithubStatusTask failureTask = project.tasks.create("setGithubStatusFailure", GithubStatusTask) {
            status = GithubUtils.stateFailure
        }
        GithubStatusTask pendingTask = project.tasks.create("setGithubStatusPending", GithubStatusTask) {
            status = GithubUtils.statePending
        }
        GithubStatusTask errorTask = project.tasks.create("setGithubStatusError", GithubStatusTask) {
            status = GithubUtils.stateError
        }
    }
}
