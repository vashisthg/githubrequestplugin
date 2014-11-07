package com.vashisthg.githubstatus

class GithubStatusExtension {
    String token = ""
    String owner = ""
    String repository = ""
    String successMessage = "The build Succeeded!"
    String failureMessage = "The build Failed!"
    String errorMessage = "The build Errored!"
    String pendingMessage = "The build is Running!"
    String context = "Continuous Integration"
}