package Winter_Project.Semteul_Battle.domain.problem.service;

import Winter_Project.Semteul_Battle.domain.contest.entity.Contest;
import Winter_Project.Semteul_Battle.domain.problem.entity.Problem;
import Winter_Project.Semteul_Battle.domain.problem.dto.request.AddProblemDto;
import Winter_Project.Semteul_Battle.domain.contest.repository.ContestRepository;
import Winter_Project.Semteul_Battle.domain.problem.repository.ProblemRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public interface AddProblemService {
    public void problemFrame(AddProblemDto addProblemDto);
    public void createInputOutputFiles(String inputContent, String outputContent, String problemNumber);
}
