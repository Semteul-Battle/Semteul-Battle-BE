# Semteul Battle Backend

Semteul Battle은 알고리즘 문제 풀이 대회를 운영하고 참가할 수 있는 웹 플랫폼입니다.
사용자는 대회에 참가해 문제를 풀고 코드를 제출할 수 있으며, 출제자와 관리자는 대회, 문제, 공지사항, 질문 게시판 등을
관리할 수 있습니다.

## Convention

  ### Package Structure

  본 프로젝트는 도메인 중심 패키지 구조를 사용합니다.
  기능별 응집도를 높이기 위해 `user`, `contest`, `problem`, `menu`, `mail` 등 도메인 단위로 패키지를 분리합니다.

  ```text
  src/main/java/Winter_Project/Semteul_Battle
  ├── domain
  │   ├── user
  │   ├── contest
  │   ├── problem
  │   ├── menu
  │   └── mail
  └── global

  각 도메인은 아래 구조를 기준으로 구성합니다.

  domain/{domain}
  ├── controller
  ├── service
  ├── repository
  ├── entity
  ├── dto
  │   ├── request
  │   └── response
  └── exception

  global 패키지는 특정 도메인에 종속되지 않는 공통 기능을 관리합니다.

  global
  ├── config
  ├── security
  ├── exception
  ├── response
  ├── status
  └── util
```
  #### Package Rule

  - 도메인과 직접 관련된 코드는 domain/{domain} 내부에 작성합니다.
  - 여러 도메인에서 공통으로 사용하는 코드는 global에 작성합니다.
  - 요청 DTO와 응답 DTO는 dto.request, dto.response로 분리합니다.
  - Entity는 API 응답으로 직접 반환하지 않습니다.
  - 도메인별 예외는 각 도메인의 exception 패키지에서 관리합니다.

  ———

  ### Naming Convention

  #### Common

  - 클래스명은 PascalCase를 사용합니다.
  - 메서드명과 변수명은 camelCase를 사용합니다.
  - 상수는 UPPER_SNAKE_CASE를 사용합니다.
  - 패키지명은 소문자로 작성합니다.
  - 이름은 역할과 의도가 드러나도록 작성합니다.
  - 불필요한 축약어는 사용하지 않습니다.

  private static final long REFRESH_TOKEN_TTL_SECONDS = 604800L;

  public void updateProfile(String profile) {
      this.profile = profile;
  }

  #### Class

   Type            Rule                                   Example
  ━━━━━━━━━━━━━━  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ━━━━━━━━━━━━━━━━━
   Controller      {Domain}Controller                     UserController
  ──────────────  ─────────────────────────────────────  ─────────────────
   Service         {Domain}Service                        UserService
  ──────────────  ─────────────────────────────────────  ─────────────────
   Service Impl    {Domain}ServiceImpl                    UserServiceImpl
  ──────────────  ─────────────────────────────────────  ─────────────────
   Repository      {Entity}Repository                     UserRepository
  ──────────────  ─────────────────────────────────────  ─────────────────
   Exception       {Domain}Exception                      UserException
  ──────────────  ─────────────────────────────────────  ─────────────────
   Request DTO     {Action}RequestDto 또는 {Action}Dto    SignUpDto
  ──────────────  ─────────────────────────────────────  ─────────────────
   Response DTO    {Data}ResponseDto                      UserResponseDto

  #### Variable

  - 단일 값은 의미가 드러나는 이름을 사용합니다.
  - 컬렉션은 복수형을 사용합니다.
  - ID 값은 {domain}Id 형식을 사용합니다.
  - boolean 값은 is, has, can 등을 사용해 의미를 명확히 합니다.

  Long userId;
  List<Contest> contests;
  boolean isChecked;

  ———

  ### Entity Convention

  - Entity에는 @Setter와 @Data를 사용하지 않습니다.
  - 값 변경이 필요한 경우 의미 있는 메서드를 작성합니다.
  - 연관관계는 기본적으로 LAZY 로딩을 사용합니다.
  - Entity를 API 응답으로 직접 반환하지 않습니다.

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  public void updateName(String name) {
      this.name = name;
  }

  ———

  ### DTO Convention

  - 요청 DTO는 dto.request 패키지에 작성합니다.
  - 응답 DTO는 dto.response 패키지에 작성합니다.
  - 요청 DTO에는 필요한 validation annotation을 작성합니다.
  - DTO에는 불필요한 @Setter 사용을 지양합니다.
  - 응답 DTO는 Entity를 받아 변환하는 정적 팩토리 메서드를 사용할 수 있습니다.

  @Getter
  @NoArgsConstructor
  public class SignUpDto {

      @NotBlank
      private String loginId;

      @NotBlank
      private String password;
  }

  ———

  ### Controller Convention

  - Controller는 요청 검증과 응답 반환에 집중합니다.
  - 비즈니스 로직은 Service에서 처리합니다.
  - 요청 DTO에는 @Valid를 사용합니다.
  - 응답은 BaseResponse 형식으로 통일합니다.

  @PostMapping
  public BaseResponse<Void> create(@RequestBody @Valid CreateRequestDto request) {
      service.create(request);
      return BaseResponse.onSuccess(null);
  }

  ———

  ### Service Convention

  - Service는 비즈니스 로직을 담당합니다.
  - 조회 실패 시 Optional.get() 대신 orElseThrow()를 사용합니다.
  - 쓰기 작업에는 @Transactional을 사용합니다.
  - 읽기 작업에는 @Transactional(readOnly = true)를 사용합니다.
  - 도메인별 예외를 사용합니다.

  User user = userRepository.findById(userId)
      .orElseThrow(() -> new UserException(ErrorStatus._NOT_FOUND));

  ———

  ### Database Convention

  - 테이블명과 컬럼명은 snake_case를 사용합니다.
  - PK 컬럼명은 id를 사용합니다.
  - FK 컬럼명은 {table_name}_id 형식을 사용합니다.
  - DDL 변경은 Flyway migration으로 관리합니다.

  V1__init_schema.sql
  V2__add_user_unique_constraints.sql

  ———

  ### Git Convention

  #### Branch

  type/short-description

   Type        Description
  ━━━━━━━━━━  ━━━━━━━━━━━━━━━━━━━━━━━
   feat        기능 추가
  ──────────  ───────────────────────
   fix         버그 수정
  ──────────  ───────────────────────
   refactor    리팩터링
  ──────────  ───────────────────────
   docs        문서 수정
  ──────────  ───────────────────────
   chore       설정, 빌드, 기타 작업
  ──────────  ───────────────────────
   test        테스트 코드
  ──────────  ───────────────────────
   ci          CI/CD 설정

  feat/auth-sign-up
  fix/token-refresh
  refactor/entity-dto-cleanup
  docs/readme-convention

  #### Commit

  type: 작업 내용 요약

  feat: 회원가입 이메일 인증 추가
  fix: refresh token 검증 로직 수정
  refactor: DTO setter 제거
  docs: README 컨벤션 추가

  #### Pull Request

  PR 제목은 커밋 메시지와 동일한 형식을 사용합니다.

  type: 작업 내용 요약

  PR 본문은 프로젝트 PR 템플릿을 따릅니다.

  ———

  ### API Response Convention

  모든 API 응답은 BaseResponse 형식을 사용합니다.

  {
    "isSuccess": true,
    "code": "COMMON200",
    "message": "성공입니다.",
    "result": {}
  }
