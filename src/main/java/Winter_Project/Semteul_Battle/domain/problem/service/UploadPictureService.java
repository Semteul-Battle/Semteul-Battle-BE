package Winter_Project.Semteul_Battle.domain.problem.service;

import Winter_Project.Semteul_Battle.domain.problem.entity.ProblemImage;
import Winter_Project.Semteul_Battle.domain.problem.repository.ProblemImageRepository;
import Winter_Project.Semteul_Battle.domain.problem.repository.ProblemRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface UploadPictureService {
    public List<String> uploadPictures(List<MultipartFile> files, Long problemId) throws IOException;
    public List<String> updatePictures(List<MultipartFile> files, Long problemId) throws IOException;
}
