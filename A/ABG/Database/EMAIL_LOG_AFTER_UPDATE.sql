--------------------------------------------------------
--  File created - Friday-June-14-2013   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Trigger EMAIL_LOG_AFTER_UPDATE
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "HEIS_SU"."EMAIL_LOG_AFTER_UPDATE" 
AFTER UPDATE
   OF APPLSTAGE
   ON EMAIL_LOG
   FOR EACH ROW
   
DECLARE
   v_APPLICATION_NO VARCHAR2(50);
   v_WORKFLOWSTAGEID VARCHAR2(50); 
   v_WORKFLOWID VARCHAR2(50);    
   v_seqno NUMBER(8);
   v_WORKFLOW_TYPE VARCHAR2(4000);
   v_STAGE_STATUS VARCHAR2(50);
   v_STAGE_START_DT date;
   v_STAGE_END_DT date;
   v_First_Last_Ind VARCHAR2(50);
   v_SYS_DECISION_CODE VARCHAR2(50);
   v_FINAL_DECISION_CODE VARCHAR2(50);
   V_DECISION_OWNER_CODE VARCHAR2(50);
   V_SOURCE_WK_CODE NUMBER(8); --VARCHAR2(50); --MODIFIED by Ritesh on 05-Jun-13
   v_REQUEST_CODE NUMBER(8); 
   v_SOURCE_SYS_ID NUMBER(8);
   v_TMP_REQUEST_COUNT NUMBER(8); 
   V_AUTOMATED_ENTRY_COUNT number(8); 
   v_FROM_EMAIL_ADDRESS varchar2(50); ---added by ritesh for approval mail to orignator
   v_CUM_TAT_SECONDS NUMBER(8);
   v_CUM_TAT_MINS NUMBER(8);
   v_CUM_TAT_HRS NUMBER(8);        
   
BEGIN

SELECT SOURCE_SYS_ID, WORKFLOWSTAGEID, WORKFLOWID, SEQNO, WORKFLOW_TYPE, NVL(First_Last_Ind,'x'), SUBSTR(STAGE_DESC1,1,50), 
SUBSTR(STAGE_DESC1,1,50)  
INTO v_SOURCE_SYS_ID, v_WORKFLOWSTAGEID, v_WORKFLOWID, v_seqno, v_WORKFLOW_TYPE, v_First_Last_Ind, 
v_SYS_DECISION_CODE,v_FINAL_DECISION_CODE  
FROM UM_WORKFLOW_STAGES
WHERE APPLSTAGE = :new.applstage
AND ORG_ID = 9000; -- Hardcoded for now

SELECT nvl2(:new.request_code,:new.request_code,:new.email_id) 
INTO v_SOURCE_WK_CODE 
FROM DUAL;

IF V_FIRST_LAST_IND = 'F' THEN 
-- No need to do this as Ram is doing it
-- select SEQ_REQUEST_CODE.nextval into v_REQUEST_CODE from dual;

   --v_SOURCE_WK_CODE := :new.email_id;
   SELECT sysdate into v_stage_start_dt FROM DUAL;
   SELECT APPLICATION_ID INTO v_APPLICATION_NO 
   FROM EMAIL_ATTACHMENT_INFO
   WHERE EMAIL_ID = :new.email_id;

-- ELSE

--   SELECT nvl2(:new.request_code,:new.request_code,:new.email_id) 
  -- INTO v_SOURCE_WK_CODE 
  -- FROM DUAL;
  --SELECT APPLICATION_ID INTO v_APPLICATION_NO 
  --FROM HI_REQUEST_TXN 
  --WHERE REQUEST_CODE = v_SOURCE_WK_CODE;

END IF;

	v_STAGE_STATUS := 'C';
  SELECT sysdate into v_STAGE_END_DT FROM DUAL;

IF v_First_Last_Ind <> 'F' THEN 
-- stage_start_date for current (new) stage will be stage_end_date of prev stage
  SELECT STAGE_END_DT, APPLICATION_NO 
  INTO   v_stage_start_dt, v_APPLICATION_NO 
  FROM   HI_APPLICATION_WF_OTH
  WHERE ORG_ID = 9000 -- hardcoded
  AND SOURCE_WK_CODE = v_SOURCE_WK_CODE --:new.email_id --CHANGED BY rITESH ON 01-06-2013
  AND SEQNO = (SELECT MAX(SEQNO) FROM HI_APPLICATION_WF_OTH
                 WHERE ORG_ID = 9000 -- hardcoded
                  AND SEQNO < V_SEQNO
                  AND SOURCE_WK_CODE = v_SOURCE_WK_CODE -- :new.email_id --CHANGED BY rITESH ON 01-06-2013
                  );
END IF;

begin
v_FROM_EMAIL_ADDRESS := :new.FROM_EMAIL_ADDRESS; ---added by ritesh for approval mail to orignator
   SELECT EMPLOYEE_ID 
   INTO v_DECISION_OWNER_CODE 
   FROM UM_EMPLOYEE
   WHERE EMP_EMAIL_ID1 = :new.FROM_EMAIL_ADDRESS OR EMP_EMAIL_ID2 = :new.FROM_EMAIL_ADDRESS;
   EXCEPTION WHEN NO_DATA_FOUND THEN v_DECISION_OWNER_CODE := 'E001'; --hardcoded with sad heart
             WHEN TOO_MANY_ROWS THEN v_DECISION_OWNER_CODE := 'E001'; --hardcoded with sad heart
END;

   -- Insert record into HI_APPLICATION_WF_OTH table
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
	(9000, -- Hardcoded for now
	 v_SOURCE_SYS_ID,
	 V_APPLICATION_NO,
         v_SOURCE_WK_CODE,
         :new.applstage,
         v_WORKFLOWSTAGEID, 
	       v_WORKFLOWID,
         V_SEQNO,
         v_stage_start_dt,
         v_stage_end_dt,
         v_STAGE_STATUS, -- pending or Complete
         sysdate, -- business date
	       v_SYS_DECISION_CODE ,
         v_FINAL_DECISION_CODE ,
         v_DECISION_OWNER_CODE ,
         v_DECISION_OWNER_CODE ,
         sysdate, -- cr_dt
	       'Y' , -- hardcoded
	       sysdate)
	 ;
-- Update OTH for mins, hours, seconds--- days added by ritesh as a big gap produces wrong calculation
    UPDATE HI_APPLICATION_WF_OTH
    SET 
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
    AND SOURCE_WK_CODE = v_SOURCE_WK_CODE --:new.email_id --CHANGED BY rITESH ON 01-06-2013
    AND SEQNO = V_SEQNO;
/* END IF; */

    SELECT  SUM(nvl(ACTUAL_TAT_SECONDS,0)),
            SUM(nvl(ACTUAL_TAT_MINS,0)),
            SUM(nvl(ACTUAL_TAT_HRS,0))
    INTO v_CUM_TAT_SECONDS, v_CUM_TAT_MINS, v_CUM_TAT_HRS        
    FROM    HI_APPLICATION_WF_OTH
    WHERE ORG_ID = 9000 -- hardcoded
    AND APPLICATION_NO = V_APPLICATION_NO
    AND SOURCE_WK_CODE = v_SOURCE_WK_CODE --:new.email_id --CHANGED BY rITESH ON 01-06-2013
    AND SEQNO <= V_SEQNO;
    
    UPDATE HI_APPLICATION_WF_OTH
    SET CUM_TAT_SECONDS = v_CUM_TAT_SECONDS, 
        CUM_TAT_MINS = v_CUM_TAT_MINS, 
        CUM_TAT_HRS = v_CUM_TAT_HRS
         WHERE ORG_ID = 9000 -- hardcoded
         AND APPLICATION_NO = V_APPLICATION_NO
          AND SOURCE_WK_CODE = v_SOURCE_WK_CODE --:new.email_id --CHANGED BY rITESH ON 01-06-2013
          AND SEQNO = V_SEQNO;



-- Simulating system data entry of Request Submit
/*IF V_FIRST_LAST_IND = 'F' THEN 
  select SEQ_REQUEST_CODE.nextval into v_REQUEST_CODE from dual;
  INSERT INTO TEMP_REQUEST_TXN
  (
    REQUEST_CODE,
    CLIENT_CODE,
    CLIENT_NAME,
    APPLICATION_ID,
    AMOUNT,
    TYPE_OF_REQUEST,
    MAKER_ID,
    EMAIL_TO_BE_SENT_TO,
    CR_DT,
    CR_BY,
    ACTIVE,
    PROCESS_FLAG,
    EMAIL_ID,
    INS_UPD_FLAG
  )
 ( SELECT v_REQUEST_CODE,
    CLIENT_CODE,
    CLIENT_NAME,
    APPLICATION_ID,
    AMOUNT,
    TYPE_OF_REQUEST,
    v_DECISION_OWNER_CODE,
    :new.FROM_EMAIL_ADDRESS,
    sysdate,
    'SYSTEM',
    'Y' ,
    'H', -- On hold till simulated entry is received via email
    :new.email_id,
    'I'
    FROM EMAIL_ATTACHMENT_INFO
    WHERE EMAIL_ID=:new.email_id
  ) ;

  
  
END IF;
*/


-- Checking if next stage is supposed to be sys generated
--sp_ins_temp_sys_txn(v_SOURCE_WK_CODE, v_WORKFLOW_TYPE, v_WORKFLOWID, v_SEQNO, v_FROM_EMAIL_ADDRESS);

SELECT count(*) into    v_AUTOMATED_ENTRY_COUNT 
FROM UM_WORKFLOW_STAGES a 
WHERE a.ORG_ID = 9000 -- hardcoded
AND   a.WORKFLOWID = v_WORKFLOWID
AND   a.SEQNO = 
      (SELECT MIN(b.SEQNO) FROM UM_WORKFLOW_STAGES b
                  WHERE b.ORG_ID = 9000 -- hardcoded
                  AND b.WORKFLOWID = a.WORKFLOWID 
                  AND b.SEQNO > v_seqno
                  AND b.ACTIVE = 'Y')
AND   a.WORKFLOW_TYPE = v_WORKFLOW_TYPE
AND   a.ACTIVE = 'Y'
AND   a.module_desc1 like 'SIMULATED%'
; 

IF v_AUTOMATED_ENTRY_COUNT > 0 THEN
-- Means next stage is system generated. That means we have to Make as many entries as sys entries into OTH table 
-- call the procedure
-- Calling the procedure from here locks the table. Hence will do as a scheduled process
  --sp_sys_workflow_oth(v_SOURCE_WK_CODE,v_WORKFLOW_TYPE, v_WORKFLOWID, v_SEQNO, v_FROM_EMAIL_ADDRESS);

    Insert into temp_system_txn
    (REQUEST_CODE,
    WORKFLOWID,
    SEQNO,
    FROM_EMAIL_ADDRESS1,
    PRocess_Flag,
    Cr_dt)
    Values
    (v_SOURCE_WK_CODE,v_WORKFLOWID, v_SEQNO, v_FROM_EMAIL_ADDRESS, 'N', sysdate);


END IF;

END;
/
ALTER TRIGGER "HEIS_SU"."EMAIL_LOG_AFTER_UPDATE" ENABLE;
