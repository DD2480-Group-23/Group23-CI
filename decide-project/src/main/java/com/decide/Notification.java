package com.decide;

import java.io.IOException;
import java.net.http.*;
import java.net.URI;

public class Notification {

    /*
    * @param state
    * @param shared
    * @param repo
    *
     */
    public static boolean setStatus(String state, String sha){
        try{

            String token = "";

            // Construct the URL for setting status
            URI uri = new URI("https://api.github.com/repos/DD2480-Group-23/Group23-CI/statuses/" + sha);

            // Create JSON payload
            String json = "{\"state\":\"" + state + "\"}";

            // Create HTTP client
            HttpClient client = HttpClient.newHttpClient();

            // Create HTTP request, POST since we are submitting data to the server
            // https://docs.github.com/en/rest/commits/statuses?apiVersion=2022-11-28
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Authorization", "Bearer " + token)
                    .header("Accept", "application/vnd.github+json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            System.out.println("Hej1");

            // Send HTTP request
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Hej2");

            // If we successfully changed the status, the status code will be 201
            if (response.statusCode() == 201) {
                return true;
            } else {
                //System.out.println("Failed to set status: " + response.statusCode());
                return false;
            }
        }
        catch(Exception e){
          return false;
        }
    }
}
