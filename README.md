# GC Board Plus – 분실물 게시판

> 분실/습득/해결 상태로 빠르게 연결되는 단일 페이지 분실물 게시판

## 배포
- 서비스 URL: https://port-0-gcproject-mhlm3dta8a880de4.sel3.cloudtype.app

## 1. 기획
- 문제: 일반 게시판에서 분실·습득 글이 뒤섞여 금방 묻힘.
- 목표: 상태(lost/found/resolved) 중심으로 글을 올리고, 상태 필터와 커서 스크롤로 빠르게 탐색·연결.
- 경험: 분실/습득 등록, 해결 전환, 내 글만 모아보기, 댓글/답글로 연락 조율.

## 2. 진단 및 개선

| 문제점 | 개선 방법 |
| --- | --- |
| 일관성 없는 에러 응답 | `@RestControllerAdvice` 전역 예외 처리로 도메인 예외를 명확한 HTTP 코드/메시지로 반환 |
| OFFSET 기반 목록의 비효율 | `lastId` 커서 기반 Slice 조회로 무한 스크롤 구현 |
| 댓글 FK로 인한 삭제 실패 | 게시글 삭제 전 댓글 선삭제, Path Model 기반 재귀 정리 |
| 이메일 기반 로그인의 마찰 | displayName+비밀번호 로그인, 소문자 정규화 및 JWT subject 통일 |

## 3. 핵심 기능
- 인증/권한: 아이디(displayName)+비밀번호 로그인, JWT 발급. 이메일은 내부용 더미로만 저장. 작성자만 글/댓글 수정·삭제.
- 게시글(분실물 카드)
  - 상태: `lost`(분실), `found`(습득), `resolved`(해결) – 기본 `lost`.
  - 상태 필터: `GET /api/articles?status=lost|found|resolved|all` 또는 `status` 생략 시 전체.
  - 커서 기반 목록(`lastId`, `nextCursor`), 단건 조회, 작성/수정/삭제.
  - “내 글” 피드: `GET /api/articles/mine`.
  - 해결 전환: 작성자만 “해결되었어요”로 `resolved` 상태 변경.
- 댓글: Path Model로 N-Depth 답글, 소프트 삭제 후 고아 재귀 정리.
- 프론트 데모 (`src/main/resources/static/index.html`)
  - 전체/상태 셀렉트/필터/배지, 내 글 탭, 해결 버튼, 답글/삭제, 커서 더보기.

## 4. 기술 스택
- Backend: Java 17, Spring Boot 3.3.5, Spring Security, Spring Data JPA, JWT(jjwt), MySQL, Gradle
- Front: 정적 HTML/CSS/JS (동일 서버에서 서빙)

## 5. 로컬 실행
1) 사전: JDK 17, MySQL(`project` DB)
2) 환경변수 예시
```
export DB_URL="jdbc:mysql://localhost:3306/project?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8&serverTimezone=Asia/Seoul"
export DB_USERNAME="root"
export DB_PASSWORD="your_pw"
```
3) 실행: `./gradlew bootRun`
4) 접속: `http://localhost:8080`

## 6. API 요약
- 인증: `POST /api/auth/register`, `POST /api/auth/login` (displayName+password, 소문자 기준)
- 게시글:
  - `GET /api/articles?lastId&size&status` 커서+상태 필터 목록 (`status` 생략/`all`이면 전체)
  - `GET /api/articles/{id}` 단건
  - `POST /api/articles` `{title, content, status}` (프론트는 `mood`로 보내도 허용)
  - `PUT /api/articles/{id}` `{title, content, status}` (프론트는 `mood`로 보내도 허용)
  - `DELETE /api/articles/{id}`
  - `GET /api/articles/mine?lastId&size` 내 글 목록 (토큰 필요)
- 댓글:
  - `GET /api/articles/{articleId}/comments`
  - `POST /api/articles/{articleId}/comments` `{content, parentId?}`
  - `PUT /api/comments/{commentId}`
  - `DELETE /api/comments/{commentId}`

## 7. 구조
- `src/main/java/bssm/be/auth` 인증/토큰
- `src/main/java/bssm/be/article` 분실물 글, 상태 필터, 커서 조회, 내 글
- `src/main/java/bssm/be/comment` Path Model, 재귀 삭제
- `src/main/java/bssm/be/common` 전역 예외, 보안 설정
- `src/main/resources/static/index.html` 프론트 데모

## 8. 시연 흐름
1) 회원가입/로그인 → JWT 저장 확인.
2) 분실/습득 등록 → 상태 배지 확인.
3) 상태 필터로 분실/습득/해결별 피드 전환, `더 보기`로 커서 스크롤.
4) 댓글/답글 작성·삭제, 해결 시 “해결되었어요”로 상태 전환.
5) “내 글” 탭에서 내가 올린 글만 모아 보고 관리.

## 9. 테스트/배포 메모
- `./gradlew test` 실행 (권한/네트워크 허용 환경 필요).
- 배포 시 DB/시크릿은 환경변수로 관리.
