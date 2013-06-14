--------------------------------------------------------
--  File created - Friday-June-14-2013   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Procedure SP_SYSTEM_WORKFLOW_OTH
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "HEIS_SU"."SP_SYSTEM_WORKFLOW_OTH" AS

--  v_Process_Flag varchar2(1);
--    v_Cr_dt timestamp;
  v_tmp_ctr Number(8);
  v_ctr Number(8);
  
v_systimestamp TIMESTAMP;
v_WORKFLOWID      varchar2(50);
v_SEQNO           Number(8);
v_FROM_EMAIL_ADDRESS VARCHAR2(2000);
v_REQUEST_CODE    NUMBER(8);

v_cur1_SOURCE_SYS_ID NUMBER(8);
v_cur1_WORKFLOWSTAGEID VARCHAR2(50);
v_cur1_WORKFLOWID VARCHAR2(50);
v_cur1_SEQNO NUMBER(8);
v_cur1_WORKFLOW_TYPE VARCHAR2(100);
v_cur1_APPLSTAGE VARCHAR2(50);
v_cur1_stage_desc VARCHAR2(50); 

   v_CUM_TAT_SECONDS NUMBER(8);
   v_CUM_TAT_MINS NUMBER(8);
   v_CUM_TAT_HRS NUMBER(8);        
     
/* First cursor */
  CURSOR C0
  is 
   SELECT b.WORKFLOWID, b.SEQNO, 
          b.FROM_EMAIL_ADDRESS1, b.REQUEST_CODE
      FROM temp_system_txn b
      WHERE B.PROCESS_FLAG = 'N'
      and  ( B.CR_DT + interval '10' minute) <= systimestamp
      ORDER BY  b.Request_code, b.SEQNO; 

  
  CURSOR C1
   is
     SELECT a.SOURCE_SYS_ID, a.WORKFLOWSTAGEID, a.WORKFLOWID, a.SEQNO, a.WORKFLOW_TYPE,
     a.APPLSTAGE, substr(a.stage_desc1,1,50) stage_desc 
      FROM UM_WORKFLOW_STAGES a 
      WHERE a.ORG_ID = 9000 -- hardcoded 
      AND   a.WORKFLOWID = v_WORKFLOWID
      AND   a.SEQNO > v_seqno
      AND   a.ACTIVE = 'Y'  -- In db we have made "REJECTION IN SYSTEM" as Inactive
      AND   a.module_desc1 like 'SIMULATED%'
      ORDER BY a.SEQNO; 

-- We are picking up record which were created 5 mins or before.

  v_stage_start_dt TIMESTAMP;
  v_stage_end_dt TIMESTAMP;
  v_APPLICATION_NO VARCHAR2(50);
  v_EMAIL_TO_BE_SENT_TO VARCHAR2(4000);


      

  
BEGIN
v_tmp_ctr := 0;
   Open c0;
   loop
      fetch c0 into 
      v_WORKFLOWID, v_SEQNO ,v_FROM_EMAIL_ADDRESS, v_REQUEST_CODE; 
	EXIT WHEN c0%NOTFOUND;
      
      v_ctr := 0;

      v_tmp_ctr := v_tmp_ctr + 1;	            
       
	Open c1;
       loop
       fetch c1 into 
       v_cur1_SOURCE_SYS_ID, v_cur1_WORKFLOWSTAGEID , v_cur1_WORKFLOWID,
       v_cur1_SEQNO , v_cur1_WORKFLOW_TYPE , v_cur1_APPLSTAGE,
        v_cur1_stage_desc; 
       EXIT WHEN c1%NOTFOUND;
      

   
 -- dbms_lock.sleep(180);-- setting wait for 300 seconds -- PENDING WAIT FOR 5 MINS

-- stage_start_date for current (new) stage will be stage_end_date of prev stage
  SELECT STAGE_END_DT, APPLICATION_NO, ( STAGE_END_DT + INTERVAL '5' minute) 
  INTO   v_stage_start_dt, v_APPLICATION_NO , v_stage_end_dt
  FROM   HI_APPLICATION_WF_OTH
  WHERE ORG_ID = 9000 -- hardcoded
  AND SOURCE_WK_CODE = v_REQUEST_CODE
  AND SEQNO = (SELECT MAX(SEQNO) FROM HI_APPLICATION_WF_OTH
                 WHERE ORG_ID = 9000 -- hardcoded
                  AND SEQNO < v_cur1_SEQNO
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
         v_cur1_SOURCE_SYS_ID,
         v_APPLICATION_NO,
         v_request_code,
         v_cur1_applstage,
         v_cur1_WORKFLOWSTAGEID, 
	       v_cur1_WORKFLOWID,
         v_cur1_SEQNO,
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
    AND SEQNO = v_cur1_SEQNO;

SELECT  SUM(nvl(ACTUAL_TAT_SECONDS,0)),
            SUM(nvl(ACTUAL_TAT_MINS,0)),
            SUM(nvl(ACTUAL_TAT_HRS,0))
    INTO v_CUM_TAT_SECONDS, v_CUM_TAT_MINS, v_CUM_TAT_HRS        
    FROM    HI_APPLICATION_WF_OTH
    WHERE ORG_ID = 9000 -- hardcoded
    AND APPLICATION_NO = V_APPLICATION_NO
    AND SOURCE_WK_CODE = v_REQUEST_CODE --:new.email_id --CHANGED BY rITESH ON 01-06-2013
    AND SEQNO <= v_cur1_SEQNO;
    
    UPDATE HI_APPLICATION_WF_OTH
    SET CUM_TAT_SECONDS = v_CUM_TAT_SECONDS, 
        CUM_TAT_MINS = v_CUM_TAT_MINS, 
        CUM_TAT_HRS = v_CUM_TAT_HRS
         WHERE ORG_ID = 9000 -- hardcoded
         AND APPLICATION_NO = V_APPLICATION_NO
          AND SOURCE_WK_CODE = v_REQUEST_CODE --:new.email_id --CHANGED BY rITESH ON 01-06-2013
          AND SEQNO = v_cur1_SEQNO;



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
'We are happy to inform you that system has made an auto-entry for  '|| v_cur1_stage_desc
||'. You may proceed to next stage. Thank you for choosing Hexhibit!',
'Regards',
v_EMAIL_TO_BE_SENT_TO||','||v_FROM_EMAIL_ADDRESS,
'N');
COMMIT;


IF v_ctr = 0 THEN

  -- SELECT systimestamp into v_systimestamp FROM DUAL;

 -- LOOP

   -- EXIT WHEN (v_SYSTIMESTAMP + INTERVAL '05' minute) <= SYSTIMESTAMP;
  -- END LOOP;
 
  
   -- DBMS_LOCK.sleep(120);
 
   v_ctr := 1;  
   ELSE
     v_ctr := v_ctr +1;

END IF;
UPDATE temp_system_txn
SET Process_Flag = 'Y'
WHERE 
REQUEST_CODE = v_REQUEST_CODE 
AND WORKFLOWID = v_WORKFLOWID      
AND SEQNO      = v_SEQNO; 
COMMIT;


   END LOOP; -- C1 end loop

close c1;

/*
UPDATE temp_system_txn
SET Process_Flag = 'Y'
WHERE 
REQUEST_CODE = v_REQUEST_CODE 
AND WORKFLOWID = v_WORKFLOWID      
AND SEQNO      = v_SEQNO; 
COMMIT;
*/

COMMIT;
   END LOOP; -- C0 end loop
      close c0;
END;

/
