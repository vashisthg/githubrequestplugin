package com.vashisthg.githubstatus

import javax.net.ssl.HttpsURLConnection
import org.gradle.api.Project


class GithubUtils {

    public static final String BASE_URL = "https://api.github.com";
    public static final String STATUS_URL_FORMAT = BASE_URL + "/repos/%s/%s/statuses/%s";

    public static String stateError = "error";
    public static String statePending = "pending"
    public static String stateSuccess = "success"
    public static String stateFailure = "failure"


    static String getGithubPostBody(Project project, String state, String targetUrl) {
        System.out.println("creating github post body");

        String description = "";

        if(stateSuccess.equals(state)) {
            description = project.githubstatus.successMessage;
        } else if (stateError.equals(state)) {
            description = project.githubstatus.errorMessage
        } else if (statePending.equals(state)) {
            description = project.githubstatus.pendingMessage
        } else if (stateFailure.equals(state)) {
            description = project.githubstatus.failureMessage
        }



        String context = project.githubstatus.context;

        String postBody =
                "{\"state\": \"" + state + "\"," +
                " \"target_url\": \"" + targetUrl + "\", " +
                " \"description\": \"" + description + "\"," +
                " \"context\": \"" + context + "\"}";

        return postBody
    }


    static String getGithubPostBodyForSuccess(Project project,  String targetUrl) {
        return  getGithubPostBody(project, stateSuccess, targetUrl)
    }

    static String getGithubPostBodyForFailure(Project project,  String targetUrl) {
        return  getGithubPostBody(project, stateFailure, targetUrl)
    }

    static String getGithubPostBodyForPending(Project project,  String targetUrl) {
        return  getGithubPostBody(project, statePending, targetUrl)
    }

    static String getGithubPostBodyForError(Project project,  String targetUrl) {
        return  getGithubPostBody(project, stateError, targetUrl)
    }

    static String getCommit (Project project) {
        "git rev-parse HEAD".execute([], project.rootDir).text.trim()
    }

    static String getDiff(Project project) {
        "git diff --porcelain".execute([], project.rootDir).text.trim()
    }

    static String sendRequest(Project project, String state) {
        System.out.println("in sendRequest " + state);

        String gitHubToken = project.githubstatus.token;
        String owner = project.githubstatus.owner;
        String repositoryName = project.githubstatus.repository;

        if (gitHubToken == null || gitHubToken.isEmpty()) {
            throw new IllegalArgumentException(" API Token is missing")
        }

        String url = String.format(STATUS_URL_FORMAT, owner, repositoryName, getCommit(project));
        URL obj = new URL(url);

        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "HttpURLConnection");
        con.setRequestProperty("Authorization", "token " + gitHubToken);

        String targetUrl = ""; // TODO

        String urlParameters = GithubUtils.getGithubPostBody(project, state, targetUrl)

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);
        System.out.println("Token: " + gitHubToken);


        InputStream exportTemplateStream
        if (isResponseCodeValid(responseCode)) {
            exportTemplateStream = con.getInputStream();
            System.out.println(getText(exportTemplateStream))
        } else {
            exportTemplateStream = con.errorStream
            throw new IllegalArgumentException(getText(exportTemplateStream))
        }
    }

    static String getText(InputStream inputStream) {
        assert inputStream: "inputstream not found"
        return inputStream.text
    }

    static boolean isResponseCodeValid(int responseCode) {
        return responseCode >= 200 && responseCode < 300
    }
}