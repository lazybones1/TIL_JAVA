# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

```bash
# Build
./gradlew build

# Run all tests
./gradlew test

# Run a single test class
./gradlew test --tests "org.example.YourTestClass"

# Run a single test method
./gradlew test --tests "org.example.YourTestClass.yourTestMethod"

# Run main class
./gradlew run
```

## Project Structure

- Java project managed by Gradle (Gradle 8.2 wrapper included)
- Group: `org.example`, project name: `studyWithClaude`
- JUnit Jupiter (JUnit 5) for testing
- Source root: `src/main/java/org/example/`
- Test root: `src/test/java/org/example/`

## Context

Java/Spring 백엔드 개발자가 실무 수준 이상의 실력을 키우기 위한 연습 프로젝트 모음.
사용자는 경력 있는 백엔드 개발자. 목표: "어디서든 백엔드 잘한다는 인식을 받을 실력"과 "만들고 싶은 걸 바로 만들 수 있는 실력".

## 진행 방식

- 4개 축을 병행. 새 과제는 가장 정체된 축 또는 직전 과제의 약점을 우선 고려.
- 과제마다: (1) 요구사항 제시 → (2) 사용자 구현 → (3) 시니어 개발자 시점 코드 리뷰 → (4) 다음 단계 추천.
- 코드 리뷰 시 정답을 바로 주지 말고 "왜 문제인지" 먼저 질문. 사용자가 "그냥 알려줘"라고 하면 바로 답변.
- 각 과제 종료 시 커밋 메시지에 "다룬 핵심 개념"과 "남은 약점" 기록.
- 과제 4~5개마다 시스템 설계 면접 시뮬레이션으로 실력 점검.

## 로드맵

### 축 1. 동시성 & 자바 코어 (진행 중)

- [x] producer-consumer — BlockingQueue, wait/notify
- [x] task-processor — ExecutorService, Future, CompletableFuture
- [x] connection-pool — 리소스 풀링, 타임아웃, 락
- [ ] **rate-limiter** — 토큰버킷/슬라이딩윈도우, 분산 환경 고려 ← 다음
- [ ] distributed-lock-simulator — Redis 기반 락, race condition 재현
- [ ] circuit-breaker — 장애 전파 차단, 상태머신 직접 구현

### 축 2. 데이터/트랜잭션

- [ ] order-system — 낙관적 락 vs 비관적 락 비교 구현, 트랜잭션 격리수준
- [ ] n+1-hunter — 일부러 N+1 만들고 fetch join/batch size로 해결
- [ ] saga-pattern-mini — 분산 트랜잭션 시뮬레이션, 보상 트랜잭션
- [ ] cache-aside-shop — Redis 캐싱, 정합성 깨짐 재현 후 해결

### 축 3. 시스템 설계 & 아키텍처

- [ ] 미니 설계 토론 (화이트보드 설계, 코드 없음)
- [ ] 직접 구현 + 트레이드오프 문서화
- [ ] 확장 시나리오 — 트래픽 100배 리팩토링
- [ ] 실전형 설계 면접 시뮬레이션

### 축 4. 실전 프로젝트 (축 1~3에서 6~8개 쌓인 뒤 시작)

- [ ] 사용자가 만들고 싶은 서비스 1개 선정 후 배포 및 부하테스트까지
