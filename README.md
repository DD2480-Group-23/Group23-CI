# Assignment 2: Continuous Integration

## Group23-CI DD2480

### Requirements

- ngrok
- Maven

### First Time Setup

1.  Make sure you have <a href = https://dashboard.ngrok.com/get-started/setup>ngrok</a> and <a href = https://maven.apache.org/what-is-maven.html>Maven</a> installed.

    - MacOS with brew: `brew install ngrok/ngrok/ngrok` and `brew install maven`

    - When ngrok is installed, claim your free "Domain" at ngrok.com.

2.  Create a webhook on the repo with your ngrok domain

    - In the repo, go to Settings tab > Webhooks.
    - Click "Add webhook".
    - Adjust following fields:
      - Payload URL: https://YOURDOMAINNAME.ngrok-free.app
      - Content Type: application/json
    - Add the webhook.

### Start the CI server

1. In a separate terminal, start your ngrok tunnel with
   `ngrok http --domain=YOURDOMAINNAME.ngrok-free.app 8023`. (Can be Shut down with Ctrl + C)

2. Make sure you are located in the `decide-project` directory, then compile and build the project with `mvn clean install`

3. Start the server with `mvn exec:java`. (Can be Shut down with Ctrl + C)

### Test the CI server

1. Make sure ngrok and the server is running.

2. Create a new branch on Github.com that will be temporarly used for testing. (Or use an existing one!)

3. Commit a change to this branch. This is best done by changing the README in the GitHub web-UI. (Make sure you're in the right branch!)

4. The server window should now notify you that a new payload has arrived, and then when the maven command has finsished executed.

5. The cloned repo for that commit is now in the newly created `git` directory. The build log (including compilation and testing status) is in `git/output.txt`

6. Delete your testing branch when done!

## How notifications are implemented and tested

- Notifications are implemented through changing status of git commits, this is done through the REST API and HTTP. Note that a token needs to be inserted manually.
- Testing is done through a test of the HTTP request body.

### Statement of contributions

The work was divided equally between members, as shown below. Meetings were held to plan the work and to evaluate the work, which all team members attended.

_Jonatan_: Start Maven project, create barebones CI server, initialize webhooks, add Git and Build (compile and test) functionalities to the CI server

_Felix_: Start Maven project, create and start barebones CI server, initialize webhooks, add Git and Build (compile and test) functionalities to the CI server
 
_Rasmus_: Notifications and Unit Testing for Notifications

_Benjamin_: State information in the CI server's console about compilation and testing of the affected branch when a webhook triggers
