package io.github.socratone.sqlexercise.ui

internal val hrSchemaReference = """
    regions(region_id PK, region_name)

    countries(country_id PK, country_name, region_id FK)

    locations(location_id PK, street_address, postal_code,
      city, state_province, country_id FK)

    departments(department_id PK, department_name,
      manager_id FK, location_id FK)

    jobs(job_id PK, job_title, min_salary, max_salary)

    employees(employee_id PK, first_name, last_name,
      email UNIQUE, phone_number, hire_date, job_id FK,
      salary, commission_pct, manager_id FK, department_id FK)

    job_history(employee_id PK/FK, start_date PK,
      end_date, job_id FK, department_id FK)

    employees.manager_id → employees.employee_id
    departments.manager_id → employees.employee_id
""".trimIndent()
