# SQL Exercise

SQL 문제를 레벨별로 풀어볼 수 있는 Kotlin Android 앱입니다. Jetpack Compose와 Material 3로 UI를 구성하고 Navigation Compose로 목록과 상세 화면을 연결합니다.

## 현재 기능

- `1 level`부터 `10 level`까지 세로 레벨 목록 제공
- 전체 너비의 outline 버튼을 통한 문제 상세 화면 이동
- 레벨별 SQL 문제와 정답 샘플 데이터 제공
- 여러 줄 SQL 입력, 초기화, 제출 기능
- 앱 내부 SQLite에서 사용자 SQL과 정답 SQL을 실행한 결과 기반 채점
- 일반 문제는 행 순서를 무시하고 `ORDER BY` 문제는 행 순서까지 비교
- 잘못된 문법이나 조회가 아닌 SQL에 대한 실행 오류 표시
- 정답 및 오답 결과 표시
- 시스템 설정과 관계없이 고정 다크 테마 적용
- 로딩, 콘텐츠, 오류 상태를 분리한 Compose UI 구조

현재 문제, 정답 SQL, `users`와 `orders` 샘플 데이터는 앱 내부에 있습니다. 제출할 때마다 새 인메모리 SQLite 데이터베이스를 생성하고 사용자 SQL과 정답 SQL을 같은 데이터에서 실행하므로, SQL 문자열이 달라도 결과가 같으면 정답으로 판단합니다. 데이터 준비 후 데이터베이스를 읽기 전용으로 전환하며 하나의 `SELECT` 또는 `WITH` 조회문만 허용합니다.

## 기술 구성

- Kotlin
- Jetpack Compose
- Material 3
- Navigation Compose
- JUnit
- Compose UI Test

지원하는 최소 Android 버전은 API 24이며, Java 11을 사용합니다.

## 주요 코드

```text
app/src/main/java/io/github/socratone/sqlexercise/
├── MainActivity.kt                 # 앱 진입점과 고정 다크 시스템 바 설정
└── ui/
    ├── SQLExerciseApp.kt           # 목록과 상세 화면 내비게이션
    ├── LevelListScreen.kt          # 레벨 목록 UI
    ├── LevelDetailScreen.kt        # 문제, SQL 입력, 초기화 및 제출 UI
    ├── LevelUiState.kt             # 목록/상세 데이터 모델과 UI 상태
    ├── SampleExercises.kt          # 레벨별 임시 문제와 정답
    ├── SqlAnswerEvaluator.kt       # 인메모리 SQLite 실행과 결과 비교 채점
    └── theme/                      # 고정 다크 색상, 글꼴 및 Compose 테마
```

프로젝트의 기본 폴더와 Gradle 파일에 대한 설명은 [초기 파일과 폴더 구성](docs/initial-project-structure.md)에서 확인할 수 있습니다.

## 실행 방법

Android Studio에서 프로젝트를 열고 에뮬레이터 또는 Android 기기를 선택한 뒤 `app` 구성을 실행합니다.

터미널에서는 프로젝트 루트에서 다음 명령어를 사용할 수 있습니다.

```bash
./gradlew assembleDebug
```

Debug APK를 빌드합니다.

```bash
./gradlew testDebugUnitTest
```

SQL 결과 비교 로직을 포함한 로컬 단위 테스트를 실행합니다. 실제 SQLite 실행 테스트는 연결된 기기나 에뮬레이터에서 계측 테스트로 실행됩니다.

```bash
./gradlew connectedDebugAndroidTest
```

연결된 기기나 에뮬레이터에서 목록, 상세 화면 및 테마 Compose 테스트를 실행합니다.

## 향후 API 연동

화면은 데이터와 이벤트를 전달받는 방식으로 구성되어 있습니다. API 연동 시에는 다음 부분을 교체하거나 확장합니다.

- `SampleExercises.kt`의 로컬 데이터를 Repository/API 응답으로 교체
- 목록 및 상세의 `UiState`를 ViewModel에서 제공
- 로컬 SQLite 채점 대신 제출 API의 채점 결과 사용
- 네트워크 로딩, 데이터 없음, 요청 실패 및 재시도 UI 연결

문제 화면 자체는 데이터 출처를 알지 않으므로, API 연동 후에도 기존 Compose UI를 재사용할 수 있습니다.
