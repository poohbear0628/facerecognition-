package com.example.demo.azure;

import org.json.JSONException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.json.*;
import org.json.Cookie;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CustomVision {
    final static String trainingEndpoint = "https://southcentralus.api.cognitive.microsoft.com";
    final static String trainingApiKey = "9ffeafbee8b54431bce29aab84638628";
    final static String predictionKey = "9ffeafbee8b54431bce29aab84638628";
    static RestTemplate restTemplate = new RestTemplate();
    final static String projectID = "2a3c7d14-0a7c-45d4-918a-5998765257b6";
    public final static String tagIdSerena = "5f7553f1-aa71-4981-830f-cb32dba4e5ff";
    public final static String tagIdOthers = "cfd391bc-de91-4998-8935-f8287d4e3854";

    public static void createProject(String projectName) throws JSONException {
        String url = "{endpoint}/customvision/v3.3/Training/projects?name={name}";
        Map<String, String> params = new HashMap<>();
        params.put("endpoint", trainingEndpoint);
        params.put("name", projectName);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        URI uri = builder.buildAndExpand(params).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Training-key", trainingApiKey);
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
        // System.out.println(response.getBody())
        JSONObject jsonObject = new JSONObject(response.getBody());
        System.out.println(jsonObject.getString("id"));
    }

    // create tag
    public static void createTag(String tagName) throws JSONException {
        String url = "{endpoint}/customvision/v3.3/Training/projects/{projectId}/tags?name={name}";
        Map<String, String> params = new HashMap<>();
        params.put("endpoint", trainingEndpoint);
        params.put("projectId", projectID);
        params.put("name", tagName);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        URI uri = builder.buildAndExpand(params).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Training-key", trainingApiKey);
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
        JSONObject jsonObject = new JSONObject(response.getBody());
        System.out.println(jsonObject.getString("id") + ": " + tagName);
    }

    //upload image
    public static void uploadImage(String tagId, String fileName) throws JSONException, IOException {
        String url = "{endpoint}/customvision/v3.3/training/projects/{projectId}/images?tagIds={tagIds}";
        Map<String, String> params = new HashMap<>();
        params.put("endpoint", trainingEndpoint);
        params.put("projectId", projectID);
        params.put("tagIds", tagId);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        URI uri = builder.buildAndExpand(params).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set("Training-key", trainingApiKey);
        Path path = Paths.get(fileName);
        byte[] imageFile = Files.readAllBytes(path);
        HttpEntity<byte[]> request = new HttpEntity<>(imageFile, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
        System.out.println(response.getBody());
    }
    public static void uploadImage(String tagId, byte[] fileData) throws JSONException, IOException {
        String url = "{endpoint}/customvision/v3.3/training/projects/{projectId}/images?tagIds={tagIds}";
        Map<String, String> params = new HashMap<>();
        params.put("endpoint", trainingEndpoint);
        params.put("projectId", projectID);
        params.put("tagIds", tagId);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        URI uri = builder.buildAndExpand(params).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set("Training-key", trainingApiKey);
        HttpEntity<byte[]> request = new HttpEntity<>(fileData, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
        System.out.println(response.getBody());
    }

    public static ResponseEntity<String> validate(byte[] data) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/octet-stream");
        headers.add("Prediction-Key", predictionKey);
        HttpEntity<byte[]> entity = new HttpEntity<>(data, headers);
        String URL = "https://southcentralus.api.cognitive.microsoft.com/customvision/v3.0/Prediction/2a3c7d14-0a7c-45d4-918a-5998765257b6/classify/iterations/Iteration1/image";
        ResponseEntity<String> result = restTemplate.postForEntity(URL, entity, String.class);
        return result;
    }

    public static void main(String args[]) throws JSONException, IOException {
        //createProject("facerecognition");
        //createTag("Serena");
        //createTag("Others");
        String fileName = "/Users/serenaliu628/Desktop/project/backend/images/ffd67770-ffd4-4511-b055-78609344f985.png";
        Path path = Paths.get(fileName);
        byte[] imageFile = Files.readAllBytes(path);
        //uploadImage(tagIdSerena, fileName);
        //alidate(imageFile);
        System.out.println(validate(imageFile));

    }
}


