package com.decide;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.lang.reflect.Parameter;

import org.junit.Rule;
import org.junit.rules.Timeout;
import org.w3c.dom.views.DocumentView;

import java.net.URI;
import java.net.http.HttpRequest;

import java.io.IOException;

import java.util.*;

/**
 * Test Class for Notification function.
 *
 */

 public class NotificationTest {

    @Test
    public void tesHTTPRequest(){
        String sha = "test";
        String state = "success";
        String json = "{\"state\":\"" + state + "\"}";
        HttpRequest request = Notification.createRequest(sha, URI.create("https://api.github.com/repos/DD2480-Group-23/Group23-CI/statuses/test"), "TestToken");
        // Create request
        HttpRequest requestCheck = HttpRequest.newBuilder()
                .uri(URI.create("https://api.github.com/repos/DD2480-Group-23/Group23-CI/statuses/test"))
                .header("Authorization", "Bearer " + "TestToken")
                .header("Accept", "application/vnd.github+json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"state\":\"success\"}"))
                .build();
        assertTrue(request.equals(requestCheck));
    }
}
