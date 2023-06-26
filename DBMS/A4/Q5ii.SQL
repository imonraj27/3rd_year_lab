DECLARE
  v_member_id MEMBER.MEMBER_ID%TYPE := :member_id;
  v_book_id BOOK.BOOK_ID%TYPE := :book_id;
  v_serial_no BOOK.SERIAL_NO%TYPE := :serial_no;
BEGIN
  -- Check if the book copy was issued to this member
  IF NOT EXISTS (
    SELECT 1 FROM TRANSACTION 
    WHERE MEMBER_ID = v_member_id AND BOOK_ID = v_book_id AND SERIAL_NO = v_serial_no AND TRANSACTION_TYPE = 'issue'
  ) THEN
    RAISE_APPLICATION_ERROR(-20001, 'This book copy was not issued to this member.');
  END IF;

  -- Update the transaction record for the book return
  UPDATE TRANSACTION SET 
    TRANSACTION_TYPE = 'return',
    TRANSACTION_DATE = SYSDATE,
    TO_BE_RETURNED = NULL
  WHERE MEMBER_ID = v_member_id AND BOOK_ID = v_book_id AND SERIAL_NO = v_serial_no AND TRANSACTION_TYPE = 'issue';

  -- Update the book status to 'available'
  UPDATE BOOK SET STATUS = 'available' WHERE BOOK_ID = v_book_id AND SERIAL_NO = v_serial_no;

  -- Print success message
  DBMS_OUTPUT.PUT_LINE('Book returned successfully.');
EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE('Error: ' || SQLCODE || ' - ' || SQLERRM);
END;
