package io.github.socratone.sqlexercise.data

import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

/** SQL 연습 문제 채점에 사용하는 고정 HR 데이터베이스를 구성합니다. */
internal fun SQLiteConnection.createHrDatabase() {
    execSQL("PRAGMA foreign_keys = ON")
    schemaStatements.forEach(::execSQL)
    seedStatements.forEach(::execSQL)
    departmentManagerStatements.forEach(::execSQL)
}

private val schemaStatements = listOf(
    """
    CREATE TABLE regions (
        region_id INTEGER PRIMARY KEY,
        region_name TEXT
    )
    """.trimIndent(),
    """
    CREATE TABLE countries (
        country_id TEXT PRIMARY KEY,
        country_name TEXT,
        region_id INTEGER REFERENCES regions(region_id)
    )
    """.trimIndent(),
    """
    CREATE TABLE locations (
        location_id INTEGER PRIMARY KEY,
        street_address TEXT,
        postal_code TEXT,
        city TEXT NOT NULL,
        state_province TEXT,
        country_id TEXT REFERENCES countries(country_id)
    )
    """.trimIndent(),
    """
    CREATE TABLE jobs (
        job_id TEXT PRIMARY KEY,
        job_title TEXT NOT NULL,
        min_salary REAL,
        max_salary REAL,
        CHECK (min_salary IS NULL OR max_salary IS NULL OR min_salary <= max_salary)
    )
    """.trimIndent(),
    """
    CREATE TABLE departments (
        department_id INTEGER PRIMARY KEY,
        department_name TEXT NOT NULL,
        manager_id INTEGER REFERENCES employees(employee_id),
        location_id INTEGER REFERENCES locations(location_id)
    )
    """.trimIndent(),
    """
    CREATE TABLE employees (
        employee_id INTEGER PRIMARY KEY,
        first_name TEXT,
        last_name TEXT NOT NULL,
        email TEXT NOT NULL UNIQUE,
        phone_number TEXT,
        hire_date TEXT NOT NULL,
        job_id TEXT NOT NULL REFERENCES jobs(job_id),
        salary REAL,
        commission_pct REAL,
        manager_id INTEGER REFERENCES employees(employee_id),
        department_id INTEGER REFERENCES departments(department_id)
    )
    """.trimIndent(),
    """
    CREATE TABLE job_history (
        employee_id INTEGER NOT NULL REFERENCES employees(employee_id),
        start_date TEXT NOT NULL,
        end_date TEXT NOT NULL,
        job_id TEXT NOT NULL REFERENCES jobs(job_id),
        department_id INTEGER REFERENCES departments(department_id),
        PRIMARY KEY (employee_id, start_date),
        CHECK (start_date < end_date)
    )
    """.trimIndent(),
)

private val seedStatements = listOf(
    "INSERT INTO regions VALUES (1, 'Europe')",
    "INSERT INTO regions VALUES (2, 'Americas')",
    "INSERT INTO regions VALUES (3, 'Asia Pacific')",
    "INSERT INTO regions VALUES (4, 'Middle East and Africa')",

    "INSERT INTO countries VALUES ('GB', 'United Kingdom', 1)",
    "INSERT INTO countries VALUES ('DE', 'Germany', 1)",
    "INSERT INTO countries VALUES ('FR', 'France', 1)",
    "INSERT INTO countries VALUES ('US', 'United States', 2)",
    "INSERT INTO countries VALUES ('CA', 'Canada', 2)",
    "INSERT INTO countries VALUES ('BR', 'Brazil', 2)",
    "INSERT INTO countries VALUES ('KR', 'South Korea', 3)",
    "INSERT INTO countries VALUES ('JP', 'Japan', 3)",
    "INSERT INTO countries VALUES ('AU', 'Australia', 3)",
    "INSERT INTO countries VALUES ('ZA', 'South Africa', 4)",

    "INSERT INTO locations VALUES (100, '10 Downing Street', 'SW1A', 'London', 'England', 'GB')",
    "INSERT INTO locations VALUES (200, '500 Market Street', '94105', 'San Francisco', 'California', 'US')",
    "INSERT INTO locations VALUES (300, '100 King Street', 'M5X', 'Toronto', 'Ontario', 'CA')",
    "INSERT INTO locations VALUES (400, '21 Teheran-ro', '06130', 'Seoul', NULL, 'KR')",
    "INSERT INTO locations VALUES (500, '8 Marunouchi', '100-0005', 'Tokyo', 'Tokyo', 'JP')",
    "INSERT INTO locations VALUES (600, '12 Friedrichstrasse', '10117', 'Berlin', 'Berlin', 'DE')",
    "INSERT INTO locations VALUES (700, '5 Collins Street', '3000', 'Melbourne', 'Victoria', 'AU')",
    "INSERT INTO locations VALUES (800, '90 Paulista Avenue', '01310', 'Sao Paulo', 'SP', 'BR')",
    "INSERT INTO locations VALUES (900, '42 Rue de Rivoli', '75001', 'Paris', 'Ile-de-France', 'FR')",
    "INSERT INTO locations VALUES (1000, '1 Broadway', '10004', 'New York', 'New York', 'US')",
    "INSERT INTO locations VALUES (1100, '200 Bay Street', 'M5J', 'Vancouver', 'British Columbia', 'CA')",
    "INSERT INTO locations VALUES (1200, '7 Long Street', '8001', 'Cape Town', 'Western Cape', 'ZA')",

    "INSERT INTO jobs VALUES ('CEO', 'Chief Executive Officer', 20000, 30000)",
    "INSERT INTO jobs VALUES ('ENG_MGR', 'Engineering Manager', 14000, 22000)",
    "INSERT INTO jobs VALUES ('SW_ENG', 'Software Engineer', 5000, 16000)",
    "INSERT INTO jobs VALUES ('DATA_ANALYST', 'Data Analyst', 5000, 15000)",
    "INSERT INTO jobs VALUES ('SA_MGR', 'Sales Manager', 12000, 20000)",
    "INSERT INTO jobs VALUES ('SA_REP', 'Sales Representative', 4000, 14000)",
    "INSERT INTO jobs VALUES ('FI_MGR', 'Finance Manager', 12000, 19000)",
    "INSERT INTO jobs VALUES ('ACCOUNTANT', 'Accountant', 4500, 12000)",
    "INSERT INTO jobs VALUES ('HR_MGR', 'Human Resources Manager', 11000, 18000)",
    "INSERT INTO jobs VALUES ('HR_SPEC', 'Human Resources Specialist', 4000, 11000)",
    "INSERT INTO jobs VALUES ('MK_MGR', 'Marketing Manager', 11000, 18000)",
    "INSERT INTO jobs VALUES ('SUPPORT', 'Support Engineer', 4000, 13000)",
    "INSERT INTO jobs VALUES ('OPS_MGR', 'Operations Manager', 10000, 18000)",
    "INSERT INTO jobs VALUES ('ARCHIVED', 'Archived Legacy Role', 3000, 9000)",
    "INSERT INTO jobs VALUES ('INTERN', 'Intern', 2000, 3500)",

    "INSERT INTO departments VALUES (10, 'Executive', NULL, 100)",
    "INSERT INTO departments VALUES (20, 'Engineering', NULL, 700)",
    "INSERT INTO departments VALUES (30, 'Sales', NULL, 200)",
    "INSERT INTO departments VALUES (40, 'Finance', NULL, 500)",
    "INSERT INTO departments VALUES (50, 'Human Resources', NULL, 400)",
    "INSERT INTO departments VALUES (60, 'Marketing', NULL, 300)",
    "INSERT INTO departments VALUES (70, 'Customer Support', NULL, 800)",
    "INSERT INTO departments VALUES (80, 'Operations', NULL, 900)",
    "INSERT INTO departments VALUES (90, 'Research', NULL, 1000)",
    "INSERT INTO departments VALUES (100, 'Legal', NULL, 600)",
    "INSERT INTO departments VALUES (110, 'Innovation Lab', NULL, NULL)",
    "INSERT INTO departments VALUES (120, 'Field Operations', NULL, 1100)",

    "INSERT INTO employees VALUES (1, 'Elena', 'Rossi', 'ELENA.ROSSI', '+44-20-1001', '2010-01-15', 'CEO', 24000, NULL, NULL, 10)",
    "INSERT INTO employees VALUES (2, 'Marcus', 'Chen', 'MARCUS.CHEN', '+61-3-1002', '2012-03-12', 'ENG_MGR', 18000, NULL, 1, 20)",
    "INSERT INTO employees VALUES (3, 'Sophia', 'Smith', 'SOPHIA.SMITH', '+1-415-1003', '2013-05-20', 'SA_MGR', 17000, 0.10, 1, 30)",
    "INSERT INTO employees VALUES (4, 'Liam', 'Tanaka', 'LIAM.TANAKA', '+81-3-1004', '2014-02-10', 'FI_MGR', 16000, NULL, 1, 40)",
    "INSERT INTO employees VALUES (5, 'Aisha', 'Khan', 'AISHA.KHAN', '+82-2-1005', '2015-07-01', 'HR_MGR', 14500, NULL, 1, 50)",
    "INSERT INTO employees VALUES (6, 'Noah', 'Martin', 'NOAH.MARTIN', '+1-416-1006', '2016-04-18', 'MK_MGR', 14000, NULL, 1, 60)",
    "INSERT INTO employees VALUES (7, 'Hana', 'Park', 'HANA.PARK', '+55-11-1007', '2017-09-11', 'SUPPORT', 12000, NULL, 2, 70)",
    "INSERT INTO employees VALUES (8, 'Diego', 'Silva', 'DIEGO.SILVA', '+33-1-1008', '2015-11-23', 'OPS_MGR', 15000, NULL, 1, 80)",
    "INSERT INTO employees VALUES (9, 'Yuki', 'Sato', 'YUKI.SATO', '+1-212-1009', '2018-06-14', 'DATA_ANALYST', 13000, NULL, 2, 90)",
    "INSERT INTO employees VALUES (10, 'Olivia', 'Schmidt', 'OLIVIA.SCHMIDT', '+49-30-1010', '2017-01-30', 'HR_SPEC', 12500, NULL, 1, 100)",
    "INSERT INTO employees VALUES (11, 'Ethan', 'Brown', 'ETHAN.BROWN', '+61-3-1011', '2018-02-05', 'SW_ENG', 15000, NULL, 2, 20)",
    "INSERT INTO employees VALUES (12, 'Mia', 'Wilson', 'MIA.WILSON', '+61-3-1012', '2019-03-18', 'SW_ENG', 13000, NULL, 2, 20)",
    "INSERT INTO employees VALUES (13, 'Lucas', 'Davis', 'LUCAS.DAVIS', '+61-3-1013', '2020-01-06', 'SW_ENG', 10000, NULL, 2, 20)",
    "INSERT INTO employees VALUES (14, 'Emma', 'Clark', 'EMMA.CLARK', '+61-3-1014', '2020-07-20', 'SW_ENG', 8000, NULL, 2, 20)",
    "INSERT INTO employees VALUES (15, 'Mateo', 'Garcia', 'MATEO.GARCIA', '+61-3-1015', '2021-04-12', 'SW_ENG', 8000, NULL, 11, 20)",
    "INSERT INTO employees VALUES (16, 'Isla', 'Moore', 'ISLA.MOORE', '+61-3-1016', '2021-09-27', 'DATA_ANALYST', 9000, NULL, 9, 20)",
    "INSERT INTO employees VALUES (17, 'Leo', 'Anderson', 'LEO.ANDERSON', '+61-3-1017', '2022-02-14', 'SW_ENG', 5000, NULL, 11, 20)",
    "INSERT INTO employees VALUES (18, 'Grace', 'Thomas', 'GRACE.THOMAS', '+61-3-1018', '2023-05-01', 'DATA_ANALYST', 7000, NULL, 9, 20)",
    "INSERT INTO employees VALUES (19, 'Daniel', 'Jackson', 'DANIEL.JACKSON', '+61-3-1019', '2024-01-15', 'SW_ENG', 6000, NULL, 11, 20)",
    "INSERT INTO employees VALUES (20, 'Zoe', 'White', 'ZOE.WHITE', '+1-415-1020', '2017-08-08', 'SA_REP', 12000, 0.25, 3, 30)",
    "INSERT INTO employees VALUES (21, 'James', 'Harris', 'JAMES.HARRIS', '+1-415-1021', '2018-10-22', 'SA_REP', 11000, 0.20, 3, 30)",
    "INSERT INTO employees VALUES (22, 'Nora', 'Lewis', 'NORA.LEWIS', '+1-415-1022', '2019-01-14', 'SA_REP', 10000, 0.15, 3, 30)",
    "INSERT INTO employees VALUES (23, 'Henry', 'Walker', 'HENRY.WALKER', '+1-415-1023', '2020-03-09', 'SA_REP', 9000, 0.20, 3, 30)",
    "INSERT INTO employees VALUES (24, 'Layla', 'Hall', 'LAYLA.HALL', '+1-415-1024', '2020-11-16', 'SA_REP', 8000, 0.10, 20, 30)",
    "INSERT INTO employees VALUES (25, 'Jack', 'Allen', 'JACK.ALLEN', '+1-415-1025', '2021-06-21', 'SA_REP', 7000, NULL, 20, 30)",
    "INSERT INTO employees VALUES (26, 'Chloe', 'Young', 'CHLOE.YOUNG', '+1-415-1026', '2022-04-04', 'SA_REP', 6000, 0.10, 20, 30)",
    "INSERT INTO employees VALUES (27, 'Owen', 'King', 'OWEN.KING', '+1-415-1027', '2023-02-13', 'SA_REP', 5000, NULL, 21, 30)",
    "INSERT INTO employees VALUES (28, 'Lily', 'Wright', 'LILY.WRIGHT', '+1-415-1028', '2023-08-28', 'SA_REP', 5000, 0.05, 21, 30)",
    "INSERT INTO employees VALUES (29, 'Mason', 'Scott', 'MASON.SCOTT', '+1-415-1029', '2024-05-06', 'SA_REP', 4500, NULL, 21, 30)",
    "INSERT INTO employees VALUES (30, 'Ava', 'Green', 'AVA.GREEN', '+81-3-1030', '2018-03-12', 'ACCOUNTANT', 10000, NULL, 4, 40)",
    "INSERT INTO employees VALUES (31, 'Logan', 'Baker', 'LOGAN.BAKER', '+81-3-1031', '2019-07-29', 'ACCOUNTANT', 8000, NULL, 4, 40)",
    "INSERT INTO employees VALUES (32, 'Ella', 'Adams', 'ELLA.ADAMS', '+81-3-1032', '2021-01-11', 'ACCOUNTANT', 6000, NULL, 4, 40)",
    "INSERT INTO employees VALUES (33, 'Ben', 'Nelson', 'BEN.NELSON', '+81-3-1033', '2022-09-19', 'ACCOUNTANT', 5000, NULL, 30, 40)",
    "INSERT INTO employees VALUES (34, 'Amelia', 'Carter', 'AMELIA.CARTER', '+82-2-1034', '2019-02-25', 'HR_SPEC', 9000, NULL, 5, 50)",
    "INSERT INTO employees VALUES (35, 'Finn', 'Mitchell', 'FINN.MITCHELL', '+82-2-1035', '2021-05-17', 'HR_SPEC', 7000, NULL, 5, 50)",
    "INSERT INTO employees VALUES (36, 'Maya', 'Perez', 'MAYA.PEREZ', '+1-416-1036', '2018-12-03', 'DATA_ANALYST', 11000, NULL, 6, 60)",
    "INSERT INTO employees VALUES (37, 'Theo', 'Roberts', 'THEO.ROBERTS', '+1-416-1037', '2020-06-15', 'HR_SPEC', 7500, NULL, 6, 60)",
    "INSERT INTO employees VALUES (38, 'Ruby', 'Turner', 'RUBY.TURNER', '+55-11-1038', '2019-09-09', 'SUPPORT', 9000, NULL, 7, 70)",
    "INSERT INTO employees VALUES (39, 'Adam', 'Phillips', 'ADAM.PHILLIPS', '+55-11-1039', '2020-09-09', 'SUPPORT', 8000, NULL, 7, 70)",
    "INSERT INTO employees VALUES (40, 'Sara', 'Campbell', 'SARA.CAMPBELL', '+55-11-1040', '2022-01-24', 'SUPPORT', 6000, NULL, 38, 70)",
    "INSERT INTO employees VALUES (41, 'Kai', 'Parker', 'KAI.PARKER', '+33-1-1041', '2018-04-16', 'SUPPORT', 10000, NULL, 8, 80)",
    "INSERT INTO employees VALUES (42, 'Ivy', 'Evans', 'IVY.EVANS', '+33-1-1042', '2020-04-16', 'SUPPORT', 8000, NULL, 8, 80)",
    "INSERT INTO employees VALUES (43, 'Max', 'Edwards', 'MAX.EDWARDS', '+33-1-1043', '2021-10-04', 'SUPPORT', 6500, NULL, 41, 80)",
    "INSERT INTO employees VALUES (44, 'Luna', 'Collins', 'LUNA.COLLINS', '+1-212-1044', '2019-05-20', 'DATA_ANALYST', 12000, NULL, 9, 90)",
    "INSERT INTO employees VALUES (45, 'Sam', 'Stewart', 'SAM.STEWART', '+1-212-1045', '2020-05-20', 'DATA_ANALYST', 10000, NULL, 9, 90)",
    "INSERT INTO employees VALUES (46, 'Aria', 'Sanchez', 'ARIA.SANCHEZ', '+49-30-1046', '2019-11-11', 'HR_SPEC', 8500, NULL, 10, 100)",
    "INSERT INTO employees VALUES (47, 'Alex', 'Morris', 'ALEX.MORRIS', '+49-30-1047', '2022-11-11', 'HR_SPEC', 5500, NULL, 10, 100)",
    "INSERT INTO employees VALUES (48, 'Ravi', 'Singh', 'RAVI.SINGH', NULL, '2023-03-06', 'SW_ENG', 8000, NULL, 2, NULL)",
    "INSERT INTO employees VALUES (49, 'Nina', 'Cook', 'NINA.COOK', NULL, '2024-03-18', 'DATA_ANALYST', 6500, NULL, 9, NULL)",
    "INSERT INTO employees VALUES (50, 'Tom', 'Morgan', 'TOM.MORGAN', NULL, '2024-07-01', 'SA_REP', 3500, NULL, 3, NULL)",

    "INSERT INTO job_history VALUES (2, '2008-01-01', '2012-03-11', 'SW_ENG', 20)",
    "INSERT INTO job_history VALUES (3, '2009-06-01', '2013-05-19', 'SA_REP', 30)",
    "INSERT INTO job_history VALUES (4, '2010-02-01', '2014-02-09', 'ACCOUNTANT', 40)",
    "INSERT INTO job_history VALUES (5, '2011-07-01', '2015-06-30', 'HR_SPEC', 50)",
    "INSERT INTO job_history VALUES (6, '2012-04-01', '2016-04-17', 'HR_SPEC', 60)",
    "INSERT INTO job_history VALUES (7, '2013-09-01', '2015-08-31', 'ARCHIVED', 80)",
    "INSERT INTO job_history VALUES (7, '2015-09-01', '2017-09-10', 'SUPPORT', 80)",
    "INSERT INTO job_history VALUES (8, '2011-11-01', '2015-11-22', 'SUPPORT', 70)",
    "INSERT INTO job_history VALUES (9, '2014-06-01', '2018-06-13', 'SW_ENG', 20)",
    "INSERT INTO job_history VALUES (10, '2013-01-01', '2017-01-29', 'ARCHIVED', 100)",
    "INSERT INTO job_history VALUES (11, '2015-02-01', '2018-02-04', 'ARCHIVED', 20)",
    "INSERT INTO job_history VALUES (12, '2016-03-01', '2019-03-17', 'SW_ENG', 20)",
    "INSERT INTO job_history VALUES (20, '2014-08-01', '2017-08-07', 'SA_REP', 30)",
    "INSERT INTO job_history VALUES (21, '2015-10-01', '2018-10-21', 'ARCHIVED', 30)",
    "INSERT INTO job_history VALUES (22, '2016-01-01', '2019-01-13', 'SA_REP', 30)",
    "INSERT INTO job_history VALUES (23, '2017-03-01', '2020-03-08', 'SA_REP', 30)",
    "INSERT INTO job_history VALUES (30, '2014-03-01', '2016-02-29', 'ARCHIVED', 40)",
    "INSERT INTO job_history VALUES (30, '2016-03-01', '2018-03-11', 'ACCOUNTANT', 40)",
    "INSERT INTO job_history VALUES (31, '2016-07-01', '2019-07-28', 'ACCOUNTANT', 40)",
    "INSERT INTO job_history VALUES (34, '2016-02-01', '2019-02-24', 'ARCHIVED', 50)",
    "INSERT INTO job_history VALUES (36, '2015-12-01', '2018-12-02', 'HR_SPEC', 60)",
    "INSERT INTO job_history VALUES (38, '2016-09-01', '2019-09-08', 'ARCHIVED', 70)",
    "INSERT INTO job_history VALUES (41, '2015-04-01', '2018-04-15', 'ARCHIVED', 80)",
    "INSERT INTO job_history VALUES (44, '2016-05-01', '2019-05-19', 'SW_ENG', 90)",
    "INSERT INTO job_history VALUES (46, '2017-11-01', '2019-11-10', 'ARCHIVED', 100)",
    "INSERT INTO job_history VALUES (47, '2019-11-12', '2022-11-10', 'HR_SPEC', 100)",
    "INSERT INTO job_history VALUES (48, '2020-03-01', '2023-03-05', 'ARCHIVED', NULL)",
    "INSERT INTO job_history VALUES (49, '2021-03-01', '2024-03-17', 'ARCHIVED', 90)",
    "INSERT INTO job_history VALUES (50, '2021-07-01', '2024-06-30', 'SA_REP', 30)",
    "INSERT INTO job_history VALUES (18, '2020-05-01', '2023-04-30', 'ARCHIVED', 90)",
)

private val departmentManagerStatements = listOf(
    "UPDATE departments SET manager_id = 1 WHERE department_id = 10",
    "UPDATE departments SET manager_id = 2 WHERE department_id = 20",
    "UPDATE departments SET manager_id = 3 WHERE department_id = 30",
    "UPDATE departments SET manager_id = 4 WHERE department_id = 40",
    "UPDATE departments SET manager_id = 5 WHERE department_id = 50",
    "UPDATE departments SET manager_id = 6 WHERE department_id = 60",
    "UPDATE departments SET manager_id = 7 WHERE department_id = 70",
    "UPDATE departments SET manager_id = 8 WHERE department_id = 80",
    "UPDATE departments SET manager_id = 9 WHERE department_id = 90",
    "UPDATE departments SET manager_id = 10 WHERE department_id = 100",
)
