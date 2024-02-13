package com.decide;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
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
import org.apache.maven.shared.invoker.InvocationResult;
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

            // Execute the Maven command
            InvocationResult invocationResult = invoker.execute(invocationRequest);

            // Get the exit code from the result
            int exitCode = invocationResult.getExitCode();

            // Check if compilation and testing succeeded
            boolean compilationAndTestingSucceeded = exitCode == 0;

            // Parse the Maven output to extract information about tests
            String mavenOutput = IOUtils.toString(new FileInputStream("../git/output.txt"), StandardCharsets.UTF_8);

            // Extract information about the number of tests run and failed
            int testsRun = extractTestsRun(mavenOutput);
            int testsFailed = extractTestsFailed(mavenOutput);

            // Print information to the console
            System.out.println("Branch triggered: " + branch);
            System.out.println("User who made the commit: " + json.getJSONObject("pusher").getString("name"));
            System.out.println("Compilation and testing succeeded: " + compilationAndTestingSucceeded);
            System.out.println("Tests run: " + testsRun);
            System.out.println("Tests failed: " + testsFailed);
            
            // Notify these results (PROPERTY 3)
            // Get sha
            String commitSHA = json.getJSONObject("head_commit").getString("id");
            // Chosse between error, failure, pending, or success
            String state = "success";
            Notification.setStatus(state, commitSHA);

            // Send response to GitHub
            response.getWriter().println("CI job done");
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
    * Extracts the number of tests run from the Maven output.
    *
    * @param output The Maven output as a string.
    * @return The number of tests run.
    */
    private static int extractTestsRun(String mavenOutput) {
        String testsRunLine = findLineContaining(mavenOutput, "Tests run:");
        return extractNumberFromLine(testsRunLine);
    }

    /**
     * Extracts the number of tests failed from the Maven output.
     *
     * @param output The Maven output as a string.
     * @return The number of tests failed.
     */
    private static int extractTestsFailed(String mavenOutput) {
        String testsFailedLine = findLineContaining(mavenOutput, "Failures:");
        return extractNumberFromLine(testsFailedLine);
    }

    /**
     * Finds a line in the output that contains a specific keyword.
     *
     * @param output  The Maven output as a string.
     * @param keyword The keyword to search for in the output.
     * @return The line containing the keyword.
     */
    private static String findLineContaining(String text, String substring) {
        String[] lines = text.split("\\r?\\n");
        boolean resultsFound = false;
        for (String line : lines) {
            if (line.contains("Results :")){
                System.out.println("Line: " + line);
                resultsFound = true;
            }
            else if (resultsFound&&line.contains(substring)) {
                System.out.println("Line: " + line);
                return line;
            }
        }
        return "";
    }

    /**
     * Extracts a number from a line of text.
     *
     * @param line The line containing the number.
     * @return The extracted number.
     */
    private static int extractNumberFromLine(String line) {
        // Assuming the line contains the number as the last element (e.g., "Tests run: 42")
        String[] parts = line.split("\\D+");
        for (String part : parts) {
            if (!part.isEmpty()) {
                return Integer.parseInt(part);
            }
        }
        return 0;
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
