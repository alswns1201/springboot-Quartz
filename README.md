# Quartz Scheduler Playground

Spring Boot 기반의 Quartz Scheduler 예제 프로젝트로, **동적으로 Job을 등록, 조회, 삭제**할 수 있는 기능을 제공합니다.

이 프로젝트는 학습/테스트 목적의 “플레이그라운드”용이며, 간단한 `HelloJobs` 실행을 통해 Timer 관리와 TriggerListener 사용 방법을 보여줍니다.

---

## 📦 프로젝트 구조
```
com.scheduler.Quartz
├─ jobs
│ └─ HelloJobs.java // 샘플 Job 클래스, TimerInfo 기반 로그 출력
├─ playground
│ ├─ PlaygroundController.java // REST API 컨트롤러
│ └─ PlaygroundService.java // Controller와 SchedulerService 연결
├─ timeservices
│ ├─ SchedulerService.java // Quartz 스케줄러 래퍼, Job 등록/삭제/조회/업데이트
│ └─ SimpleTriggerListener.java // TriggerListener, 남은 실행 횟수 차감 관리
├─ util
│ └─ TimerUtils.java // JobDetail / Trigger 생성 유틸
└─ info
└─ TimerInfo.java // Timer 설정 정보 DTO
```

---

## 🛠 주요 기능

### 1. Job 등록
- `HelloJobs` 클래스를 기반으로 TimerInfo를 설정 후 스케줄 등록
- `totalFireCount`, `remainingFireCount`, `repeatIntervalMs`, `initialOffsetMs` 등을 설정 가능

### 2. 실행 중인 Timer 조회
- 모든 실행 중 Timer 조회: `/api/timer`
- 특정 Timer 조회: `/api/timer/{timerId}`

### 3. Timer 삭제
- 등록된 Timer 삭제: `/api/timer/{timerId}`

### 4. TriggerListener 활용
- `SimpleTriggerListener`에서 `triggerFired` 시 남은 실행 횟수를 차감하고 `SchedulerService`를 통해 상태를 갱신

---

## ⚙️ TimerInfo 예제 구조

```java
TimerInfo info = new TimerInfo();
info.setTotalFireCount(5);         // 총 실행 횟수
info.setRemainingFireCount(5);     // 남은 실행 횟수
info.setRepeatIntervalMs(5000);    // 반복 간격 (ms)
info.setInitialOffsetMs(1000);     // 최초 시작 지연 시간 (ms)
info.setCallbackData("My callback data"); // Job 내부에서 활용 가능


🚀 REST API 예제
Method	Endpoint	Description
POST	/api/timer/runHelloJob	HelloJobs 실행 등록
GET	/api/timer	모든 실행 중 Timer 조회
GET	/api/timer/{timerId}	특정 Timer 조회
DELETE	/api/timer/{timerId}	Timer 삭제
예시

등록

POST /api/timer/runHelloJob


조회

GET /api/timer
GET /api/timer/HelloJobs


삭제

DELETE /api/timer/HelloJobs

💡 동작 원리

PlaygroundService에서 TimerInfo를 생성 후 SchedulerService.schedule() 호출

TimerUtils에서 JobDetail과 Trigger 생성

Quartz Scheduler에 Job 등록

TriggerListener에서 실행 시 남은 Fire Count 차감 및 상태 갱신

Controller를 통해 조회/삭제 가능

⚠️ 주의 사항

remainingFireCount는 Trigger가 실제 실행될 때만 차감됩니다

SchedulerService.updateTimer() 호출이 없으면 DB/영속 상태는 갱신되지 않습니다

Playground용 샘플이므로 production 환경에서는 에러 처리, 동시성, persist 등 추가 필요

📌 요구 사항

Java 17+

Spring Boot 3.x

Quartz 2.x
