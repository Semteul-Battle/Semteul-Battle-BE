package Winter_Project.Semteul_Battle.domain.problem.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class FileUploadController {

    private final AmazonS3 amazonS3Client;

    @Value("#{environment['cloud.aws.s3.bucketName']}")
    private String bucket;

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String fileUrl = "https://" + bucket + ".s3.amazonaws.com/test/" + fileName;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            // ?????bucket ?????곕츥????? ??饔낅떽?????????쇰뮝????耀붾굝???????饔낅떽????????勇??????밸쫫??????萸??
            amazonS3Client.putObject(bucket, "test/" + fileName, file.getInputStream(), metadata);
return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}