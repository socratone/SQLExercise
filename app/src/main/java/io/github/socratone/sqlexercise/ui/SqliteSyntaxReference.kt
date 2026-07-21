package io.github.socratone.sqlexercise.ui

internal val sqliteSyntaxReference = """
    문자열 연결
    SQLite에서는 || 연산자로 문자열을 연결합니다.
    first_name || ' ' || last_name

    날짜와 시간
    SQLite에는 전용 날짜 타입이 없습니다. 이 앱은 날짜를
    YYYY-MM-DD 형식의 TEXT로 저장합니다. 연도는
    SUBSTR(hire_date, 1, 4), 두 날짜 사이의 일수는
    julianday(end_date) - julianday(start_date)로 구합니다.

    유연한 타입
    SQLite의 타입은 컬럼보다 값에 연결됩니다. 컬럼의 타입은
    저장할 값을 변환할 때 선호하는 타입을 뜻합니다. 별도의
    Boolean 타입은 없으며 보통 0(false), 1(true)을 사용합니다.

    숫자 연산
    정수끼리 나누면 소수 부분이 잘립니다. 실수 결과가 필요하면
    salary * 100.0 / total 또는 CAST(value AS REAL)을 사용합니다.

    NULL 처리
    NULL 비교에는 = NULL이 아니라 IS NULL 또는 IS NOT NULL을
    사용합니다. 대체값은 COALESCE(commission_pct, 0)처럼 지정합니다.

    형 변환
    CAST(value AS INTEGER), CAST(value AS REAL),
    CAST(value AS TEXT) 형태로 값을 명시적으로 변환합니다.
""".trimIndent()
