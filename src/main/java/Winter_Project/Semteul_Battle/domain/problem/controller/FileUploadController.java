package Winter_Project.Semteul_Battle.domain.problem.controller;


import Winter_Project.Semteul_Battle.global.response.BaseResponse;
import Winter_Project.Semteul_Battle.global.status.SuccessStatus;
import Winter_Project.Semteul_Battle.domain.problem.exception.ProblemException;
import Winter_Project.Semteul_Battle.global.status.ErrorStatus;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    public BaseResponse<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String fileUrl = "https://" + bucket + ".s3.amazonaws.com/test/" + fileName;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3Client.putObject(bucket, "test/" + fileName, file.getInputStream(), metadata);
            return BaseResponse.onSuccess(SuccessStatus.OK, fileUrl);
        } catch (IOException e) {
            throw new ProblemException(ErrorStatus._INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
        }
    }
}
