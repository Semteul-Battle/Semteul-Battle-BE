package Winter_Project.Semteul_Battle.domain.contest.service;

import Winter_Project.Semteul_Battle.domain.contest.entity.Contest;
import Winter_Project.Semteul_Battle.domain.problem.entity.Problem;
import Winter_Project.Semteul_Battle.domain.contest.entity.Submit;
import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import Winter_Project.Semteul_Battle.domain.contest.dto.request.SubmitDTO;
import Winter_Project.Semteul_Battle.domain.contest.repository.ContestRepository;
import Winter_Project.Semteul_Battle.domain.problem.repository.ProblemRepository;
import Winter_Project.Semteul_Battle.domain.contest.repository.SubmitRepository;
import Winter_Project.Semteul_Battle.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.Optional;

public interface SubmitService {
    Long saveSubmit(SubmitDTO submitDTO);
    void saveCodeToFile(SubmitDTO submitDTO, Long submitId);
    void runGradingProgram(String programPath, Long submitId);
}
