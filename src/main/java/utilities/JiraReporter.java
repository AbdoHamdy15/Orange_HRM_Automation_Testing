package utilities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class JiraReporter implements AutoCloseable {

    private final HttpClient httpClient;
    private final String jiraUrl;
    private final String authHeader;
    private final ObjectMapper objectMapper;

    public JiraReporter(String jiraUrl, String email, String apiToken) {
        this.jiraUrl = jiraUrl;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        
        // Create Basic Auth header
        String auth = email + ":" + apiToken;
        this.authHeader = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());
        
        LogsUtil.info("JiraReporter initialized successfully for: " + jiraUrl);
    }

    public String createIssue(String projectKey, String summary, String description) {
        try {
            // Build JSON payload
            ObjectNode issueJson = objectMapper.createObjectNode();
            ObjectNode fields = issueJson.putObject("fields");
            
            // Project
            ObjectNode project = fields.putObject("project");
            project.put("key", projectKey);
            
            // Issue type (Bug)
            ObjectNode issueType = fields.putObject("issuetype");
            issueType.put("name", "Bug");
            
            // Summary and description
            fields.put("summary", summary);
            fields.put("description", description);

            // Create HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(jiraUrl + "/rest/api/2/issue"))
                    .header("Authorization", authHeader)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(issueJson.toString()))
                    .build();

            // Send request
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                JsonNode responseJson = objectMapper.readTree(response.body());
                String issueKey = responseJson.get("key").asText();
                LogsUtil.info("Created JIRA issue: " + issueKey);
                return issueKey;
            } else {
                LogsUtil.error("Failed to create JIRA issue. Status: " + response.statusCode() + ", Response: " + response.body());
                return null;
            }
        } catch (Exception e) {
            LogsUtil.error("Failed to create JIRA issue: " + e.getMessage());
            return null;
        }
    }

    public void reportBug(String projectKey, String summary, String description) {
        createIssue(projectKey, summary, description);
    }

    public void reportBug(String projectKey, String summary, String description, String pngFilePath) {
        String issueKey = createIssue(projectKey, summary, description);
        if (issueKey != null && pngFilePath != null) {
            attachFile(issueKey, pngFilePath);
        }
    }

    public void reportBugWithMultipleAttachments(String projectKey, String summary, String description, String... filePaths) {
        String issueKey = createIssue(projectKey, summary, description);
        if (issueKey != null && filePaths != null) {
            for (String filePath : filePaths) {
                attachFile(issueKey, filePath);
            }
        }
    }

    private void attachFile(String issueKey, String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                LogsUtil.warn("File does not exist: " + filePath);
                return;
            }

            // Read file content
            byte[] fileContent = Files.readAllBytes(Path.of(filePath));
            String fileName = Paths.get(filePath).getFileName().toString();

            // Create multipart request with proper binary handling
            String boundary = "boundary" + System.currentTimeMillis();
            byte[] multipartBody = buildMultipartBody(fileName, fileContent, boundary);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(jiraUrl + "/rest/api/2/issue/" + issueKey + "/attachments"))
                    .header("Authorization", authHeader)
                    .header("X-Atlassian-Token", "no-check")
                    .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(multipartBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                LogsUtil.info("Attached file: " + fileName + " to issue: " + issueKey);
            } else {
                LogsUtil.error("Failed to attach file " + fileName + ". Status: " + response.statusCode() + ", Response: " + response.body());
            }
        } catch (Exception e) {
            LogsUtil.error("Failed to attach file " + filePath + ": " + e.getMessage());
        }
    }

    private byte[] buildMultipartBody(String fileName, byte[] fileContent, String boundary) throws IOException {
        String header = "--" + boundary + "\r\n" +
                       "Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"\r\n" +
                       "Content-Type: application/octet-stream\r\n\r\n";
        
        String footer = "\r\n--" + boundary + "--\r\n";
        
        byte[] headerBytes = header.getBytes("UTF-8");
        byte[] footerBytes = footer.getBytes("UTF-8");
        
        byte[] result = new byte[headerBytes.length + fileContent.length + footerBytes.length];
        System.arraycopy(headerBytes, 0, result, 0, headerBytes.length);
        System.arraycopy(fileContent, 0, result, headerBytes.length, fileContent.length);
        System.arraycopy(footerBytes, 0, result, headerBytes.length + fileContent.length, footerBytes.length);
        
        return result;
    }

    public void addComment(String issueKey, String comment) {
        try {
            ObjectNode commentJson = objectMapper.createObjectNode();
            commentJson.put("body", comment);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(jiraUrl + "/rest/api/2/issue/" + issueKey + "/comment"))
                    .header("Authorization", authHeader)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(commentJson.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                LogsUtil.info("Added comment to issue: " + issueKey);
            } else {
                LogsUtil.error("Failed to add comment. Status: " + response.statusCode() + ", Response: " + response.body());
            }
        } catch (Exception e) {
            LogsUtil.error("Failed to add comment: " + e.getMessage());
        }
    }

    public void transitionIssue(String issueKey, String transitionName) {
        try {
            // First, get available transitions
            HttpRequest getTransitionsRequest = HttpRequest.newBuilder()
                    .uri(URI.create(jiraUrl + "/rest/api/2/issue/" + issueKey + "/transitions"))
                    .header("Authorization", authHeader)
                    .GET()
                    .build();

            HttpResponse<String> transitionsResponse = httpClient.send(getTransitionsRequest, HttpResponse.BodyHandlers.ofString());

            if (transitionsResponse.statusCode() == 200) {
                JsonNode transitionsJson = objectMapper.readTree(transitionsResponse.body());
                JsonNode transitions = transitionsJson.get("transitions");
                
                String transitionId = null;
                for (JsonNode transition : transitions) {
                    if (transition.get("name").asText().equalsIgnoreCase(transitionName)) {
                        transitionId = transition.get("id").asText();
                        break;
                    }
                }

                if (transitionId != null) {
                    // Perform transition
                    ObjectNode transitionJson = objectMapper.createObjectNode();
                    transitionJson.put("id", transitionId);

                    HttpRequest transitionRequest = HttpRequest.newBuilder()
                            .uri(URI.create(jiraUrl + "/rest/api/2/issue/" + issueKey + "/transitions"))
                            .header("Authorization", authHeader)
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(transitionJson.toString()))
                            .build();

                    HttpResponse<String> response = httpClient.send(transitionRequest, HttpResponse.BodyHandlers.ofString());

                    if (response.statusCode() == 204) {
                        LogsUtil.info("Transitioned issue " + issueKey + " to: " + transitionName);
                    } else {
                        LogsUtil.error("Failed to transition issue. Status: " + response.statusCode());
                    }
                } else {
                    LogsUtil.error("Transition not found: " + transitionName);
                }
            }
        } catch (Exception e) {
            LogsUtil.error("Failed to transition issue: " + e.getMessage());
        }
    }

    public void assignIssue(String issueKey, String assignee) {
        try {
            ObjectNode assignJson = objectMapper.createObjectNode();
            assignJson.put("name", assignee);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(jiraUrl + "/rest/api/2/issue/" + issueKey + "/assignee"))
                    .header("Authorization", authHeader)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(assignJson.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 204) {
                LogsUtil.info("Assigned issue " + issueKey + " to: " + assignee);
            } else {
                LogsUtil.error("Failed to assign issue. Status: " + response.statusCode() + ", Response: " + response.body());
            }
        } catch (Exception e) {
            LogsUtil.error("Failed to assign issue: " + e.getMessage());
        }
    }

    public void updateIssueSummary(String issueKey, String newSummary) {
        try {
            ObjectNode fieldsJson = objectMapper.createObjectNode();
            fieldsJson.put("summary", newSummary);

            ObjectNode updateJson = objectMapper.createObjectNode();
            updateJson.set("fields", fieldsJson);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(jiraUrl + "/rest/api/2/issue/" + issueKey))
                    .header("Authorization", authHeader)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(updateJson.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 204) {
                LogsUtil.info("Updated summary for issue: " + issueKey);
            } else {
                LogsUtil.error("Failed to update issue summary. Status: " + response.statusCode() + ", Response: " + response.body());
            }
        } catch (Exception e) {
            LogsUtil.error("Failed to update issue summary: " + e.getMessage());
        }
    }

    public void updateIssueDescription(String issueKey, String newDescription) {
        try {
            ObjectNode fieldsJson = objectMapper.createObjectNode();
            fieldsJson.put("description", newDescription);

            ObjectNode updateJson = objectMapper.createObjectNode();
            updateJson.set("fields", fieldsJson);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(jiraUrl + "/rest/api/2/issue/" + issueKey))
                    .header("Authorization", authHeader)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(updateJson.toString()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 204) {
                LogsUtil.info("Updated description for issue: " + issueKey);
            } else {
                LogsUtil.error("Failed to update issue description. Status: " + response.statusCode() + ", Response: " + response.body());
            }
        } catch (Exception e) {
            LogsUtil.error("Failed to update issue description: " + e.getMessage());
        }
    }

    public JsonNode getIssue(String issueKey) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(jiraUrl + "/rest/api/2/issue/" + issueKey))
                    .header("Authorization", authHeader)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode issueJson = objectMapper.readTree(response.body());
                LogsUtil.info("Retrieved issue: " + issueKey);
                return issueJson;
            } else {
                LogsUtil.error("Failed to get issue. Status: " + response.statusCode() + ", Response: " + response.body());
                return null;
            }
        } catch (Exception e) {
            LogsUtil.error("Failed to get issue: " + e.getMessage());
            return null;
        }
    }

    public void deleteIssue(String issueKey) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(jiraUrl + "/rest/api/2/issue/" + issueKey + "?deleteSubtasks=true"))
                    .header("Authorization", authHeader)
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 204) {
                LogsUtil.info("Deleted issue: " + issueKey);
            } else {
                LogsUtil.error("Failed to delete issue. Status: " + response.statusCode() + ", Response: " + response.body());
            }
        } catch (Exception e) {
            LogsUtil.error("Failed to delete issue: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        try {
            if (httpClient != null) {
                LogsUtil.info("JiraReporter closed successfully");
            }
        } catch (Exception e) {
            LogsUtil.error("Error closing JiraReporter: " + e.getMessage());
        }
    }
}
