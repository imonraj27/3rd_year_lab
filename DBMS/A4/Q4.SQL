DECLARE
  v_dept_code DEPT098.dept_code%TYPE;
  v_rows_deleted PLS_INTEGER := 0;
BEGIN
  -- Accept the department code from the user
  v_dept_code := &department_code;

  -- Delete all employee rows with the given department code
  DELETE FROM EMP WHERE dept_code = v_dept_code;

  -- Get the number of rows that were deleted
  v_rows_deleted := SQL%ROWCOUNT;

  -- Display the number of rows that were deleted
  DBMS_OUTPUT.PUT_LINE(v_rows_deleted || ' rows deleted.');
EXCEPTION
  WHEN OTHERS THEN
    -- Display an error message for any exceptions
    DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END;
