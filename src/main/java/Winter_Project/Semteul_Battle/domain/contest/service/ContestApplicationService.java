package Winter_Project.Semteul_Battle.domain.contest.service;

import Winter_Project.Semteul_Battle.domain.contest.dto.request.ContestApplicationDto;

public interface ContestApplicationService {

    boolean applyContest(ContestApplicationDto contestApplicationDto, String loginId);
}
