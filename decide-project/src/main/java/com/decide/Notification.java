package com.decide;

import java.io.IOException;
import java.net.http.*;
import java.net.URI;

/**
 * Class for Notification of Build information from CI server through GitHub
 * commit status.
 */
public class Notification {

  private static HttpClient client = HttpClient.newHttpClient();

  /**
   * Sets the commit status of a payload on GitHub
   * 
   * @param state The state of the build, either {error, failure, pending, or
   *              success}
   * @param sha   Sha of the commit
   *
   */
  public static void setStatus(String state, String sha) {
    try {
      // Insert token here
      String token = "INSERT_TOKEN_HERE";
      // Create the URL for setting status
      URI uri = new URI("https://api.github.com/repos/DD2480-Group-23/Group23-CI/statuses/" + sha);
      // Create JSON payload
      String json = "{\"state\":\"" + state + "\"}";
      // Send HTTP request
      HttpResponse<String> response = client.send(createRequest(json, uri, token),
          HttpResponse.BodyHandlers.ofString());
      // If the status was changed successfully, the status code will be 201
      if (response.statusCode() == 201) {
        System.out.println("Successfully set status");
      } else {
        System.out.println("Failed to set status: " + response.statusCode());
      }
    } catch (Exception e) {
      System.out.println("Failed to set status");
    }
  }

  /**
   * Creates a HttpRequest from JSON.
   * 
   * @param json  Json containing commit status state
   * @param uri   URI for the repo
   * @param token GitHub token
   */

  public static HttpRequest createRequest(String json, URI uri, String token) {
    // Create HTTP request, POST since we are submitting data to the server
    // https://docs.github.com/en/rest/commits/statuses?apiVersion=2022-11-28
    HttpRequest request = HttpRequest.newBuilder()
        .uri(uri)
        .header("Authorization", "Bearer " + token)
        .header("Accept", "application/vnd.github+json")
        .POST(HttpRequest.BodyPublishers.ofString(json))
        .build();
    return request;
  }

  /**
   * For Testing purposes. Set a mock client as client.
   * 
   * @param mockClient The mock client.
   */
  public static void setHttpClient(HttpClient mockClient) {
    client = mockClient;
  }
}
