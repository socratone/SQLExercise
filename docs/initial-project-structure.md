# 초기 파일과 폴더 구성

이 문서는 Android Studio로 처음 생성된 Kotlin Android 프로젝트의 기본 파일과 폴더가 어떤 역할을 하는지 설명합니다.

## 프로젝트 구조

```text
SQLExercise/
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/io/github/socratone/sqlexercise/
│       │   │   ├── MainActivity.kt
│       │   │   └── ui/theme/
│       │   └── res/
│       ├── test/
│       └── androidTest/
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
└── gradlew.bat
```

## 각 폴더가 하는 일

### `app/`

실제 Android 앱 코드가 들어있는 모듈입니다. 보통 앱을 만들 때 가장 많이 수정하는 곳입니다.

### `app/src/main/`

앱이 실행될 때 사용하는 실제 코드와 리소스가 들어갑니다.

### `app/src/main/java/io/github/socratone/sqlexercise/`

Kotlin 소스 코드가 들어있는 패키지 폴더입니다.

- `MainActivity.kt`: 앱이 처음 실행될 때 열리는 메인 화면입니다.
- `ui/theme/`: Compose 화면의 색상, 글꼴, 테마 설정을 모아둔 폴더입니다.

### `app/src/main/res/`

이미지, 문자열, 색상, XML 설정 같은 Android 리소스가 들어갑니다. Kotlin 코드에서 직접 값을 쓰기보다 리소스로 관리하면 여러 화면과 언어에서 재사용하기 좋습니다.

- `values/strings.xml`: 앱 이름 같은 문자열을 저장합니다.
- `values/colors.xml`: XML 리소스에서 쓰는 색상 값을 저장합니다.
- `values/themes.xml`: Android 시스템 테마 설정을 저장합니다.
- `mipmap-*/`: 앱 아이콘 이미지가 해상도별로 들어갑니다.
- `drawable/`: 화면이나 아이콘에 사용할 그림 리소스가 들어갑니다.
- `xml/backup_rules.xml`: 앱 데이터 백업 규칙입니다.
- `xml/data_extraction_rules.xml`: Android 데이터 추출/백업 관련 규칙입니다.

### `app/src/test/`

일반 단위 테스트 코드가 들어갑니다. Android 기기나 에뮬레이터 없이 개발 컴퓨터에서 실행되는 테스트입니다.

- `ExampleUnitTest.kt`: 기본 예제 테스트 파일입니다.

### `app/src/androidTest/`

Android 기기나 에뮬레이터에서 실행되는 테스트 코드가 들어갑니다.

- `ExampleInstrumentedTest.kt`: 앱 패키지명이 맞는지 확인하는 기본 예제 테스트입니다.

### `gradle/`

Gradle 실행에 필요한 파일이 들어갑니다.

- `wrapper/`: 이 프로젝트에서 사용할 Gradle 버전을 자동으로 맞춰주는 Gradle Wrapper 파일입니다.
- `libs.versions.toml`: 라이브러리와 플러그인 버전을 한 곳에서 관리하는 파일입니다.

### `.idea/`

Android Studio 또는 IntelliJ IDEA가 사용하는 프로젝트 설정 폴더입니다. 직접 수정할 일은 거의 없습니다.

### `.gradle/`, `.kotlin/`

빌드 도구가 자동으로 만드는 캐시 폴더입니다. 직접 수정하지 않습니다.

## 주요 파일 설명

### `settings.gradle.kts`

프로젝트 이름과 포함할 모듈을 정합니다.

현재 설정:

- 프로젝트 이름: `SQL Exercise`
- 포함된 모듈: `:app`

### `build.gradle.kts`

프로젝트 전체에 적용되는 Gradle 설정입니다. 여기서는 Android 앱 플러그인과 Kotlin Compose 플러그인을 선언합니다.

### `app/build.gradle.kts`

앱 모듈의 빌드 설정입니다.

주요 내용:

- `namespace`: Kotlin/Android 코드의 기본 네임스페이스
- `applicationId`: 기기에 설치될 앱의 고유 ID
- `minSdk`: 앱이 지원하는 최소 Android 버전
- `targetSdk`: 앱이 목표로 하는 Android 버전
- `buildFeatures { compose = true }`: Jetpack Compose 사용 설정
- `dependencies`: 앱에서 사용하는 라이브러리 목록

### `app/src/main/AndroidManifest.xml`

Android 앱의 기본 정보를 선언하는 파일입니다.

주요 내용:

- 앱 아이콘
- 앱 이름
- 앱 테마
- 처음 실행할 화면인 `MainActivity`

### `app/src/main/java/io/github/socratone/sqlexercise/MainActivity.kt`

앱의 시작점입니다.

현재 흐름:

1. `MainActivity`가 실행됩니다.
2. `setContent { ... }` 안에서 Compose 화면을 그립니다.
3. `SQLExerciseTheme`으로 앱 테마를 적용합니다.
4. `Greeting("Android")`를 호출해서 `Hello Android!` 문구를 보여줍니다.

### `app/src/main/java/io/github/socratone/sqlexercise/ui/theme/Color.kt`

앱 테마에서 사용할 색상을 정의합니다.

### `app/src/main/java/io/github/socratone/sqlexercise/ui/theme/Type.kt`

앱 테마에서 사용할 글꼴 스타일을 정의합니다.

### `app/src/main/java/io/github/socratone/sqlexercise/ui/theme/Theme.kt`

앱 전체에 적용할 Material Design 테마를 정의합니다. 다크 모드와 Android 12 이상에서 지원하는 동적 색상도 이 파일에서 처리합니다.

### `gradle/libs.versions.toml`

라이브러리 버전을 모아두는 파일입니다.

예를 들어 `app/build.gradle.kts`에서 `libs.androidx.core.ktx`처럼 짧게 쓰면, 실제 라이브러리 이름과 버전은 이 파일에서 찾습니다.

### `gradle.properties`

Gradle 빌드 옵션을 설정하는 파일입니다. 빌드 속도, AndroidX 사용 여부 같은 프로젝트 설정이 들어갑니다.

### `gradlew`, `gradlew.bat`

Gradle Wrapper 실행 파일입니다.

- macOS/Linux: `./gradlew`
- Windows: `gradlew.bat`

Android Studio 없이 터미널에서 빌드하거나 테스트할 때 사용합니다.

### `local.properties`

개발자 PC의 Android SDK 위치 같은 로컬 설정이 들어갑니다. 사람마다 경로가 다르기 때문에 보통 Git에 올리지 않습니다.

### `.gitignore`

Git에 올리지 않을 파일과 폴더를 정합니다. 예를 들어 빌드 결과물, 캐시, 로컬 설정 파일 등이 여기에 포함됩니다.
