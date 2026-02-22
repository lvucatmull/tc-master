# 1. 프로젝트 개요

## 1.1 프로젝트 목적

TC Master는 소프트웨어 프로젝트별 **테스트 케이스(TC)를 등록하고, 코드를 작성하여 실행**할 수 있는 웹 기반 테스트 관리 플랫폼이다.

주요 목표:

- 프로젝트별 TC 등록 및 체계적 관리
- 웹 에디터를 통한 테스트 코드 작성 및 실행
- 전체/카테고리별/개별 테스트 실행 지원
- 실행 결과 대시보드 및 리포트 제공

## 1.2 UI/UX 참조

**Qase** (https://qase.io) 와 **TestRail** (https://www.testrail.com) 을 주요 UI 참조 대상으로 한다.

핵심 UI 특징:

- 좌측 사이드바 기반 네비게이션 (Nested Tree View)
- 테스트 스위트/케이스 트리 구조
- 대시보드 위젯 시스템 (도넛 차트, 시계열 그래프, 통계 카드)
- 테스트 실행 결과의 실시간 업데이트

# 2. 기술 스택

## 2.1 Backend

| 항목 | 기술 |
| --- | --- |
| Framework | Spring Boot 3.x |
| Language | Java 17+ |
| ORM | Spring Data JPA |
| Database | PostgreSQL 15+ |
| Build Tool | Gradle |
| API Docs | SpringDoc OpenAPI (Swagger) |
| Security | Spring Security + JWT |
| Test | JUnit 5, Mockito |

## 2.2 Frontend

| 항목 | 기술 |
| --- | --- |
| Framework | Next.js 14+ (App Router) |
| Language | TypeScript |
| Styling | Tailwind CSS |
| State Mgmt | Zustand |
| API Client | Axios + React Query (TanStack) |
| Code Editor | Monaco Editor |
| Charts | Recharts |
| UI Components | shadcn/ui |

## 2.3 인프라

| 항목 | 기술 |
| --- | --- |
| Containerization | Docker |
| CI/CD | GitHub Actions |
| Code Execution | Docker 기반 샌드박스 컨테이너 |

# 3. 시스템 아키텍처

```text
    [Browser]
    |
    v
[Next.js Frontend] --REST API--> [Spring Boot Backend]
    |                                    |
    |                                    v
    |                              [PostgreSQL]
    |                                    |
    v                                    v
[Monaco Editor]              [Docker Sandbox Engine]
    |                              (코드 실행 컨테이너)
    v                                    |
[WebSocket] <-- 실행 결과 스트리밍 --------+
```

**주요 흐름:**

1. 사용자가 Frontend에서 TC 등록/관리
2. 코드 에디터(Monaco)에서 테스트 코드 작성
3. 실행 요청 시 Backend가 Docker 샌드박스에서 코드 실행
4. 실행 결과를 WebSocket으로 실시간 스트리밍
5. 결과 저장 후 대시보드에 반영

# 4. 핵심 기능

## 4.1 프로젝트 관리

- 프로젝트 CRUD
- 프로젝트별 멤버 관리
- 프로젝트 설정 (실행 환경, 언어 선택 등)

## 4.2 테스트 스위트 / 케이스 관리

- 트리 구조 테스트 스위트 (폴더) 관리
- TC 등록: 제목, 설명, 우선순위, 카테고리, 태그
- TC 상세: Precondition, Steps, Expected Result
- TC에 실행 코드 연결 (Monaco Editor)
- Bulk 작업 (이동, 복사, 삭제)

## 4.3 테스트 실행

- **전체 실행**: 프로젝트 내 모든 TC 실행
- **카테고리별 실행**: 스위트/태그 기반 필터 실행
- **개별 실행**: 단일 TC 실행
- **테스트 플랜**: 여러 TC를 묶어 실행 계획 수립
- 실행 중 실시간 로그 스트리밍
- 실행 결과: Pass / Fail / Skip / Error

## 4.4 코드 에디터

- Monaco Editor 기반 웹 코드 에디터
- 지원 언어: JavaScript, TypeScript, Python, Java
- 자동완성 및 구문 강조
- 테스트 코드 템플릿 제공
- **TC-코드 1:N 관계**: 하나의 TC에 여러 언어의 코드를 연결 가능 (TestCode 테이블의 language 필드로 구분)
- **코드 버전 관리**: 저장 시 version 자동 증가, 이전 버전 조회 및 롤백 지원
- **템플릿 자동 생성**: TC 메타데이터(제목, Steps, Expected Result)를 코드 템플릿에 자동 주입
- **Split View**: 좌측 TC 상세 패널 + 우측 코드 에디터 레이아웃

## 4.5 대시보드 및 리포트

- 프로젝트별 대시보드
- 위젯: Pass Rate 도넛 차트, 실행 트렌드, 실패 분석
- 테스트 히스토리 타임라인
- CSV/PDF 리포트 내보내기

# 5. 데이터베이스 스키마

## 5.1 ERD 핵심 엔티티

```text
[User] 1---* [ProjectMember] *---1 [Project]
                                      |
                                 1----*
                                      |
                                [TestSuite]
                                      |
                                 1----*
                                      |
                                 [TestCase]
                                      |
                     +----------------+----------------+
                     |                                 |
                1----*                            1----*
                     |                                 |
                [TestCode]                       [TestRun]
                                                      |
                                                 1----*
                                                      |
                                                [TestResult]
```

## 5.2 테이블 정의

### User

| Column | Type | Description |
| --- | --- | --- |
| id | BIGINT (PK) | 사용자 ID |
| email | VARCHAR(255) | 이메일 |
| password | VARCHAR(255) | 암호화된 비밀번호 |
| name | VARCHAR(100) | 이름 |
| role | ENUM | ADMIN, USER |
| created_at | TIMESTAMP | 생성일 |

### Project

| Column | Type | Description |
| --- | --- | --- |
| id | BIGINT (PK) | 프로젝트 ID |
| name | VARCHAR(200) | 프로젝트명 |
| code | VARCHAR(10) | 프로젝트 코드 (e.g., TCM) |
| description | TEXT | 설명 |
| created_by | BIGINT (FK) | 생성자 |
| created_at | TIMESTAMP | 생성일 |

### TestSuite

| Column | Type | Description |
| --- | --- | --- |
| id | BIGINT (PK) | 스위트 ID |
| project_id | BIGINT (FK) | 프로젝트 |
| parent_id | BIGINT (FK, nullable) | 상위 스위트 (트리 구조) |
| name | VARCHAR(200) | 스위트명 |
| description | TEXT | 설명 |
| order_index | INT | 정렬 순서 |

### TestCase

| Column | Type | Description |
| --- | --- | --- |
| id | BIGINT (PK) | TC ID |
| suite_id | BIGINT (FK) | 소속 스위트 |
| title | VARCHAR(500) | TC 제목 |
| description | TEXT | 설명 |
| precondition | TEXT | 사전 조건 |
| steps | JSON | 테스트 단계 |
| expected_result | TEXT | 기대 결과 |
| priority | ENUM | CRITICAL, HIGH, MEDIUM, LOW |
| category | VARCHAR(100) | 카테고리 |
| tags | JSON | 태그 배열 |
| status | ENUM | ACTIVE, DEPRECATED, DRAFT |
| created_at | TIMESTAMP | 생성일 |

### TestCode

| Column | Type | Description |
| --- | --- | --- |
| id | BIGINT (PK) | 코드 ID |
| test_case_id | BIGINT (FK) | TC 연결 |
| language | ENUM | JS, TS, PYTHON, JAVA |
| code_content | TEXT | 실행 코드 |
| version | INT | 코드 버전 |
| updated_at | TIMESTAMP | 수정일 |

### TestRun

| Column | Type | Description |
| --- | --- | --- |
| id | BIGINT (PK) | 실행 ID |
| project_id | BIGINT (FK) | 프로젝트 |
| title | VARCHAR(200) | 실행 제목 |
| run_type | ENUM | FULL, SUITE, SELECTIVE |
| status | ENUM | PENDING, RUNNING, COMPLETED, ABORTED |
| started_at | TIMESTAMP | 시작 시간 |
| completed_at | TIMESTAMP | 완료 시간 |
| triggered_by | BIGINT (FK) | 실행자 |

### TestResult

| Column | Type | Description |
| --- | --- | --- |
| id | BIGINT (PK) | 결과 ID |
| test_run_id | BIGINT (FK) | 실행 |
| test_case_id | BIGINT (FK) | TC |
| result | ENUM | PASS, FAIL, SKIP, ERROR |
| duration_ms | INT | 실행 시간 (ms) |
| log_output | TEXT | 실행 로그 |
| error_message | TEXT | 에러 메시지 |
| executed_at | TIMESTAMP | 실행 시간 |

# 6. API 설계

## 6.1 인증 API

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | /api/auth/register | 회원가입 |
| POST | /api/auth/login | 로그인 (JWT 발급) |
| POST | /api/auth/refresh | 토큰 갱신 |

## 6.2 프로젝트 API

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | /api/projects | 프로젝트 목록 |
| POST | /api/projects | 프로젝트 생성 |
| GET | /api/projects/{id} | 프로젝트 상세 |
| PUT | /api/projects/{id} | 프로젝트 수정 |
| DELETE | /api/projects/{id} | 프로젝트 삭제 |

## 6.3 테스트 스위트 API

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | /api/projects/{id}/suites | 스위트 트리 조회 |
| POST | /api/projects/{id}/suites | 스위트 생성 |
| PUT | /api/suites/{id} | 스위트 수정 |
| DELETE | /api/suites/{id} | 스위트 삭제 |
| PATCH | /api/suites/{id}/move | 스위트 이동 |

## 6.4 테스트 케이스 API

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | /api/suites/{id}/cases | TC 목록 |
| POST | /api/suites/{id}/cases | TC 생성 |
| GET | /api/cases/{id} | TC 상세 |
| PUT | /api/cases/{id} | TC 수정 |
| DELETE | /api/cases/{id} | TC 삭제 |
| PUT | /api/cases/{id}/code | TC 코드 저장 |
| GET | /api/cases/{id}/code | TC 코드 조회 |

## 6.5 테스트 실행 API

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | /api/projects/{id}/runs | 테스트 실행 생성 |
| GET | /api/projects/{id}/runs | 실행 이력 조회 |
| GET | /api/runs/{id} | 실행 상세 |
| POST | /api/runs/{id}/execute | 실행 시작 |
| POST | /api/runs/{id}/abort | 실행 중단 |
| GET | /api/runs/{id}/results | 실행 결과 |
| WS | /ws/runs/{id} | 실행 상태 실시간 스트리밍 |

## 6.6 대시보드 API

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | /api/projects/{id}/dashboard | 프로젝트 대시보드 |
| GET | /api/projects/{id}/stats | 통계 데이터 |
| GET | /api/projects/{id}/reports | 리포트 |

# 7. 프론트엔드 페이지 구조

## 7.1 라우팅

```text
/                          → 랜딩 / 로그인
/dashboard                 → 전체 대시보드
/projects                  → 프로젝트 목록
/projects/[id]             → 프로젝트 대시보드
/projects/[id]/suites      → 테스트 스위트 (트리뷰)
/projects/[id]/cases/[caseId]  → TC 상세 + 코드 에디터
/projects/[id]/runs        → 테스트 실행 목록
/projects/[id]/runs/[runId]    → 실행 상세 + 결과
/projects/[id]/reports     → 리포트
/settings                  → 설정
```

## 7.2 주요 컴포넌트

- **Sidebar**: 프로젝트 네비게이션, 스위트 트리
- **TestCaseEditor**: TC 정보 + Monaco 코드 에디터 Split View
- **TestRunnerPanel**: 실행 제어 + 실시간 로그
- **DashboardWidgets**: 도넛차트, 트렌드, 통계카드
- **TreeView**: 스위트/케이스 드래그앤드롭 트리

# 8. 디렉토리 구조

## 8.1 Backend

```text
backend/
├── build.gradle
├── src/main/java/com/tcmaster/
│   ├── TcMasterApplication.java
│   ├── config/          # Security, CORS, WebSocket 설정
│   ├── controller/      # REST API 컨트롤러
│   ├── service/         # 비즈니스 로직
│   ├── repository/      # JPA Repository
│   ├── entity/          # JPA Entity
│   ├── dto/             # Request/Response DTO
│   ├── enums/           # Enum 정의
│   ├── exception/       # 예외 처리
│   ├── security/        # JWT, Auth
│   └── util/            # 유틸리티
├── src/main/resources/
│   ├── application.yml
│   └── db/migration/    # Flyway 마이그레이션
└── src/test/
```

## 8.2 Frontend

```text
frontend/
├── package.json
├── next.config.js
├── tailwind.config.ts
├── tsconfig.json
├── src/
│   ├── app/             # Next.js App Router 페이지
│   │   ├── layout.tsx
│   │   ├── page.tsx
│   │   ├── dashboard/
│   │   ├── projects/
│   │   └── settings/
│   ├── components/      # 재사용 UI 컴포넌트
│   │   ├── ui/          # shadcn/ui 기반
│   │   ├── layout/      # Sidebar, Header
│   │   ├── editor/      # Monaco Editor 래퍼
│   │   ├── dashboard/   # 위젯 컴포넌트
│   │   └── tree/        # TreeView 컴포넌트
│   ├── hooks/           # Custom Hooks
│   ├── lib/             # API Client, 유틸리티
│   ├── store/           # Zustand Store
│   ├── types/           # TypeScript 타입
│   └── styles/          # 글로벌 스타일
└── public/
```

# 9. 개발 마일스톤

## Phase 1 - 기반 구축

- 프로젝트 초기 설정 (Spring Boot + Next.js)
- 인증 시스템 (JWT)
- 프로젝트 CRUD API
- 기본 레이아웃 (Sidebar + Header)

## Phase 2 - TC 관리 핵심

- 테스트 스위트 트리 구조
- TC CRUD + 상세 페이지
- Monaco Editor 통합
- TC 코드 저장/조회

## Phase 3 - 실행 엔진

- Docker 샌드박스 코드 실행
- WebSocket 실시간 스트리밍
- 전체/카테고리별/개별 실행
- 실행 결과 저장

## Phase 4 - 대시보드 및 리포트

- 프로젝트 대시보드 위젯
- 실행 트렌드 차트
- 리포트 내보내기
- 테스트 히스토리

# 10. TC-코드 연동 전략

## 10.1 Qase 연동 방식 분석 (참조)

Qase는 **Reporter 패턴**을 사용하여 테스트 코드와 TMS를 연동한다.

```text
[테스트 코드 (Playwright/Jest)]   ← qase(ID) 어노테이션
        │
        ▼
[Qase Reporter Plugin]            ← 테스트 러너 이벤트 리스닝
        │
        ▼  REST API 호출
[Qase TMS (TestOps)]              ← 결과 저장 + TC 자동 생성
```

**Qase의 3가지 매핑 방식:**

- **Wrapper 함수**: `test(qase(1, 'Test name'), async () => { ... })`
- **복수 ID 매핑**: `test(qase([1,2,3], 'Multiple'), async () => { ... })`
- **메서드 기반**: 테스트 내부에서 `qase.id(1)`, `qase.title()`, `qase.fields()` 호출

**Reporter 설정 예시** (`playwright.config.ts`):

```ts
reporter: [['playwright-qase-reporter', {
  mode: 'testops',
  testops: { api: { token: QASE_API_TOKEN }, project: 'CODE' }
}]]
```

**핵심 특징:**

- 코드 실행 → Reporter가 결과를 Qase API로 전송
- Qase ID 없는 테스트도 첫 실행 시 TC 자동 생성 (Code → TMS 양방향)
- Manual + Automated TC를 하나의 Test Plan에서 통합 관리

## 10.2 TC Master 연동 전략 — 직접 매핑 방식

TC Master는 Qase와 달리 **Monaco Editor가 웹에 내장**되어 있어 별도 Reporter 없이 직접 매핑이 가능하다.

```text
[Web UI]
    ├─ TC 상세 패널 (좌측)
    │    TC ID: TCM-42
    │    제목, 설명, Steps...
    │
    └─ Monaco Editor (우측)        ← TC에 바인딩된 코드
         language: TypeScript
         version: 3
              │
              ▼  실행 버튼
    [Spring Boot Backend]
              │
              ▼  TestCode 조회 (test_case_id FK)
    [Docker Sandbox] → [TestResult 저장]
```

**Qase vs TC Master 비교:**

| 항목 | Qase | TC Master |
| --- | --- | --- |
| 코드 위치 | 외부 프로젝트 (로컬) | 웹 에디터 (Monaco) |
| 연동 방식 | Reporter 플러그인 | 직접 DB 매핑 (FK) |
| TC-코드 바인딩 | 어노테이션 qase(ID) | TestCode.test_case_id FK |
| 실행 환경 | 로컬 CI/CD | Docker 샌드박스 |
| 결과 전송 | Reporter → API | 서버 내부 직접 저장 |

## 10.3 TC ID 체계

**포맷**: `{프로젝트코드}-{순번}`

- 예시: `TCM-1`, `TCM-42`, `AUTH-15`
- `프로젝트코드`: Project.code 필드 (최대 10자)
- `순번`: 프로젝트 내 auto-increment
- UI 표시: 사이드바 트리, 검색, 실행 결과에서 TC ID로 식별

## 10.4 코드 실행 흐름

1. **TC 선택**: 사용자가 트리뷰에서 TC 클릭
2. **코드 로드**: TestCode 테이블에서 해당 TC의 최신 버전 코드 조회
3. **코드 편집**: Monaco Editor에서 수정 → 저장 시 version 증가
4. **실행 요청**: POST /api/runs/{id}/execute
5. **Docker 실행**: 선택된 language에 맞는 Docker 이미지에서 코드 실행
6. **결과 스트리밍**: WebSocket으로 stdout/stderr 실시간 전달
7. **결과 저장**: TestResult에 PASS/FAIL/SKIP/ERROR + 로그
8. **대시보드 반영**: 실행 완료 시 통계 자동 갱신

## 10.5 외부 프로젝트 연동 (향후 확장)

Phase 5 이후 외부 CI/CD에서도 TC Master에 결과를 전송할 수 있도록 **CLI Reporter**를 제공할 계획이다.

```bash
tcmaster report --project TCM --token $API_TOKEN --results ./test-results.json
```

- Playwright/Jest/Pytest 등 외부 테스트 프레임워크 결과를 JSON으로 수집
- TC ID 매핑: 코드 어노테이션 또는 테스트명 기반 자동 매칭
- REST API: POST /api/external/results 엔드포인트 제공

# 11. 진행 현황

기준일: 2026-02-22

| Phase | 상태 | 완료 커밋 | 비고 |
| --- | --- | --- | --- |
| Phase 1 - 기반 구축 | 완료 | `fbe8fc6` | Spring Boot/Next.js 기본 구조, JWT/Auth, 프로젝트 CRUD, 기본 레이아웃 |
| Phase 2 - TC 관리 핵심 | 완료 | `658fc98` | 스위트/케이스/코드 API, TC ID 생성, Split View 편집 화면 |
| Phase 3 - 실행 엔진 | 완료 | `618c5ae` | 실행/결과 도메인, 실행 API, WebSocket 스트리밍 스켈레톤 |
| Phase 4 - 대시보드 및 리포트 | 완료 | `95e3bad` | 대시보드/통계/리포트 API, 차트 위젯, CSV 내보내기 |

다음 작업 제안:

- 의존성 설치 후 backend/frontend 빌드 및 런타임 검증
- Flyway 마이그레이션 스크립트 작성
- Docker sandbox 실제 실행 엔진 구현 (현재 시뮬레이션)
