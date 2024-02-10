package com.decide;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import org.eclipse.jgit.api.Git;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationOutputHandler;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.PrintStreamHandler;

/**
 * Continuous Integration Server for GitHub webhooks
 *
 * @author Benjamin Jansson Mbonyimana
 * @author Isadora Fukiat Winter
 * @author Felix Sjögren
 * @author Jonatan Stagge
 * @author Rasmus Craelius
 */
public class ContinuousIntegrationServer extends AbstractHandler {

    /**
     * Handles an incoming webhook request.
     * Executes "mvn clean install" on the branch the webhook was activated.
     * This compiles and tests the code.
     *
     * @param target      The target of the request - either a URI or a name.
     * @param baseRequest The original unwrapped request object.
     * @param request     The request either as the {@link Request} object or a
     *                    wrapper of that request.
     * @param response    The response as the {@link Response} object or a wrapper
     *                    of that request.
     * @throws IOException
     * @throws ServletException
     */
    public void handle(String target,
            Request baseRequest,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        System.out.println("New payload arrived");
        String payload = IOUtils.toString(request.getReader());
        try {
            JSONObject json = new JSONObject(payload);
            // The branch the commit was pushed
            String branch = json.get("ref").toString().substring(11);
            // Local directory where the repository will be cloned
            String localPath = "../git";
            // URL of remote repository
            String remoteURL = json.getJSONObject("repository").get("clone_url").toString();
            System.out.println(remoteURL);
            // Remove previous repository clone
            File folder = new File(localPath);
            deleteDirectory(folder);

            // Clone the repository of the branch that launched the webhook
            Git.cloneRepository()
                    .setURI(remoteURL)
                    .setDirectory(new File(localPath))
                    .setBranch(branch)
                    .call();

            // Build project from the cloned repository
            // Specify the base directory of the Maven project
            File baseDirectory = new File("../git/decide-project");

            // Create an invocation request
            InvocationRequest invocationRequest = new DefaultInvocationRequest();
            invocationRequest.setBaseDirectory(baseDirectory);
            invocationRequest.setGoals(Arrays.asList("clean", "install")); // Specify the goals (clean and install)
            // Create an invoker
            DefaultInvoker invoker = new DefaultInvoker();

            // Make invoker print output to file
            InvocationOutputHandler outputHandler = new PrintStreamHandler(
                    new PrintStream(new File("../git/output.txt")),
                    true);

            // Execute the Maven command
            invoker.setOutputHandler(outputHandler).execute(invocationRequest);
            System.out.println("Maven command executed successfully.");

            // Print information to the console
            System.out.println("Branch triggered: " + branch);
            System.out.println("User who made the commit: " + json.getJSONObject("pusher").getString("name"));

            // Notify these results (PROPERTY 3)
            // Get sha
            String commitSHA = json.getJSONObject("head_commit").getString("id");
            Notification.setStatus("pending", commitSHA);

            // Send response to GitHub
            response.getWriter().println("CI job done");
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Deletes a directory and all of its content.
     *
     * @param directory The directory to be deleted
     * @return true if success.
     */
    public static boolean deleteDirectory(File directory) {
        // Check if the given File object represents a directory
        if (!directory.isDirectory()) {
            return false;
        }

        // Get all files and subdirectories inside the directory
        File[] allFiles = directory.listFiles();

        // Delete all files and subdirectories recursively
        if (allFiles != null) {
            for (File file : allFiles) {
                if (file.isDirectory()) {
                    // Recursively delete subdirectories
                    deleteDirectory(file);
                } else {
                    // Delete files
                    file.delete();
                }
            }
        }

        // Finally, delete the empty directory itself
        return directory.delete();
    }

    /**
     * Start the CI server
     *
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Server server = new Server(8023);
        server.setHandler(new ContinuousIntegrationServer());
        server.start();
        server.join();
    }
}
