package sg.edu.nus.iss.server.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.server.service.FileService;

@Controller
@CrossOrigin(origins = "*")
public class FileController {
    @Autowired
    private FileService flSvc;


    @PostMapping(path="/upload", consumes=MediaType.MULTIPART_FORM_DATA_VALUE,
            produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> upload(
        @RequestPart MultipartFile imageFile,
        @RequestPart String title,
        @RequestPart String complain
    ){
        String key;
        JsonObject jsonPayload = null;

        try {
            key = this.flSvc.upload(imageFile);
            jsonPayload = Json.createObjectBuilder()
            .add("image-key", key)
            .build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(jsonPayload.toString());
    }

}
