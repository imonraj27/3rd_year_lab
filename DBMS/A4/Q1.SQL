DECLARE
  v_emp_code EMP098.emp_code%TYPE;
  v_emp_name EMP098.emp_name%TYPE;
BEGIN
  -- Accept the employee code from the user
  v_emp_code := &employee_code;

  -- Retrieve the name of the employee from the EMP table
  SELECT name INTO v_emp_name FROM EMP WHERE code = v_emp_code;

  -- Display the name of the employee
  DBMS_OUTPUT.PUT_LINE('Employee Name: ' || v_emp_name);
EXCEPTION
  WHEN NO_DATA_FOUND THEN
    -- Indicate that the employee does not exist
    DBMS_OUTPUT.PUT_LINE('Employee with code ' || v_emp_code || ' does not exist.');
END;
