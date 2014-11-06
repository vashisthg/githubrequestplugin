package com.vashisthg.githubstatus

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection

class GithubStatusTask extends DefaultTask {



    String gitHubToken;


    @TaskAction
    def exec () {
        println "running githubstatus task"
        sendRequest()

    }

    @Input
    String getCommit () {
        "git rev-parse HEAD".execute([], project.rootDir).text.trim()
    }

    @Input
    String getDiff() {
        "git diff --porcelain".execute([], project.rootDir).text.trim()
    }

    void sendRequest() {





        gitHubToken = project.githubstatus.token;

        if (gitHubToken == null || gitHubToken.isEmpty()) {
            throw new IllegalArgumentException(" API Token is missing")
        }


        String url = "https://api.github.com/repos/vashisthg/pullrequestplugin/statuses/" + getCommit();
        URL obj = new URL(url);

        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla");
        con.setRequestProperty("Authorization", "token " + gitHubToken);
        String urlParameters =
                "{\"state\": \"error\", \"target_url\": \"https://example.com/build/status\", \"description\": \"The build succeeded!\",\"context\": \"continuous-integration/jenkins\"}";
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
        System.out.println("Token: " + gitHubToken)

//        BufferedReader in = new BufferedReader(
//                new InputStreamReader(con.getInputStream()));
//        String inputLine;
//        StringBuffer response = new StringBuffer();
//
//        while ((inputLine = in.readLine()) != null) {
//            response.append(inputLine);
//        }
//        in.close();
//
//        //print result
//        System.out.println(response.toString());
    }


}