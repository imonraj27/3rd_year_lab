DECLARE
    v_start_date DATE := '&enter_start_date'; -- User inputs start date
    v_end_date DATE := '&enter_end_date'; -- User inputs end date
    v_pending_delv_cnt NUMBER := 0; -- Counter for pending deliveries
BEGIN
    FOR order_rec IN (SELECT DISTINCT ORDER_NO FROM ORDERMAST WHERE ORDER_DT BETWEEN v_start_date AND v_end_date)
    LOOP
        DBMS_OUTPUT.PUT_LINE('Order No: ' || order_rec.ORDER_NO);
        FOR item_rec IN (SELECT ITEM_NO, SUM(QTY) AS order_qty FROM ORDERDETAILS WHERE ORDER_NO = order_rec.ORDER_NO GROUP BY ITEM_NO)
        LOOP
            -- Calculate total quantity of items delivered for this order and item combination
            SELECT NVL(SUM(QTY), 0) INTO v_delivered_qty FROM DELIVERY_DETAILS WHERE ORDER_NO = order_rec.ORDER_NO AND ITEM_NO = item_rec.ITEM_NO;
            
            -- If total delivered quantity is less than ordered quantity, this item is pending
            IF v_delivered_qty < item_rec.order_qty THEN
                -- Retrieve information about the pending delivery
                SELECT * INTO v_delv_mast_rec FROM DELIVERYMAST WHERE ORDER_NO = order_rec.ORDER_NO AND ITEM_NO = item_rec.ITEM_NO;
                v_pending_delv_cnt := v_pending_delv_cnt + 1;
                
                -- Print the details of the pending delivery
                DBMS_OUTPUT.PUT_LINE('  Item No: ' || item_rec.ITEM_NO || ', Ordered Qty: ' || item_rec.order_qty || ', Delivered Qty: ' || v_delivered_qty || ', Pending Qty: ' || (item_rec.order_qty - v_delivered_qty));
                DBMS_OUTPUT.PUT_LINE('    Delivery No: ' || v_delv_mast_rec.DELV_NO || ', Delivery Date: ' || v_delv_mast_rec.DELV_DT || ', Pending Qty: ' || (item_rec.order_qty - v_delivered_qty));
            END IF;
        END LOOP;
    END LOOP;
    
    -- Print the total number of pending deliveries
    IF v_pending_delv_cnt = 0 THEN
        DBMS_OUTPUT.PUT_LINE('No pending deliveries found.');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Total pending deliveries: ' || v_pending_delv_cnt);
    END IF;
END;
