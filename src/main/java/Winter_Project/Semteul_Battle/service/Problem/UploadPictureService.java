package Winter_Project.Semteul_Battle.service.Problem;

import Winter_Project.Semteul_Battle.domain.ProblemImage;
import Winter_Project.Semteul_Battle.repository.ProblemImageRepository;
import Winter_Project.Semteul_Battle.repository.ProblemRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UploadPictureService {
    private final AmazonS3 amazonS3Client;
    private final ProblemRepository problemRepository;
    private final ProblemImageRepository problemImageRepository;

    @Value("#{environment['cloud.aws.s3.bucketName']}")
    private String bucket;

    // 대회 문제 이미지 추가
    public List<String> uploadPictures(List<MultipartFile> files, Long problemId) throws IOException {
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String fileUrl = "https://" + bucket + ".s3.amazonaws.com/problemPictures/" + fileName;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3Client.putObject(bucket, "problemPictures/" + fileName, file.getInputStream(), metadata);
            imageUrls.add(fileUrl);

            // 문제 이미지 저장
            ProblemImage problemImage = new ProblemImage();
            problemImage.setProblem(problemRepository.findById(problemId).orElse(null)); // Problem 엔티티 참조 설정
            problemImage.setImageUrl(fileUrl);
            problemImageRepository.save(problemImage);
        }
        return imageUrls;
    }

    // 문제 사진 수정
    public List<String> updatePictures(List<MultipartFile> files, Long problemId) throws IOException {
        // 기존 사진 삭제
        List<ProblemImage> existingImages = problemImageRepository.findByProblemId(problemId);
        for (ProblemImage existingImage : existingImages) {
            amazonS3Client.deleteObject(bucket, "problemPictures/" + existingImage.getImageUrl());
            problemImageRepository.delete(existingImage);
        }

        // 새로운 사진 추가
        List<String> newImageUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String fileUrl = "https://" + bucket + ".s3.amazonaws.com/problemPictures/" + fileName;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3Client.putObject(bucket, "problemPictures/" + fileName, file.getInputStream(), metadata);
            newImageUrls.add(fileUrl);

            // 문제 이미지 저장
            ProblemImage problemImage = new ProblemImage();
            problemImage.setProblem(problemRepository.findById(problemId).orElse(null)); // Problem 엔티티 참조 설정
            problemImage.setImageUrl(fileUrl);
            problemImageRepository.save(problemImage);
        }

        // 업데이트된 이미지 URL 목록 반환
        return newImageUrls;
    }
}
