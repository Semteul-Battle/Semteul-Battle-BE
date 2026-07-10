package Winter_Project.Semteul_Battle.domain.contest.service;

import Winter_Project.Semteul_Battle.domain.contest.entity.*;
import Winter_Project.Semteul_Battle.domain.problem.entity.*;
import Winter_Project.Semteul_Battle.domain.user.entity.*;
import Winter_Project.Semteul_Battle.domain.menu.entity.*;
import Winter_Project.Semteul_Battle.domain.contest.dto.response.ContestInfoDTO;
import Winter_Project.Semteul_Battle.domain.contest.dto.request.CreateContestDto;
import Winter_Project.Semteul_Battle.domain.contest.repository.*;
import Winter_Project.Semteul_Battle.domain.problem.repository.*;
import Winter_Project.Semteul_Battle.domain.user.repository.*;
import Winter_Project.Semteul_Battle.domain.menu.repository.*;
import org.apache.catalina.User;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface ContestService {
    public Contest createContest(CreateContestDto createContestDto);
    public void deleteContest(Long contestId);
    public List<Problem> getProblemsByContestId(Long contestId);
    public ContestInfoDTO getContestInfo(Long contestId, String loginId);
    public Contest getContestById(Long contestId);
}
