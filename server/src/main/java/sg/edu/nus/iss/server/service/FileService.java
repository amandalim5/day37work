package sg.edu.nus.iss.server.service;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class FileService {
    @Value("${do.storage.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    public String upload(MultipartFile file) throws IOException {
        // create the user data
        Map<String, String> userData = new HashMap<>();
        userData.put("name", "Amanda");
        userData.put("uploadTime", Instant.now().toString());
        userData.put("originalFilename", file.getOriginalFilename());

        // to set into S3
        ObjectMetadata metadata = new ObjectMetadata();
        // if it were png, it would be image/png
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        metadata.setUserMetadata(userData);

        // every file should be represented by an id
        String key = UUID.randomUUID().toString()
                    .substring(0, 8);

        // extracting the content type/ the file extension (use String tokenizer, or split)
        // the String tokeniszer will take in the original file name, delimit by .
        // it will return a count

        StringTokenizer tk = new StringTokenizer(file.getOriginalFilename(),".");
        int count = 0;
        String fileNameExt = "";
        while(tk.hasMoreTokens()){
            if(count == 1){
                fileNameExt = tk.nextToken();
                break;
            }
            count++;
        }
        if(fileNameExt.equals("blob")){
              
        }

        String finalfileUpload = "";
        finalfileUpload = key  +".png";

        PutObjectRequest putRequest = 
            new PutObjectRequest(
                bucketName,
                // the key name is below
                "myobject/%s.%s".formatted(key, finalfileUpload),
                file.getInputStream(),
                metadata
            );
        
        putRequest = putRequest.withCannedAcl(CannedAccessControlList.PublicRead);
        s3Client.putObject(putRequest);
        return "myobject/%s.%s".formatted(key, finalfileUpload);
    }
}
