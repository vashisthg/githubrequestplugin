package com.vashisthg.githubstatus

import jdk.internal.util.xml.impl.Input
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.Project


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection

class GithubStatusTask extends DefaultTask {

    @Input
    String status;

    @TaskAction
    def exec () {
        println "running githubstatus task"
        sendRequest(project)
    }

    void sendRequest(Project project) {
        GithubUtils.sendRequest(project,  status)

    }
}