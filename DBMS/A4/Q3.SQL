DECLARE
  TYPE emp_info IS RECORD (name EMP098.emp_name%TYPE, salary EMP098.basic%TYPE);
  TYPE emp_info_list IS TABLE OF emp_info INDEX BY PLS_INTEGER;
  v_emp_list emp_info_list;
BEGIN
  -- Populate the employee list with the top 5 employees by salary
  SELECT emp_name, basic BULK COLLECT INTO v_emp_list
  FROM (SELECT emp_name, basic FROM EMP098 ORDER BY basic DESC)
  WHERE ROWNUM <= 5;

  -- Display the names of the top 5 employees
  DBMS_OUTPUT.PUT_LINE('Top 5 Employees by Salary:');
  FOR i IN 1..v_emp_list.COUNT LOOP
    DBMS_OUTPUT.PUT_LINE(v_emp_list(i).name || ' - ' || v_emp_list(i).salary);
  END LOOP;
EXCEPTION
  WHEN OTHERS THEN
    -- Display an error message for any exceptions
    DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END;
