DECLARE
  v_emp_code EMP098.emp_code%TYPE;
  v_emp_name EMP098.emp_name%TYPE;
  v_dept_code EMP098.dept_code%TYPE;
BEGIN
  -- Accept the input values from the user
  v_emp_code := &employee_code;
  v_emp_name := '&employee_name';
  v_dept_code := &dept_code;

  -- Check if the employee code is already present in the EMP table
  SELECT COUNT(*) INTO v_emp_count FROM EMP WHERE code = v_emp_code;

  IF v_emp_count > 0 THEN
    -- Display an error message if the employee code is already present
    DBMS_OUTPUT.PUT_LINE('Employee code ' || v_emp_code || ' already exists.');
  ELSE
    -- Check if the department code is present in the DEPT table
    SELECT COUNT(*) INTO v_dept_count FROM DEPT098 WHERE code = v_dept_code;

    IF v_dept_count = 0 THEN
      -- Display an error message if the department code is not present
      DBMS_OUTPUT.PUT_LINE('Department code ' || v_dept_code || ' does not exist.');
    ELSE
      -- Insert the row into the EMP table if everything is valid
      INSERT INTO EMP (code, name, dept_code) VALUES (v_emp_code, v_emp_name, v_dept_code);
      DBMS_OUTPUT.PUT_LINE('Employee record added successfully.');
    END IF;
  END IF;
EXCEPTION
  WHEN OTHERS THEN
    -- Display an error message for any other exceptions
    DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END;
