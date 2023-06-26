CREATE OR REPLACE TRIGGER request_delete_trigger
AFTER DELETE ON REQUEST
FOR EACH ROW
ENABLE
DECLARE
  last_entry_no NUMBER;
BEGIN
  SELECT MAX(ENTRY_NO) INTO last_entry_no FROM SERVICE_LOG;
  IF last_entry_no IS NULL THEN
    last_entry_no := 1;
  ELSE
    last_entry_no := last_entry_no + 1;
  END IF;
  INSERT INTO SERVICE_LOG (ENTRY_NO, REQ_NO, SERVICE_DT)
    VALUES (last_entry_no, :OLD.REQ_NO, SYSDATE);
END;
/
