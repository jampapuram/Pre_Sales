--------------------------------------------------------
--  File created - Sunday-June-02-2013   
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
   v_STAGE_STATUS VARCHAR2(50);
   v_STAGE_END_DT date;
   v_First_Last_Ind VARCHAR2(50);
   v_SYS_DECISION_CODE VARCHAR2(50);
   v_FINAL_DECISION_CODE VARCHAR2(50);
   V_DECISION_OWNER_CODE VARCHAR2(50);
   v_SOURCE_WK_CODE VARCHAR2(50);
   
BEGIN

SELECT WORKFLOWSTAGEID, WORKFLOWID, SEQNO, NVL(First_Last_Ind,'x'), SUBSTR(STAGE_DESC1,1,50), SUBSTR(STAGE_DESC1,1,50)  
INTO v_WORKFLOWSTAGEID, v_WORKFLOWID, v_seqno, v_First_Last_Ind, v_SYS_DECISION_CODE,v_FINAL_DECISION_CODE  
FROM UM_WORKFLOW_STAGES
WHERE APPLSTAGE = :new.applstage
AND ORG_ID = 9000; -- Hardcoded for now

IF V_FIRST_LAST_IND = 'F' THEN 
v_SOURCE_WK_CODE := :new.email_id;
   SELECT APPLICATION_ID INTO v_APPLICATION_NO 
   FROM EMAIL_ATTACHMENT_INFO
   WHERE EMAIL_ID = :new.email_id;
ELSE
v_SOURCE_WK_CODE := :new.request_code;
  SELECT APPLICATION_ID INTO v_APPLICATION_NO 
  FROM HI_REQUEST_TXN 
  WHERE REQUEST_CODE = :new.request_code;
END IF;

IF v_First_Last_Ind = 'L' THEN 
	v_STAGE_STATUS := 'C';
  SELECT sysdate into v_STAGE_END_DT FROM DUAL;
ELSE
	v_STAGE_STATUS := 'P';
	v_STAGE_END_DT := '';
END IF;


SELECT EMPLOYEE_ID 
INTO v_DECISION_OWNER_CODE 
FROM UM_EMPLOYEE
WHERE EMP_EMAIL_ID1 = :new.FROM_EMAIL_ADDRESS OR EMP_EMAIL_ID2 = :new.FROM_EMAIL_ADDRESS;

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
	-- DEACTIVATION_DT
	)
	VALUES
	(9000, -- Hardcoded for now
	 5100, -- Hardcoded for now
	 V_APPLICATION_NO,
         v_SOURCE_WK_CODE,--:new.email_id, --Changed frm EMAIL_ID RO REQUEST_CODE added by Ritesh to check if this code can help in next stage processing(01-06-2013). 
	       :new.applstage,
         v_WORKFLOWSTAGEID, 
	       v_WORKFLOWID,
         V_SEQNO,
         sysdate, -- to_date(:new.SUCCESS_FLAG_UPD_DT,'dd/mm/yyyy HH24:MI'), Temp modified by Ritesh on 02-06-2013, for TAT calculation.
         v_stage_end_dt,
         v_STAGE_STATUS, -- pending or Complete
         sysdate,
	       v_SYS_DECISION_CODE ,
         v_FINAL_DECISION_CODE ,
         v_DECISION_OWNER_CODE ,
         v_DECISION_OWNER_CODE ,
	       sysdate,
	       'Y', -- hardcoded
	       sysdate)
	-- to_date('31-dec-2099','dd-mon-yyyy'))
	 ;
-- Update details for previous stage's 
IF v_First_Last_Ind <> 'F' THEN 
    UPDATE HI_APPLICATION_WF_OTH
    SET STAGE_END_DT = sysdate,
        STAGE_STATUS = 'C',
	      STATUS_BUSINESS_DT = sysdate
    WHERE ORG_ID = 9000 -- hardcoded
    AND APPLICATION_NO = V_APPLICATION_NO
    AND SOURCE_WK_CODE = v_SOURCE_WK_CODE --:new.email_id --CHANGED BY rITESH ON 01-06-2013
    AND SEQNO = (SELECT MAX(SEQNO) FROM HI_APPLICATION_WF_OTH
                 WHERE ORG_ID = 9000 -- hardcoded
                  AND APPLICATION_NO = v_APPLICATION_NO
                  AND SEQNO < V_SEQNO
                  AND SOURCE_WK_CODE = v_SOURCE_WK_CODE -- :new.email_id --CHANGED BY rITESH ON 01-06-2013
                  );
END IF;

END;
/
ALTER TRIGGER "HEIS_SU"."EMAIL_LOG_AFTER_UPDATE" ENABLE;
