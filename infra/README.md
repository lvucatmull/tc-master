# TC Master Docker 실행

## 실행

```bash
cd /Users/seongjoo/Desktop/portfolio/tcMasterProject
npm run up
```

직접 compose 파일을 지정해서 실행하려면:

```bash
docker compose -f infra/docker-compose.yml up --build
```

## 접속 주소

- Frontend: http://localhost:3000
- Backend: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html

## 종료

```bash
npm run down
```

데이터까지 삭제하려면:

```bash
docker compose -f infra/docker-compose.yml down -v
```
