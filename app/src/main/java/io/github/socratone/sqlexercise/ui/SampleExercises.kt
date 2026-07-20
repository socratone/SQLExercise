package io.github.socratone.sqlexercise.ui

/**
 * API가 연결되기 전 화면과 채점 동작을 확인하기 위한 임시 데이터입니다.
 * 네트워크 계층이 추가되면 이 목록을 Repository가 제공하는 데이터로 교체합니다.
 */
internal val sampleExercises = listOf(
    LevelExercise(
        id = 1,
        levelTitle = "1 level",
        question = "users 테이블의 모든 데이터를 조회하세요.",
        expectedSql = "SELECT * FROM users",
    ),
    LevelExercise(
        id = 2,
        levelTitle = "2 level",
        question = "users 테이블에서 name 컬럼만 조회하세요.",
        expectedSql = "SELECT name FROM users",
    ),
    LevelExercise(
        id = 3,
        levelTitle = "3 level",
        question = "users 테이블에서 age가 20 이상인 사용자를 조회하세요.",
        expectedSql = "SELECT * FROM users WHERE age >= 20",
    ),
    LevelExercise(
        id = 4,
        levelTitle = "4 level",
        question = "users 테이블의 데이터를 name 오름차순으로 조회하세요.",
        expectedSql = "SELECT * FROM users ORDER BY name ASC",
        orderSensitive = true,
    ),
    LevelExercise(
        id = 5,
        levelTitle = "5 level",
        question = "users 테이블의 전체 행 개수를 조회하세요.",
        expectedSql = "SELECT COUNT(*) FROM users",
    ),
    LevelExercise(
        id = 6,
        levelTitle = "6 level",
        question = "users 테이블에서 중복되지 않은 city 값을 조회하세요.",
        expectedSql = "SELECT DISTINCT city FROM users",
    ),
    LevelExercise(
        id = 7,
        levelTitle = "7 level",
        question = "orders 테이블에서 amount가 100보다 큰 주문을 조회하세요.",
        expectedSql = "SELECT * FROM orders WHERE amount > 100",
    ),
    LevelExercise(
        id = 8,
        levelTitle = "8 level",
        question = "orders 테이블에서 사용자별 user_id와 주문 금액 합계를 조회하세요.",
        expectedSql = "SELECT user_id, SUM(amount) FROM orders GROUP BY user_id",
    ),
    LevelExercise(
        id = 9,
        levelTitle = "9 level",
        question = "users와 orders를 사용자 ID로 조인하여 사용자 이름과 주문 금액을 조회하세요.",
        expectedSql = "SELECT users.name, orders.amount FROM users JOIN orders ON users.id = orders.user_id",
    ),
    LevelExercise(
        id = 10,
        levelTitle = "10 level",
        question = "모든 사용자 이름과 각 사용자의 주문 개수를 조회하세요. 주문이 없는 사용자도 포함합니다.",
        expectedSql = "SELECT users.name, COUNT(orders.id) FROM users LEFT JOIN orders ON users.id = orders.user_id GROUP BY users.name",
    ),
)
