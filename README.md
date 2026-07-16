# 📖 Semteul Battle Backend Convention

Semteul Battle은 알고리즘 문제 풀이 대회를 운영하고 참가할 수 있는 웹 플랫폼입니다.

사용자는 대회에 참가하여 문제를 풀고 코드를 제출할 수 있으며, 출제자와 관리자는 대회, 문제, 공지사항, 질문 게시판 등을 관리할 수 있습니다.

---

# 📦 Package Structure

본 프로젝트는 **도메인 중심 패키지 구조(Domain-Oriented Package Structure)** 를 사용합니다.

```text
src/main/java/Winter_Project/Semteul_Battle
├── domain
│   ├── user
│   ├── contest
│   ├── problem
│   ├── menu
│   └── mail
└── global
```

각 도메인은 다음 구조를 따릅니다.

```text
domain/{domain}
├── controller
├── service
├── repository
├── entity
├── dto
│   ├── request
│   └── response
└── exception
```

공통 기능은 `global` 패키지에서 관리합니다.

```text
global
├── config
├── security
├── exception
├── response
├── status
└── util
```

## Package Rules

* 도메인 관련 코드는 `domain/{domain}` 내부에 작성합니다.
* 여러 도메인에서 사용하는 공통 코드는 `global`에 작성합니다.
* DTO는 `request`, `response`로 분리합니다.
* Entity를 API 응답으로 직접 반환하지 않습니다.
* 도메인 예외는 각 도메인의 `exception` 패키지에서 관리합니다.

---

# 📝 Naming Convention

## Common Rules

| 대상       | 규칙               |
| -------- | ---------------- |
| Class    | PascalCase       |
| Method   | camelCase        |
| Variable | camelCase        |
| Constant | UPPER_SNAKE_CASE |
| Package  | lowercase        |

### Example

```java
private static final long REFRESH_TOKEN_TTL_SECONDS = 604800L;

public void updateProfile(String profile) {
    this.profile = profile;
}
```

---

## Class Naming

| Type         | Convention            | Example            |
| ------------ | --------------------- | ------------------ |
| Controller   | `{Domain}Controller`  | `UserController`   |
| Service      | `{Domain}Service`     | `UserService`      |
| Service Impl | `{Domain}ServiceImpl` | `UserServiceImpl`  |
| Repository   | `{Entity}Repository`  | `UserRepository`   |
| Exception    | `{Domain}Exception`   | `UserException`    |
| Request DTO  | `{Action}RequestDto`  | `SignUpRequestDto` |
| Response DTO | `{Data}ResponseDto`   | `UserResponseDto`  |

---

## Variable Naming

### Rules

* 단일 값은 의미가 드러나는 이름을 사용합니다.
* 컬렉션은 복수형을 사용합니다.
* ID는 `{domain}Id` 형식을 사용합니다.
* Boolean 값은 `is`, `has`, `can` 접두어를 사용합니다.

### Example

```java
Long userId;

List<Contest> contests;

boolean isChecked;
boolean hasPermission;
```

---

# 🏛 Entity Convention

### Rules

* `@Setter`, `@Data` 사용 금지
* 상태 변경은 명시적인 메서드로 수행
* 연관관계는 기본적으로 `LAZY`
* Entity를 API 응답으로 직접 반환하지 않음

### Example

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id")
private User user;

public void updateName(String name) {
    this.name = name;
}
```

---

# 📄 DTO Convention

### Rules

* 요청 DTO → `dto.request`
* 응답 DTO → `dto.response`
* 요청 DTO에는 Validation 적용
* 불필요한 Setter 사용 지양
* 응답 DTO는 정적 팩토리 메서드 사용 가능

### Example

```java
@Getter
@NoArgsConstructor
public class SignUpRequestDto {

    @NotBlank
    private String loginId;

    @NotBlank
    private String password;
}
```

---

# 🎮 Controller Convention

### Rules

* 요청 검증 담당
* 응답 반환 담당
* 비즈니스 로직 작성 금지
* `@Valid` 사용
* `BaseResponse` 형식으로 응답 통일

### Example

```java
@PostMapping
public BaseResponse<Void> create(
        @RequestBody @Valid CreateRequestDto request
) {
    service.create(request);
    return BaseResponse.onSuccess(null);
}
```

---

# ⚙️ Service Convention

### Rules

* 비즈니스 로직 담당
* `Optional.get()` 사용 금지
* `orElseThrow()` 사용
* 쓰기 작업 → `@Transactional`
* 조회 작업 → `@Transactional(readOnly = true)`
* 도메인 예외 사용

### Example

```java
User user = userRepository.findById(userId)
    .orElseThrow(() -> new UserException(ErrorStatus.NOT_FOUND));
```

---

# 🗄 Database Convention

### Rules

* 테이블명: `snake_case`
* 컬럼명: `snake_case`
* PK: `id`
* FK: `{table_name}_id`
* 스키마 변경은 Flyway로 관리

### Example

```text
V1__init_schema.sql
V2__add_user_unique_constraints.sql
```

---

# 🌱 Git Convention

## Branch Naming

```text
type/short-description
```

### Branch Types

| Type     | Description |
| -------- | ----------- |
| feat     | 기능 추가       |
| fix      | 버그 수정       |
| refactor | 리팩토링        |
| docs     | 문서 수정       |
| chore    | 설정 및 기타 작업  |
| test     | 테스트 코드      |
| ci       | CI/CD 설정    |

### Example

```text
feat/auth-sign-up
fix/token-refresh
refactor/entity-dto-cleanup
docs/readme-convention
```

---

## Commit Message

```text
type: 작업 내용 요약
```

### Example

```text
feat: 회원가입 이메일 인증 추가
fix: refresh token 검증 로직 수정
refactor: DTO setter 제거
docs: README 컨벤션 추가
```

---

## Pull Request

PR 제목은 Commit Message 형식을 따릅니다.

```text
type: 작업 내용 요약
```

PR 본문은 프로젝트 PR 템플릿을 사용합니다.

---

# 📡 API Response Convention

모든 API 응답은 `BaseResponse` 형식을 사용합니다.

```json
{
  "isSuccess": true,
  "code": "COMMON200",
  "message": "성공입니다.",
  "result": {}
}
```
