# SQL Exercise

Kotlin으로 만든 Android 앱 프로젝트입니다. 현재는 Jetpack Compose 기본 템플릿 상태에 가깝고, 화면에는 `Hello Android!` 문구를 보여줍니다.

## 문서

- [초기 파일과 폴더 구성](docs/initial-project-structure.md)

## 앞으로 주로 수정하게 될 곳

처음에는 아래 파일들을 중심으로 보면 됩니다.

- 화면을 바꾸고 싶을 때: `app/src/main/java/io/github/socratone/sqlexercise/MainActivity.kt`
- 앱 이름을 바꾸고 싶을 때: `app/src/main/res/values/strings.xml`
- 색상이나 테마를 바꾸고 싶을 때: `app/src/main/java/io/github/socratone/sqlexercise/ui/theme/`
- 라이브러리를 추가하고 싶을 때: `app/build.gradle.kts`, `gradle/libs.versions.toml`
- 앱 권한이나 시작 화면 설정을 바꾸고 싶을 때: `app/src/main/AndroidManifest.xml`

## 자주 쓰는 Gradle 명령어

터미널에서 프로젝트 루트 폴더 기준으로 실행합니다.

```bash
./gradlew build
```

앱을 빌드합니다.

```bash
./gradlew test
```

일반 단위 테스트를 실행합니다.

```bash
./gradlew connectedAndroidTest
```

연결된 Android 기기나 에뮬레이터에서 테스트를 실행합니다.

## 현재 앱 상태

아직 SQL 연습 기능은 구현되어 있지 않습니다. 현재는 Android 앱 프로젝트가 정상적으로 생성되었고, Compose 화면을 띄우는 기본 코드가 있는 상태입니다.
