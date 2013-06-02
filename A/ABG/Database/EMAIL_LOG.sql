--------------------------------------------------------
--  File created - Sunday-June-02-2013   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table EMAIL_LOG
--------------------------------------------------------

  CREATE TABLE "HEIS_SU"."EMAIL_LOG" 
   (	"EMAIL_ID" NUMBER(8,0), 
	"EMAIL_TYPE" VARCHAR2(4000 BYTE), 
	"FROM_EMAIL_ADDRESS" VARCHAR2(4000 BYTE), 
	"CC_EMAIL_ADDRESS" VARCHAR2(4000 BYTE), 
	"TO_EMAIL_ADDRESS" VARCHAR2(4000 BYTE), 
	"SUBJECT" VARCHAR2(4000 BYTE), 
	"ATTACHMENT_FLAG" VARCHAR2(1 BYTE), 
	"ATTACHMENT_NAME" VARCHAR2(4000 BYTE), 
	"PROCESS_FLAG" VARCHAR2(1 BYTE), 
	"SUCCESS_FLAG" VARCHAR2(1 BYTE), 
	"ACTIVE" VARCHAR2(1 BYTE), 
	"ORG_ID" NUMBER(8,0), 
	"APPLSTAGE" VARCHAR2(50 BYTE), 
	"REQUEST_CODE" VARCHAR2(50 BYTE), 
	"EMAIL_RESPONSE_FLAG" VARCHAR2(1 BYTE), 
	"SUCCESS_FLAG_UPD_DT" DATE, 
	"CR_DT" DATE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 65536 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "HDM_TBS" ;
 

   COMMENT ON COLUMN "HEIS_SU"."EMAIL_LOG"."EMAIL_ID" IS 'Contains the unique ID generated for each entry';
 
   COMMENT ON COLUMN "HEIS_SU"."EMAIL_LOG"."FROM_EMAIL_ADDRESS" IS 'Contains the email address of the sender';
 
   COMMENT ON COLUMN "HEIS_SU"."EMAIL_LOG"."CC_EMAIL_ADDRESS" IS 'The mail may be copied to multiple people. The multiple cc mail addresses will be stored separated by comma.';
 
   COMMENT ON COLUMN "HEIS_SU"."EMAIL_LOG"."TO_EMAIL_ADDRESS" IS 'The mail may be sent to multiple people. The multiple TO mail addresses will be stored separated by comma.';
 
   COMMENT ON COLUMN "HEIS_SU"."EMAIL_LOG"."SUBJECT" IS 'Contains the subject of the email';
 
   COMMENT ON COLUMN "HEIS_SU"."EMAIL_LOG"."ATTACHMENT_FLAG" IS 'Contains the attachment flag whether the email contains the attachment Y or No N';
 
   COMMENT ON COLUMN "HEIS_SU"."EMAIL_LOG"."ATTACHMENT_NAME" IS 'The mail may have multiple Attachments. The multiple attachment file names will be stored separated by comma.';
 
   COMMENT ON COLUMN "HEIS_SU"."EMAIL_LOG"."PROCESS_FLAG" IS 'Process_Flag will contain Y/N. By default when a new record is inserted in this table, this flag will be set to "N". Once the record is processed, the same is set to "Y" irrespective of whether the record was successfully processed or not.';
 
   COMMENT ON COLUMN "HEIS_SU"."EMAIL_LOG"."SUCCESS_FLAG" IS 'Success_flag will have followin values :S indicates Successful processing,F indicates failed process, e.g. attachment could not be read,D indicates Duplicate mail and so to be ignored.';
 
   COMMENT ON COLUMN "HEIS_SU"."EMAIL_LOG"."ACTIVE" IS 'Active flag denotes whether the transaction is Active Y or INACTIVE N';
 
   COMMENT ON COLUMN "HEIS_SU"."EMAIL_LOG"."ORG_ID" IS 'Contains the org_id ';
 
   COMMENT ON COLUMN "HEIS_SU"."EMAIL_LOG"."APPLSTAGE" IS ' Application Stage Of the Email ';
 
   COMMENT ON COLUMN "HEIS_SU"."EMAIL_LOG"."EMAIL_RESPONSE_FLAG" IS 'To check the Email Response Flag';
REM INSERTING into HEIS_SU.EMAIL_LOG
--------------------------------------------------------
--  DDL for Index EL_PK_EMAIL_ID
--------------------------------------------------------

  CREATE UNIQUE INDEX "HEIS_SU"."EL_PK_EMAIL_ID" ON "HEIS_SU"."EMAIL_LOG" ("EMAIL_ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 65536 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "HDM_TBS" ;
--------------------------------------------------------
--  Constraints for Table EMAIL_LOG
--------------------------------------------------------

  ALTER TABLE "HEIS_SU"."EMAIL_LOG" ADD CONSTRAINT "EL_CHK_ACTIVE" CHECK (ACTIVE IN ('Y','N')) ENABLE;
 
  ALTER TABLE "HEIS_SU"."EMAIL_LOG" ADD CONSTRAINT "EL_CHK_ATTACHMENT_FLAG" CHECK (ATTACHMENT_FLAG IN ('Y','N')) ENABLE;
 
  ALTER TABLE "HEIS_SU"."EMAIL_LOG" ADD CONSTRAINT "EL_CHK_PROCESS_FLAG" CHECK (PROCESS_FLAG IN ('Y','N')) ENABLE;
 
  ALTER TABLE "HEIS_SU"."EMAIL_LOG" ADD CONSTRAINT "EL_CHK_SUCESS_FLAG" CHECK ("SUCCESS_FLAG"='S' OR "SUCCESS_FLAG"='F' OR "SUCCESS_FLAG"='D' OR "SUCCESS_FLAG"='W') ENABLE;
 
  ALTER TABLE "HEIS_SU"."EMAIL_LOG" ADD CONSTRAINT "EL_PK_EMAIL_ID" PRIMARY KEY ("EMAIL_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 65536 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "HDM_TBS"  ENABLE;
 
  ALTER TABLE "HEIS_SU"."EMAIL_LOG" MODIFY ("EMAIL_ID" NOT NULL ENABLE);
 
  ALTER TABLE "HEIS_SU"."EMAIL_LOG" MODIFY ("EMAIL_TYPE" NOT NULL ENABLE);
 
  ALTER TABLE "HEIS_SU"."EMAIL_LOG" MODIFY ("FROM_EMAIL_ADDRESS" NOT NULL ENABLE);
 
  ALTER TABLE "HEIS_SU"."EMAIL_LOG" MODIFY ("TO_EMAIL_ADDRESS" NOT NULL ENABLE);
 
  ALTER TABLE "HEIS_SU"."EMAIL_LOG" MODIFY ("ATTACHMENT_FLAG" NOT NULL ENABLE);
 
  ALTER TABLE "HEIS_SU"."EMAIL_LOG" MODIFY ("PROCESS_FLAG" NOT NULL ENABLE);
 
  ALTER TABLE "HEIS_SU"."EMAIL_LOG" MODIFY ("ACTIVE" NOT NULL ENABLE);
 
  ALTER TABLE "HEIS_SU"."EMAIL_LOG" MODIFY ("ORG_ID" NOT NULL ENABLE);

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
