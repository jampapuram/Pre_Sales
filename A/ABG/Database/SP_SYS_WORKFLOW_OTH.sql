--------------------------------------------------------
--  File created - Friday-June-14-2013   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Procedure SP_SYS_WORKFLOW_OTH
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "HEIS_SU"."SP_SYS_WORKFLOW_OTH" AS

--  v_Process_Flag varchar2(1);
--    v_Cr_dt timestamp;
  v_tmp_ctr Number(8);
  v_ctr Number(8);
  
v_systimestamp TIMESTAMP;
v_WORKFLOWID      varchar2(50);
v_SEQNO           Number(8);
v_FROM_EMAIL_ADDRESS VARCHAR2(2000);
v_REQUEST_CODE    NUMBER(8);

v_cur0_SOURCE_SYS_ID NUMBER(8);
v_cur0_WORKFLOWSTAGEID VARCHAR2(50);
v_cur0_WORKFLOWID VARCHAR2(50);
v_cur0_SEQNO NUMBER(8);
v_cur0_WORKFLOW_TYPE VARCHAR2(100);
v_cur0_APPLSTAGE VARCHAR2(50);
v_cur0_stage_desc VARCHAR2(50); 
v_cur0_timer_in_mins NUMBER(8);
v_cur0_cr_dt TIMESTAMP;
  
   v_CUM_TAT_SECONDS NUMBER(8);
   v_CUM_TAT_MINS NUMBER(8);
   v_CUM_TAT_HRS NUMBER(8);        


/* First cursor */
  CURSOR C0
  is 
   SELECT b.WORKFLOWID, b.SEQNO, 
          b.FROM_EMAIL_ADDRESS1, b.REQUEST_CODE,
          b.SOURCE_SYS_ID, b.WORKFLOWSTAGEID, b.WORKFLOWID, 
          b.SEQNO, b.WORKFLOW_TYPE, b.APPLSTAGE, b.stage_desc,
          b.Timer_in_mins, b.cr_dt
      FROM temp_system_txn2 b
      WHERE B.PROCESS_FLAG = 'N'
      -- AND  ( b.cr_dt + INTERVAL '10' minute) <= systimestamp
      ORDER BY b.Request_code, b.SEQNO; 

  
-- We are picking up record which were created 5 mins or before.

  v_stage_start_dt TIMESTAMP;
  v_stage_end_dt TIMESTAMP;
  v_APPLICATION_NO VARCHAR2(50);
  v_EMAIL_TO_BE_SENT_TO VARCHAR2(4000);

  v_future_dt TIMESTAMP ;
  
BEGIN
v_tmp_ctr := 0;
   Open c0;
   loop
      fetch c0 into 
      v_WORKFLOWID, v_SEQNO ,v_FROM_EMAIL_ADDRESS, v_REQUEST_CODE,
      v_cur0_SOURCE_SYS_ID, v_cur0_WORKFLOWSTAGEID , v_cur0_WORKFLOWID,
       v_cur0_SEQNO , v_cur0_WORKFLOW_TYPE , v_cur0_APPLSTAGE,
        v_cur0_stage_desc, v_cur0_timer_in_mins, v_cur0_cr_dt; 
	EXIT WHEN c0%NOTFOUND;
      
  SELECT
  (v_cur0_cr_dt +  v_cur0_timer_in_mins*(1/24/60)) 
  INTO v_future_dt
  FROM dual;

   IF sysdate >= v_future_dt THEN
   -- DO THIS only if the time to do this has arrived!
  UPDATE temp_system_txn2
  SET Process_Flag = 'W'
  WHERE 
  REQUEST_CODE = v_REQUEST_CODE 
  AND WORKFLOWID = v_WORKFLOWID      
  AND SEQNO      = v_SEQNO; 
  COMMIT;
   
   
-- stage_start_date for current (new) stage will be stage_end_date of prev stage
  SELECT STAGE_END_DT, APPLICATION_NO, systimestamp 
  INTO   v_stage_start_dt, v_APPLICATION_NO , v_stage_end_dt
  FROM   HI_APPLICATION_WF_OTH
  WHERE ORG_ID = 9000 -- hardcoded
  AND SOURCE_WK_CODE = v_REQUEST_CODE
  AND SEQNO = (SELECT MAX(SEQNO) FROM HI_APPLICATION_WF_OTH
                 WHERE ORG_ID = 9000 -- hardcoded
                  AND SEQNO < v_cur0_SEQNO
                  AND SOURCE_WK_CODE = v_REQUEST_CODE
                  );

-- Insert into OTH

  INSERT INTO HI_APPLICATION_WF_OTH
   (	ORG_ID,
	SOURCE_SYS_ID,
	APPLICATION_NO,
	SOURCE_WK_CODE,
	APPLICATION_STAGE,
	WORKFLOWSTAGEID,
	WORKFLOWID,
	SEQNO,
	STAGE_START_DT,
	STAGE_END_DT,
	STAGE_STATUS,
	STATUS_BUSINESS_DT,
	SYS_DECISION_CODE,
	FINAL_DECISION_CODE,
	DECISION_OWNER_CODE,
  CR_ID,
	CR_DT,
	ACTIVE,
	ACTIVATION_DT
	--DEACTIVATION_DT
	)
	VALUES
	(      9000, -- Hardcoded for now
         v_cur0_SOURCE_SYS_ID,
         v_APPLICATION_NO,
         v_request_code,
         v_cur0_applstage,
         v_cur0_WORKFLOWSTAGEID, 
	       v_cur0_WORKFLOWID,
         v_cur0_SEQNO,
         v_stage_start_dt,
         v_stage_end_dt,
         'C', -- pending or Complete
         sysdate, -- business date
	       'A',
         'APPROVED IN SYSTEM',
         'E003',
         'SYSTEM',
         sysdate, -- cr_dt
	       'Y' , -- hardcoded
	       sysdate);
	 

-- Update OTH for mins, hours, seconds--- days added by ritesh as a big gap produces wrong calculation

    UPDATE HI_APPLICATION_WF_OTH
    set 
    ACTUAL_TAT_HRS = ROUND((extract(day from STAGE_END_DT)  - extract(day from STAGE_START_DT))*60*60*24 +
    (extract(hour from STAGE_END_DT)  - extract(hour from STAGE_START_DT))*60*60 +
    ((extract(minute from STAGE_END_DT)- extract(minute from STAGE_START_DT))*60 +
      (extract(second from STAGE_END_DT)- extract(second from STAGE_START_DT))),3)/(60*60),
   ACTUAL_TAT_MINS = ROUND((extract(day from STAGE_END_DT)  - extract(day from STAGE_START_DT))*60*60*24 +
   (extract(hour from STAGE_END_DT)  - extract(hour from STAGE_START_DT))*60*60 +
   ((extract(minute from STAGE_END_DT)- extract(minute from STAGE_START_DT))*60 +
    (extract(second from STAGE_END_DT)- extract(second from STAGE_START_DT))),2)/60 ,
    ACTUAL_TAT_SECONDS = ROUND((extract(day from STAGE_END_DT)  - extract(day from STAGE_START_DT))*60*60*24 +
    (extract(hour from STAGE_END_DT)  - extract(hour from STAGE_START_DT))*60*60 +
    (extract(minute from STAGE_END_DT)- extract(minute from STAGE_START_DT))*60 +
    (extract(second FROM STAGE_END_DT)- extract(second FROM STAGE_START_DT)),2) 
    WHERE ORG_ID = 9000 -- hardcoded
    AND APPLICATION_NO = V_APPLICATION_NO
    AND SOURCE_WK_CODE = v_REQUEST_CODE
    AND SEQNO = v_cur0_SEQNO;

    SELECT  SUM(nvl(ACTUAL_TAT_SECONDS,0)),
            SUM(nvl(ACTUAL_TAT_MINS,0)),
            SUM(nvl(ACTUAL_TAT_HRS,0))
    INTO v_CUM_TAT_SECONDS, v_CUM_TAT_MINS, v_CUM_TAT_HRS        
    FROM    HI_APPLICATION_WF_OTH
    WHERE ORG_ID = 9000 -- hardcoded
    AND APPLICATION_NO = V_APPLICATION_NO
    AND SOURCE_WK_CODE = v_REQUEST_CODE --:new.email_id --CHANGED BY rITESH ON 01-06-2013
    AND SEQNO <= v_cur0_SEQNO;
    
    UPDATE HI_APPLICATION_WF_OTH
    SET CUM_TAT_SECONDS = v_CUM_TAT_SECONDS, 
        CUM_TAT_MINS = v_CUM_TAT_MINS, 
        CUM_TAT_HRS = v_CUM_TAT_HRS
         WHERE ORG_ID = 9000 -- hardcoded
         AND APPLICATION_NO = V_APPLICATION_NO
          AND SOURCE_WK_CODE = v_REQUEST_CODE --:new.email_id --CHANGED BY rITESH ON 01-06-2013
          AND SEQNO = v_cur0_SEQNO;




SELECT EMP_EMAIL_ID1 into v_EMAIL_TO_BE_SENT_TO
FROM UM_EMPLOYEE
WHERE employee_id = 'E003';

INSERT INTO TEMP_RESPONSE_FOR_SYSREQUEST
(ORG_ID,
MSG_SUBJECT,
MSG_CONTENT,
MSG_ENDOFMAIL,
MSG_TO_BE_SENT_TO,
PROCESS_FLAG)
VALUES
(9000, -- Hardcoded for now
'System Generated Record for Request No : '|| to_char(v_request_code),
'We are happy to inform you that system has made an auto-entry for  '|| v_cur0_stage_desc
||'. You may proceed to next stage. Thank you for choosing Hexhibit!',
'Regards',
v_EMAIL_TO_BE_SENT_TO||','||v_FROM_EMAIL_ADDRESS,
'N');
COMMIT;


UPDATE temp_system_txn2
SET Process_Flag = 'Y'
WHERE 
REQUEST_CODE = v_REQUEST_CODE 
AND WORKFLOWID = v_WORKFLOWID      
AND SEQNO      = v_SEQNO; 
COMMIT;

END IF;

   END LOOP; -- C0 end loop
      close c0;
END;

/
