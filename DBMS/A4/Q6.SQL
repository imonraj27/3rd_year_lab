DECLARE
  v_month DATE := SYSDATE; -- current month
BEGIN
  FOR emp IN (SELECT emp_code, basic FROM EMP098) LOOP
    -- Get the total number of days in the current month
    DECLARE
      v_last_day_of_month DATE := LAST_DAY(v_month);
      v_num_of_days_in_month PLS_INTEGER := EXTRACT(DAY FROM v_last_day_of_month);
    BEGIN
      NULL;
    END;

    -- Get the total number of leave days taken by the employee in the current month
    DECLARE
      v_num_of_leave_days PLS_INTEGER;
    BEGIN
      SELECT SUM(no_of_days)
      INTO v_num_of_leave_days
      FROM LEAVE
      WHERE emp_no = EMP098.emp_code
        AND MONTH = v_month;
    END;

    -- Calculate the effective basic salary for the employee for the current month
    DECLARE
      v_effective_basic_salary NUMBER := emp.basic_salary - (emp.basic_salary * v_num_of_leave_days / v_num_of_days_in_month);
    BEGIN
      -- Print the result for the employee
      DBMS_OUTPUT.PUT_LINE('Emp No: ' || emp.emp_no || ', Effective Basic Salary: ' || v_effective_basic_salary);
    END;
  END LOOP;
END;
