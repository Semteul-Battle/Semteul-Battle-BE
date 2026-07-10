package Winter_Project.Semteul_Battle.domain.problem.service;

import Winter_Project.Semteul_Battle.domain.problem.entity.ProblemImage;
import Winter_Project.Semteul_Battle.domain.problem.repository.ProblemImageRepository;
import Winter_Project.Semteul_Battle.domain.problem.repository.ProblemRepository;
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

    // ???????嶺?????????饔낅떽???? ?????댄뱼??
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

            // ???嶺?????????饔낅떽???? ????
ProblemImage problemImage = new ProblemImage();
            problemImage.setProblem(problemRepository.findById(problemId).orElse(null)); // Problem ?????????饔낅떽???????????嚥싲갭큔???
problemImage.setImageUrl(fileUrl);
            problemImageRepository.save(problemImage);
        }
        return imageUrls;
    }

    // ???嶺????????????癰궽블뀯??
public List<String> updatePictures(List<MultipartFile> files, Long problemId) throws IOException {
        // ????????????????
List<ProblemImage> existingImages = problemImageRepository.findByProblemId(problemId);
        for (ProblemImage existingImage : existingImages) {
            amazonS3Client.deleteObject(bucket, "problemPictures/" + existingImage.getImageUrl());
            problemImageRepository.delete(existingImage);
        }

        // ????雅???????????댄뱼??
List<String> newImageUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String fileUrl = "https://" + bucket + ".s3.amazonaws.com/problemPictures/" + fileName;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3Client.putObject(bucket, "problemPictures/" + fileName, file.getInputStream(), metadata);
            newImageUrls.add(fileUrl);

            // ???嶺?????????饔낅떽???? ????
ProblemImage problemImage = new ProblemImage();
            problemImage.setProblem(problemRepository.findById(problemId).orElse(null)); // Problem ?????????饔낅떽???????????嚥싲갭큔???
problemImage.setImageUrl(fileUrl);
            problemImageRepository.save(problemImage);
        }

        // ???????살숲??????밸븶?????????饔낅떽???? URL ?饔낅떽????ш낄?뉔뇡?꾩땡沃섏쥓???????썹땟戮녹????
return newImageUrls;
    }
}
