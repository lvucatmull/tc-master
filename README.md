# TC Master

테스트 케이스 관리 플랫폼입니다. 프로젝트 단위로 테스트 케이스를 생성, 관리, 추적할 수 있습니다.

## 기술 스택

### Backend
- Java 17
- Spring Boot 3.3
- Spring Data JPA
- PostgreSQL 15
- WebSocket (STOMP)
- Swagger (springdoc-openapi)

### Frontend
- Next.js 14
- React 18
- TypeScript
- Tailwind CSS
- Zustand (상태 관리)
- Recharts (차트)
- STOMP.js + SockJS (실시간 통신)

### Infra
- Docker / Docker Compose

## 프로젝트 구조

```
tcMasterProject/
├── backend/                  # Spring Boot API 서버
│   └── src/main/java/com/tcmaster/
│       ├── controller/       # REST 컨트롤러
│       ├── service/          # 비즈니스 로직
│       ├── entity/           # JPA 엔티티
│       ├── repository/       # 데이터 접근 계층
│       └── dto/              # 요청/응답 DTO
├── frontend/                 # Next.js 웹 클라이언트
│   └── src/
│       ├── app/              # 페이지 (App Router)
│       └── components/       # 재사용 컴포넌트
└── infra/                    # Docker 설정
    ├── docker-compose.yml
    ├── backend/Dockerfile
    └── frontend/Dockerfile
```

## 실행 방법

### 통합 실행 (권장)

루트에서 한 번에 전체 서비스를 실행합니다.

```bash
npm run up
```

동일한 동작을 직접 실행하려면:

```bash
docker compose -f infra/docker-compose.yml up --build
```

### 로컬 개발 환경에서 실행

#### 1. PostgreSQL 실행

PostgreSQL이 로컬에서 실행 중이어야 합니다. Docker로 DB만 띄울 수도 있습니다.

```bash
docker run -d \
  --name tcmaster-postgres \
  -e POSTGRES_DB=tcmaster \
  -e POSTGRES_USER=tcmaster \
  -e POSTGRES_PASSWORD=tcmaster \
  -p 5432:5432 \
  postgres:15-alpine
```

#### 2. Backend 실행

```bash
cd backend
gradle bootRun
```

서버가 `http://localhost:8080` 에서 실행됩니다.

#### 3. Frontend 실행

```bash
cd frontend
npm install
npm run dev
```

클라이언트가 `http://localhost:3000` 에서 실행됩니다.

## 접속 정보

| 서비스 | URL |
|--------|-----|
| Frontend | http://localhost:3000 |
| Backend API | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |

## API 엔드포인트

### 인증
| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/auth/register` | 회원가입 |
| POST | `/api/auth/login` | 로그인 |
| POST | `/api/auth/refresh` | 토큰 갱신 |

### 프로젝트
| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/projects` | 프로젝트 목록 조회 |
| POST | `/api/projects` | 프로젝트 생성 |
| GET | `/api/projects/{id}` | 프로젝트 상세 조회 |
| PUT | `/api/projects/{id}` | 프로젝트 수정 |
| DELETE | `/api/projects/{id}` | 프로젝트 삭제 |

## 테스트

### Backend
```bash
cd backend
gradle test
```

### Frontend
```bash
cd frontend
npm run test
```

## 종료

### Docker Compose 종료
```bash
npm run down
```

데이터 볼륨까지 삭제하려면:
```bash
docker compose -f infra/docker-compose.yml down -v
```
