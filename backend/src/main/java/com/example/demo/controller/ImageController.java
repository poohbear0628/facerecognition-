package com.example.demo.controller;

import com.example.demo.azure.CustomVision;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.azure.storage.blob.*;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "${FRONTEND_HOST:http://localhost:8000}")
public class ImageController {
    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return name;
    }

    @PostMapping("/images")
    public ResponseEntity<Object> image(@RequestBody String base64image) throws IOException {
        String base64 = base64image.replace("data:image/png;base64,", "");
        byte[] imageByte = Base64.getDecoder().decode(base64);
        String imageName = UUID.randomUUID() + ".png";

        File imageFolder = new File("./images");
        if (!imageFolder.exists()) {
            imageFolder.mkdir();
        }

        Files.write(new File("./images/" + imageName).toPath(), imageByte);
        //saveToCloud(imageName);
        CustomVision.uploadImage(CustomVision.tagIdSerena, imageByte);
        return new ResponseEntity<>("Success!", HttpStatus.OK);
    }

    private void saveToCloud(String imageName) {
        String connectStr = "DefaultEndpointsProtocol=https;AccountName=yichenproject1;AccountKey=SPdCeek1msgZlpxGVOBUiYkS4VW4CRmm95JJi0OT8U6ylg8rqYUTNTAOfrPFNmTGcIQAP0n3toMC+ASt5xAc5w==;EndpointSuffix=core.windows.net";
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectStr).buildClient();
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient("images");
        if (!containerClient.exists()) {
            containerClient.create();
        }
        BlobClient blobClient = containerClient.getBlobClient(imageName);
        blobClient.uploadFromFile("./images/" + imageName);
    }

    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    public ResponseEntity<String> validate(@RequestBody String data) throws IOException, JSONException {
        String base64 = data.replace("data:image/png;base64,", "");
        byte[] decode = Base64.getDecoder().decode(base64);
        return CustomVision.validate(decode);
    }

}

