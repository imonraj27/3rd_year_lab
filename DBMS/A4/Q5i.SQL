DECLARE
  v_member_type MEMBER.MEMBER_TYPE%TYPE;
  v_max_books_count MEMBER.MAX_BOOKS_COUNT%TYPE;
  v_current_books_count NUMBER;
  v_book_status BOOK.STATUS%TYPE;
BEGIN
  -- Check if member exists and get member details
  SELECT MEMBER_TYPE, MAX_BOOKS_COUNT INTO v_member_type, v_max_books_count 
  FROM MEMBER WHERE MEMBER_ID = :member_id;

  -- Check if member is eligible to issue more books
  SELECT COUNT(*) INTO v_current_books_count 
  FROM TRANSACTION WHERE MEMBER_ID = :member_id AND TRANSACTION_TYPE = 'issue';
  IF v_current_books_count >= v_max_books_count THEN
    RAISE_APPLICATION_ERROR(-20001, 'Member has already issued maximum allowed books.');
  END IF;

  -- Check if book is available and get book details
  SELECT STATUS INTO v_book_status FROM BOOK 
  WHERE BOOK_ID = :book_id AND SERIAL_NO = :serial_no;
  IF v_book_status != 'available' THEN
    RAISE_APPLICATION_ERROR(-20002, 'Book is not available for issue.');
  END IF;

  -- Update book status and create transaction record
  UPDATE BOOK SET STATUS = 'issued', LAST_SERIAL = :serial_no 
  WHERE BOOK_ID = :book_id AND SERIAL_NO = :serial_no;
  INSERT INTO TRANSACTION (TRANSACTION_ID, TRANSACTION_DATE, TRANSACTION_TYPE, 
    TO_BE_RETURNED, BOOK_ID, SERIAL_NO, MEMBER_ID) 
  VALUES ('TRN'||TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), SYSDATE, 'issue', 
    SYSDATE + 7, :book_id, :serial_no, :member_id);
  COMMIT;
  
  DBMS_OUTPUT.PUT_LINE('Book issued successfully.');
EXCEPTION
  WHEN NO_DATA_FOUND THEN
    RAISE_APPLICATION_ERROR(-20003, 'Member or book not found.');
  WHEN OTHERS THEN
    RAISE_APPLICATION_ERROR(-20004, 'Error in issuing book.');
END;
