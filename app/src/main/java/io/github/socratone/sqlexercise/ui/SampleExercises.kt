package io.github.socratone.sqlexercise.ui

private fun exercise(
    id: Int,
    title: String,
    question: String,
    expectedSql: String,
    orderSensitive: Boolean = false,
) = LevelExercise(
    id = id,
    levelTitle = "문제 $id · $title",
    question = question,
    expectedSql = expectedSql.trimIndent(),
    orderSensitive = orderSensitive,
)

internal val sampleExercises = listOf(
    exercise(1, "전체 직원 조회", "employees 테이블의 모든 컬럼과 행을 조회하세요.", "SELECT * FROM employees"),
    exercise(2, "직원 연락처 조회", "모든 직원의 first_name, last_name, email을 이 순서로 조회하세요.", "SELECT first_name, last_name, email FROM employees"),
    exercise(3, "부서 목록 조회", "모든 부서의 department_id와 department_name을 이 순서로 조회하세요.", "SELECT department_id, department_name FROM departments"),
    exercise(4, "열 별칭 사용", "모든 직원의 first_name과 last_name을 각각 firstName, lastName 별칭으로 조회하세요.", "SELECT first_name AS firstName, last_name AS lastName FROM employees"),
    exercise(5, "직무 중복 제거", "직원에게 현재 지정된 job_id를 중복 없이 조회하세요.", "SELECT DISTINCT job_id FROM employees"),
    exercise(6, "부서 ID 중복 제거", "직원의 department_id를 중복 없이 조회하세요. 소속 부서가 없는 직원의 NULL도 포함합니다.", "SELECT DISTINCT department_id FROM employees"),

    exercise(7, "고액 급여 직원", "급여가 10000 이상인 직원의 employee_id, first_name, last_name, salary를 이 순서로 조회하세요.", "SELECT employee_id, first_name, last_name, salary FROM employees WHERE salary >= 10000"),
    exercise(8, "급여 범위 검색", "급여가 5000 이상 10000 이하인 직원의 employee_id, first_name, last_name, salary를 조회하세요.", "SELECT employee_id, first_name, last_name, salary FROM employees WHERE salary BETWEEN 5000 AND 10000"),
    exercise(9, "특정 직무 검색", "job_id가 SW_ENG 또는 SA_REP인 직원의 employee_id, first_name, last_name, job_id를 조회하세요.", "SELECT employee_id, first_name, last_name, job_id FROM employees WHERE job_id IN ('SW_ENG', 'SA_REP')"),
    exercise(10, "커미션 대상자", "커미션을 받는 직원의 employee_id, first_name, last_name, commission_pct를 조회하세요.", "SELECT employee_id, first_name, last_name, commission_pct FROM employees WHERE commission_pct IS NOT NULL"),
    exercise(11, "미소속 직원", "소속 부서가 없는 직원의 employee_id, first_name, last_name, department_id를 조회하세요.", "SELECT employee_id, first_name, last_name, department_id FROM employees WHERE department_id IS NULL"),
    exercise(12, "성으로 검색", "last_name이 S로 시작하는 직원의 employee_id, first_name, last_name을 조회하세요.", "SELECT employee_id, first_name, last_name FROM employees WHERE last_name LIKE 'S%'"),
    exercise(13, "최근 입사자", "2020-01-01 이후에 입사한 직원의 employee_id, first_name, last_name, hire_date를 조회하세요. 2020-01-01도 포함합니다.", "SELECT employee_id, first_name, last_name, hire_date FROM employees WHERE hire_date >= '2020-01-01'"),
    exercise(14, "급여와 NULL 조건", "급여가 8000 이상이고 커미션을 받지 않는 직원의 employee_id, first_name, last_name, salary를 조회하세요.", "SELECT employee_id, first_name, last_name, salary FROM employees WHERE salary >= 8000 AND commission_pct IS NULL"),
    exercise(15, "급여순 정렬", "모든 직원의 employee_id, first_name, last_name, salary를 급여 내림차순으로 조회하세요. 급여가 같으면 employee_id 오름차순으로 정렬합니다.", "SELECT employee_id, first_name, last_name, salary FROM employees ORDER BY salary DESC, employee_id ASC", true),
    exercise(16, "도시 정렬", "모든 근무지의 location_id와 city를 도시 오름차순으로 조회하세요. 도시가 같으면 location_id 오름차순으로 정렬합니다.", "SELECT location_id, city FROM locations ORDER BY city ASC, location_id ASC", true),

    exercise(17, "전체 이름 만들기", "직원의 employee_id와 first_name, 공백, last_name을 연결한 full_name을 조회하세요.", "SELECT employee_id, first_name || ' ' || last_name AS full_name FROM employees"),
    exercise(18, "연봉 계산", "직원의 employee_id, salary, salary에 12를 곱한 annual_salary를 조회하세요.", "SELECT employee_id, salary, salary * 12 AS annual_salary FROM employees"),
    exercise(19, "NULL 대체", "직원의 employee_id와 commission_pct를 조회하되 NULL은 0으로 표시하세요.", "SELECT employee_id, COALESCE(commission_pct, 0) AS commission_pct FROM employees"),
    exercise(20, "이메일 소문자 변환", "직원의 employee_id와 email을 소문자로 변환한 lower_email을 조회하세요.", "SELECT employee_id, LOWER(email) AS lower_email FROM employees"),
    exercise(21, "이름 길이", "직원의 employee_id, last_name, last_name의 글자 수인 name_length를 조회하세요.", "SELECT employee_id, last_name, LENGTH(last_name) AS name_length FROM employees"),
    exercise(22, "급여 등급", "직원의 employee_id와 salary, salary_grade를 조회하세요. 급여가 15000 이상이면 HIGH, 8000 이상이면 MEDIUM, 나머지는 LOW입니다.", "SELECT employee_id, salary, CASE WHEN salary >= 15000 THEN 'HIGH' WHEN salary >= 8000 THEN 'MEDIUM' ELSE 'LOW' END AS salary_grade FROM employees"),
    exercise(23, "입사 연도 추출", "직원의 employee_id, hire_date, 네 자리 입사 연도 hire_year를 조회하세요.", "SELECT employee_id, hire_date, SUBSTR(hire_date, 1, 4) AS hire_year FROM employees"),
    exercise(24, "커미션 포함 보수", "직원의 employee_id, salary, salary * (1 + commission_pct)로 계산한 expected_monthly_compensation을 조회하세요. 커미션 NULL은 0으로 계산합니다.", "SELECT employee_id, salary, salary * (1 + COALESCE(commission_pct, 0)) AS expected_monthly_compensation FROM employees"),

    exercise(25, "전체 직원 수", "전체 직원 수를 employee_count로 조회하세요.", "SELECT COUNT(*) AS employee_count FROM employees"),
    exercise(26, "급여 통계", "전체 직원 급여의 min_salary, max_salary, 소수점 둘째 자리로 반올림한 avg_salary를 조회하세요.", "SELECT MIN(salary) AS min_salary, MAX(salary) AS max_salary, ROUND(AVG(salary), 2) AS avg_salary FROM employees"),
    exercise(27, "부서별 직원 수", "department_id별 employee_count를 조회하세요. NULL 부서도 하나의 그룹으로 포함합니다.", "SELECT department_id, COUNT(*) AS employee_count FROM employees GROUP BY department_id"),
    exercise(28, "직무별 평균 급여", "job_id별 소수점 둘째 자리로 반올림한 avg_salary를 조회하세요.", "SELECT job_id, ROUND(AVG(salary), 2) AS avg_salary FROM employees GROUP BY job_id"),
    exercise(29, "대규모 부서", "직원이 5명 이상인 department_id와 employee_count를 조회하세요. NULL 부서는 제외합니다.", "SELECT department_id, COUNT(*) AS employee_count FROM employees WHERE department_id IS NOT NULL GROUP BY department_id HAVING COUNT(*) >= 5"),
    exercise(30, "고평균 직무", "평균 급여가 8000 이상인 job_id와 소수점 둘째 자리로 반올림한 avg_salary를 조회하세요.", "SELECT job_id, ROUND(AVG(salary), 2) AS avg_salary FROM employees GROUP BY job_id HAVING AVG(salary) >= 8000"),
    exercise(31, "커미션 조건부 집계", "department_id별 commission_employee_count와 non_commission_employee_count를 조회하세요. NULL 부서도 포함합니다.", "SELECT department_id, SUM(CASE WHEN commission_pct IS NOT NULL THEN 1 ELSE 0 END) AS commission_employee_count, SUM(CASE WHEN commission_pct IS NULL THEN 1 ELSE 0 END) AS non_commission_employee_count FROM employees GROUP BY department_id"),
    exercise(32, "연도별 입사자", "네 자리 hire_year와 employee_count를 연도 오름차순으로 조회하세요.", "SELECT SUBSTR(hire_date, 1, 4) AS hire_year, COUNT(*) AS employee_count FROM employees GROUP BY SUBSTR(hire_date, 1, 4) ORDER BY hire_year ASC", true),

    exercise(33, "직원과 부서", "소속 부서가 있는 직원의 employee_id, first_name, last_name, department_name을 조회하세요.", "SELECT e.employee_id, e.first_name, e.last_name, d.department_name FROM employees e JOIN departments d ON d.department_id = e.department_id"),
    exercise(34, "직원과 직무", "직원의 employee_id, first_name, last_name, job_title을 조회하세요.", "SELECT e.employee_id, e.first_name, e.last_name, j.job_title FROM employees e JOIN jobs j ON j.job_id = e.job_id"),
    exercise(35, "부서와 도시", "근무지가 있는 부서의 department_id, department_name, city를 조회하세요.", "SELECT d.department_id, d.department_name, l.city FROM departments d JOIN locations l ON l.location_id = d.location_id"),
    exercise(36, "직원의 근무 도시", "소속 부서와 근무지가 있는 직원의 employee_id, first_name, last_name, department_name, city를 조회하세요.", "SELECT e.employee_id, e.first_name, e.last_name, d.department_name, l.city FROM employees e JOIN departments d ON d.department_id = e.department_id JOIN locations l ON l.location_id = d.location_id"),
    exercise(37, "지역까지 다중 조인", "직원의 employee_id, first_name, last_name, job_title, country_name, region_name을 조회하세요. 부서와 근무지가 있는 직원만 포함합니다.", "SELECT e.employee_id, e.first_name, e.last_name, j.job_title, c.country_name, r.region_name FROM employees e JOIN jobs j ON j.job_id = e.job_id JOIN departments d ON d.department_id = e.department_id JOIN locations l ON l.location_id = d.location_id JOIN countries c ON c.country_id = l.country_id JOIN regions r ON r.region_id = c.region_id"),
    exercise(38, "빈 부서를 포함한 집계", "모든 부서의 department_id, department_name, employee_count를 조회하세요. 직원이 없는 부서도 0으로 포함합니다.", "SELECT d.department_id, d.department_name, COUNT(e.employee_id) AS employee_count FROM departments d LEFT JOIN employees e ON e.department_id = d.department_id GROUP BY d.department_id, d.department_name"),
    exercise(39, "직원과 직속 관리자", "모든 직원의 employee_id, first_name, last_name과 manager_id, manager_first_name, manager_last_name을 조회하세요. 관리자가 없으면 관리자 컬럼은 NULL입니다.", "SELECT e.employee_id, e.first_name, e.last_name, m.employee_id AS manager_id, m.first_name AS manager_first_name, m.last_name AS manager_last_name FROM employees e LEFT JOIN employees m ON m.employee_id = e.manager_id"),
    exercise(40, "부서 책임자", "모든 부서의 department_id, department_name, manager_id, manager_first_name, manager_last_name을 조회하세요. 책임자가 없는 부서도 포함합니다.", "SELECT d.department_id, d.department_name, m.employee_id AS manager_id, m.first_name AS manager_first_name, m.last_name AS manager_last_name FROM departments d LEFT JOIN employees m ON m.employee_id = d.manager_id"),
    exercise(41, "직원이 없는 부서", "직원이 한 명도 없는 부서의 department_id와 department_name을 조회하세요.", "SELECT d.department_id, d.department_name FROM departments d LEFT JOIN employees e ON e.department_id = d.department_id GROUP BY d.department_id, d.department_name HAVING COUNT(e.employee_id) = 0"),
    exercise(42, "근무지 없는 부서", "location_id가 없는 부서의 department_id와 department_name을 조회하세요.", "SELECT department_id, department_name FROM departments WHERE location_id IS NULL"),

    exercise(43, "평균 이상 급여", "전체 평균 급여보다 높은 직원의 employee_id, first_name, last_name, salary를 조회하세요.", "SELECT employee_id, first_name, last_name, salary FROM employees WHERE salary > (SELECT AVG(salary) FROM employees)"),
    exercise(44, "부서 평균 이상 급여", "자신의 부서 평균보다 높은 급여를 받는 직원의 employee_id, department_id, salary를 조회하세요. 부서 없는 직원은 제외합니다.", "SELECT e.employee_id, e.department_id, e.salary FROM employees e WHERE e.department_id IS NOT NULL AND e.salary > (SELECT AVG(c.salary) FROM employees c WHERE c.department_id = e.department_id)"),
    exercise(45, "최고 급여자", "회사 최고 급여를 받는 직원의 employee_id, first_name, last_name, salary를 조회하세요. 공동 1위도 포함합니다.", "SELECT employee_id, first_name, last_name, salary FROM employees WHERE salary = (SELECT MAX(salary) FROM employees)"),
    exercise(46, "직무 최소 급여 위반", "현재 급여가 해당 직무의 min_salary보다 낮은 직원의 employee_id, job_id, salary, min_salary를 조회하세요.", "SELECT e.employee_id, e.job_id, e.salary, j.min_salary FROM employees e JOIN jobs j ON j.job_id = e.job_id WHERE e.salary < j.min_salary"),
    exercise(47, "EXISTS 부서 검색", "현재 직원이 한 명 이상 있는 부서의 department_id와 department_name을 조회하세요.", "SELECT d.department_id, d.department_name FROM departments d WHERE EXISTS (SELECT 1 FROM employees e WHERE e.department_id = d.department_id)"),
    exercise(48, "직무 이력 보유자", "과거 직무 이력이 있는 직원의 employee_id, first_name, last_name을 중복 없이 조회하세요.", "SELECT e.employee_id, e.first_name, e.last_name FROM employees e WHERE EXISTS (SELECT 1 FROM job_history h WHERE h.employee_id = e.employee_id)"),
    exercise(49, "현재·과거 직무 합집합", "현재 또는 과거에 직원에게 배정된 모든 job_id를 중복 없이 조회하세요.", "SELECT job_id FROM employees UNION SELECT job_id FROM job_history"),
    exercise(50, "과거에만 사용된 직무", "현재 직원에게는 사용되지 않지만 과거 직무 이력에는 존재하는 job_id를 조회하세요.", "SELECT job_id FROM job_history EXCEPT SELECT job_id FROM employees"),

    exercise(51, "최고 평균 급여 부서", "CTE로 부서별 평균 급여를 계산하고 평균이 가장 높은 부서의 department_id, department_name, 소수점 둘째 자리 avg_salary를 조회하세요. 공동 1위도 포함합니다.", """
        WITH department_averages AS (
            SELECT department_id, AVG(salary) AS avg_salary
            FROM employees
            WHERE department_id IS NOT NULL
            GROUP BY department_id
        )
        SELECT d.department_id, d.department_name, ROUND(a.avg_salary, 2) AS avg_salary
        FROM department_averages a
        JOIN departments d ON d.department_id = a.department_id
        WHERE a.avg_salary = (SELECT MAX(avg_salary) FROM department_averages)
    """),
    exercise(52, "전체 평균과 급여 차이", "직원의 employee_id, salary, 전체 평균과의 차이를 소수점 둘째 자리로 반올림한 salary_difference를 조회하세요.", "SELECT employee_id, salary, ROUND(salary - AVG(salary) OVER (), 2) AS salary_difference FROM employees"),
    exercise(53, "부서별 급여 순위", "직원의 employee_id, department_id, salary와 부서별 급여 내림차순 DENSE_RANK인 salary_rank를 조회하세요. 부서 없는 직원도 하나의 그룹으로 포함합니다.", "SELECT employee_id, department_id, salary, DENSE_RANK() OVER (PARTITION BY department_id ORDER BY salary DESC) AS salary_rank FROM employees"),
    exercise(54, "부서별 급여 상위 3위", "부서별 급여 순위가 3위 이내인 직원의 employee_id, department_id, salary, salary_rank를 조회하세요. 공동 순위도 포함하고 부서 없는 직원은 제외합니다.", """
        WITH ranked AS (
            SELECT employee_id, department_id, salary,
                   DENSE_RANK() OVER (PARTITION BY department_id ORDER BY salary DESC) AS salary_rank
            FROM employees
            WHERE department_id IS NOT NULL
        )
        SELECT employee_id, department_id, salary, salary_rank
        FROM ranked
        WHERE salary_rank <= 3
    """),
    exercise(55, "직무 평균과 개인 급여", "직원의 employee_id, job_id, salary와 직무별 평균 급여를 소수점 둘째 자리로 반올림한 job_avg_salary를 조회하세요.", "SELECT employee_id, job_id, salary, ROUND(AVG(salary) OVER (PARTITION BY job_id), 2) AS job_avg_salary FROM employees"),
    exercise(56, "입사자 누적 합계", "직원의 employee_id, first_name, last_name, hire_date와 입사일 순 누적 직원 수 cumulative_employee_count를 조회하세요. 같은 입사일이면 employee_id 오름차순으로 정렬합니다.", "SELECT employee_id, first_name, last_name, hire_date, COUNT(*) OVER (ORDER BY hire_date, employee_id ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) AS cumulative_employee_count FROM employees ORDER BY hire_date, employee_id", true),
    exercise(57, "직전 입사자", "직원의 employee_id, department_id, hire_date와 같은 부서에서 바로 전에 입사한 previous_employee_id, previous_hire_date를 조회하세요. 부서 없는 직원은 제외하고 입사일과 employee_id 순서를 사용합니다.", "SELECT employee_id, department_id, hire_date, LAG(employee_id) OVER (PARTITION BY department_id ORDER BY hire_date, employee_id) AS previous_employee_id, LAG(hire_date) OVER (PARTITION BY department_id ORDER BY hire_date, employee_id) AS previous_hire_date FROM employees WHERE department_id IS NOT NULL"),
    exercise(58, "개인 급여 비율", "직원의 employee_id, salary와 전체 급여에서 차지하는 백분율 salary_percentage를 소수점 둘째 자리로 반올림해 조회하세요.", "SELECT employee_id, salary, ROUND(salary * 100.0 / SUM(salary) OVER (), 2) AS salary_percentage FROM employees"),

    exercise(59, "재귀 조직도", "재귀 CTE로 최상위 관리자부터 모든 직원의 employee_id, full_name, manager_id, hierarchy_depth를 조회하세요. 최상위 깊이는 0입니다.", """
        WITH RECURSIVE organization AS (
            SELECT employee_id, first_name || ' ' || last_name AS full_name, manager_id, 0 AS hierarchy_depth
            FROM employees
            WHERE manager_id IS NULL
            UNION ALL
            SELECT e.employee_id, e.first_name || ' ' || e.last_name, e.manager_id, o.hierarchy_depth + 1
            FROM employees e
            JOIN organization o ON e.manager_id = o.employee_id
        )
        SELECT employee_id, full_name, manager_id, hierarchy_depth FROM organization
    """),
    exercise(60, "전체 부하 직원 수", "각 관리자의 manager_id, first_name, last_name과 모든 직속·간접 부하 직원 수 total_report_count를 조회하세요.", """
        WITH RECURSIVE reports(manager_id, employee_id) AS (
            SELECT manager_id, employee_id FROM employees WHERE manager_id IS NOT NULL
            UNION ALL
            SELECT r.manager_id, e.employee_id
            FROM reports r
            JOIN employees e ON e.manager_id = r.employee_id
        )
        SELECT m.employee_id AS manager_id, m.first_name, m.last_name, COUNT(*) AS total_report_count
        FROM reports r
        JOIN employees m ON m.employee_id = r.manager_id
        GROUP BY m.employee_id, m.first_name, m.last_name
    """),
    exercise(61, "지역별 인력 통계", "모든 지역의 region_id, region_name, employee_count, 소수점 둘째 자리 avg_salary, max_salary를 조회하세요. 직원이 없는 지역은 employee_count 0, 급여 통계 NULL로 표시합니다.", """
        SELECT r.region_id, r.region_name, COUNT(e.employee_id) AS employee_count,
               ROUND(AVG(e.salary), 2) AS avg_salary, MAX(e.salary) AS max_salary
        FROM regions r
        LEFT JOIN countries c ON c.region_id = r.region_id
        LEFT JOIN locations l ON l.country_id = c.country_id
        LEFT JOIN departments d ON d.location_id = l.location_id
        LEFT JOIN employees e ON e.department_id = d.department_id
        GROUP BY r.region_id, r.region_name
    """),
    exercise(62, "부서별 급여 비중", "모든 부서의 department_id, department_name, total_salary와 전체 급여 대비 백분율 salary_percentage를 소수점 둘째 자리로 조회하세요. 직원 없는 부서는 0으로 표시하고 비율 내림차순, department_id 오름차순으로 정렬합니다.", """
        SELECT d.department_id, d.department_name, COALESCE(SUM(e.salary), 0) AS total_salary,
               ROUND(COALESCE(SUM(e.salary), 0) * 100.0 / (SELECT SUM(salary) FROM employees), 2) AS salary_percentage
        FROM departments d
        LEFT JOIN employees e ON e.department_id = d.department_id
        GROUP BY d.department_id, d.department_name
        ORDER BY salary_percentage DESC, d.department_id ASC
    """, true),
    exercise(63, "직무별 상위 10%", "직무별 급여 내림차순 NTILE(10)이 1인 직원의 employee_id, job_id, salary를 조회하세요. 같은 급여는 employee_id 오름차순으로 구분합니다.", """
        WITH salary_tiles AS (
            SELECT employee_id, job_id, salary,
                   NTILE(10) OVER (PARTITION BY job_id ORDER BY salary DESC, employee_id) AS salary_tile
            FROM employees
        )
        SELECT employee_id, job_id, salary FROM salary_tiles WHERE salary_tile = 1
    """),
    exercise(64, "가장 최근 과거 직무", "모든 직원의 employee_id, 현재 job_id인 current_job_id, 가장 최근 이력의 job_id인 previous_job_id와 previous_end_date를 조회하세요. 이력이 없으면 과거 컬럼은 NULL입니다.", """
        WITH latest_history AS (
            SELECT employee_id, job_id, end_date,
                   ROW_NUMBER() OVER (PARTITION BY employee_id ORDER BY start_date DESC) AS history_rank
            FROM job_history
        )
        SELECT e.employee_id, e.job_id AS current_job_id, h.job_id AS previous_job_id, h.end_date AS previous_end_date
        FROM employees e
        LEFT JOIN latest_history h ON h.employee_id = e.employee_id AND h.history_rank = 1
    """),
    exercise(65, "과거 총 근무 일수", "직무 이력이 있는 직원의 employee_id, first_name, last_name과 모든 이력 기간의 일수 합계 total_history_days를 조회하세요. 종료일에서 시작일을 뺀 값을 사용합니다.", "SELECT e.employee_id, e.first_name, e.last_name, CAST(SUM(julianday(h.end_date) - julianday(h.start_date)) AS INTEGER) AS total_history_days FROM employees e JOIN job_history h ON h.employee_id = e.employee_id GROUP BY e.employee_id, e.first_name, e.last_name"),
    exercise(66, "최다 직무 변경자", "직무 이력 건수가 가장 많은 직원의 employee_id, first_name, last_name, change_count를 조회하세요. 공동 1위도 포함합니다.", """
        WITH change_counts AS (
            SELECT employee_id, COUNT(*) AS change_count FROM job_history GROUP BY employee_id
        )
        SELECT e.employee_id, e.first_name, e.last_name, c.change_count
        FROM change_counts c
        JOIN employees e ON e.employee_id = c.employee_id
        WHERE c.change_count = (SELECT MAX(change_count) FROM change_counts)
    """),
    exercise(67, "같은 해 입사한 직원 쌍", "같은 부서에서 같은 해에 입사한 직원 쌍의 department_id, employee1_id, employee2_id, hire_year를 조회하세요. employee1_id가 더 작도록 하여 중복 쌍을 제거합니다.", "SELECT e1.department_id, e1.employee_id AS employee1_id, e2.employee_id AS employee2_id, SUBSTR(e1.hire_date, 1, 4) AS hire_year FROM employees e1 JOIN employees e2 ON e2.department_id = e1.department_id AND e1.employee_id < e2.employee_id AND SUBSTR(e1.hire_date, 1, 4) = SUBSTR(e2.hire_date, 1, 4) WHERE e1.department_id IS NOT NULL"),
    exercise(68, "부서별 급여 중앙값", "직원이 있는 부서의 department_id와 median_salary를 소수점 둘째 자리로 조회하세요. 직원 수가 짝수이면 가운데 두 급여의 평균을 사용합니다.", """
        WITH ranked AS (
            SELECT department_id, salary,
                   ROW_NUMBER() OVER (PARTITION BY department_id ORDER BY salary) AS row_number,
                   COUNT(*) OVER (PARTITION BY department_id) AS employee_count
            FROM employees
            WHERE department_id IS NOT NULL
        )
        SELECT department_id, ROUND(AVG(salary), 2) AS median_salary
        FROM ranked
        WHERE row_number IN ((employee_count + 1) / 2, (employee_count + 2) / 2)
        GROUP BY department_id
    """),
    exercise(69, "국가별 최고 평균 급여 부서", "각 국가에서 평균 급여가 가장 높은 부서의 country_id, country_name, department_id, department_name, 소수점 둘째 자리 avg_salary를 조회하세요. 직원이 있는 부서만 대상으로 하고 공동 1위도 포함합니다.", """
        WITH department_averages AS (
            SELECT c.country_id, c.country_name, d.department_id, d.department_name, AVG(e.salary) AS avg_salary
            FROM countries c
            JOIN locations l ON l.country_id = c.country_id
            JOIN departments d ON d.location_id = l.location_id
            JOIN employees e ON e.department_id = d.department_id
            GROUP BY c.country_id, c.country_name, d.department_id, d.department_name
        ), ranked AS (
            SELECT *, DENSE_RANK() OVER (PARTITION BY country_id ORDER BY avg_salary DESC) AS salary_rank
            FROM department_averages
        )
        SELECT country_id, country_name, department_id, department_name, ROUND(avg_salary, 2) AS avg_salary
        FROM ranked WHERE salary_rank = 1
    """),
    exercise(70, "한 번도 사용되지 않은 항목", "현재와 과거를 통틀어 직원이 한 번도 배정되지 않은 직무와 부서를 조회하세요. entity_type에는 JOB 또는 DEPARTMENT, entity_id에는 ID를 TEXT로 변환한 값, entity_name에는 이름을 표시합니다.", """
        SELECT 'JOB' AS entity_type, CAST(j.job_id AS TEXT) AS entity_id, j.job_title AS entity_name
        FROM jobs j
        WHERE NOT EXISTS (SELECT 1 FROM employees e WHERE e.job_id = j.job_id)
          AND NOT EXISTS (SELECT 1 FROM job_history h WHERE h.job_id = j.job_id)
        UNION ALL
        SELECT 'DEPARTMENT', CAST(d.department_id AS TEXT), d.department_name
        FROM departments d
        WHERE NOT EXISTS (SELECT 1 FROM employees e WHERE e.department_id = d.department_id)
          AND NOT EXISTS (SELECT 1 FROM job_history h WHERE h.department_id = d.department_id)
    """),
)
