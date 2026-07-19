package io.github.socratone.sqlexercise.ui

import java.util.Locale

private val whitespace = Regex("\\s+")

/** 입력 SQL과 저장된 정답을 동일한 형식으로 정리한 뒤 비교합니다. */
internal fun evaluateSqlAnswer(input: String, expected: String): SubmissionResult =
    if (normalizeSql(input) == normalizeSql(expected)) {
        SubmissionResult.Correct
    } else {
        SubmissionResult.Incorrect
    }

/**
 * 단순 문자열 차이로 오답 처리되지 않도록 대소문자, 연속 공백, 마지막 세미콜론을 정리합니다.
 * SQL의 실행 결과가 같은지까지 판단하는 역할은 향후 서버 채점 API가 담당합니다.
 */
internal fun normalizeSql(sql: String): String = sql
    .trim()
    .removeSuffix(";")
    .trim()
    .replace(whitespace, " ")
    .lowercase(Locale.ROOT)
