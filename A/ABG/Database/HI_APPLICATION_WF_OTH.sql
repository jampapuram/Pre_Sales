--------------------------------------------------------
--  File created - Sunday-June-02-2013   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table HI_APPLICATION_WF_OTH
--------------------------------------------------------

  CREATE TABLE "HEIS_SU"."HI_APPLICATION_WF_OTH" 
   (	"ORG_ID" NUMBER(8,0), 
	"SOURCE_SYS_ID" NUMBER(8,0), 
	"APPLICATION_NO" VARCHAR2(50 BYTE), 
	"SOURCE_WK_CODE" NUMBER(8,0), 
	"SEQNO" NUMBER(8,0), 
	"APPLICATION_STAGE" VARCHAR2(50 BYTE), 
	"WORKFLOWSTAGEID" VARCHAR2(50 BYTE), 
	"WORKFLOWID" VARCHAR2(50 BYTE), 
	"STAGE_START_DT" TIMESTAMP (6), 
	"STAGE_END_DT" TIMESTAMP (6), 
	"STAGE_STATUS" VARCHAR2(50 BYTE), 
	"STATUS_BUSINESS_DT" TIMESTAMP (6), 
	"SYS_DECISION_CODE" VARCHAR2(50 BYTE), 
	"FINAL_DECISION_CODE" VARCHAR2(50 BYTE), 
	"DECISION_OWNER_CODE" VARCHAR2(50 BYTE), 
	"REASON_FOR_DECISION" VARCHAR2(500 BYTE), 
	"FF_N1" NUMBER(16,2), 
	"FF_N2" NUMBER(16,2), 
	"FF_N3" NUMBER(16,2), 
	"FF_N4" NUMBER(16,2), 
	"FF_N5" NUMBER(16,2), 
	"FF_VC1" VARCHAR2(500 BYTE), 
	"FF_VC2" VARCHAR2(500 BYTE), 
	"FF_VC3" VARCHAR2(500 BYTE), 
	"FF_VC4" VARCHAR2(500 BYTE), 
	"FF_VC5" VARCHAR2(500 BYTE), 
	"FF_D1" DATE, 
	"FF_D2" DATE, 
	"FF_D3" DATE, 
	"FF_D4" DATE, 
	"FF_D5" DATE, 
	"CR_ID" VARCHAR2(50 BYTE), 
	"CR_DT" TIMESTAMP (6), 
	"UPD_ID" VARCHAR2(50 BYTE), 
	"UPD_DT" TIMESTAMP (6), 
	"ACTIVE" VARCHAR2(2 BYTE) DEFAULT 'Y', 
	"ACTIVATION_DT" TIMESTAMP (6), 
	"DEACTIVATION_DT" TIMESTAMP (6) DEFAULT '01-JAN-2099 12:00:00 AM'
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 65536 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "HDM_TBS" ;
REM INSERTING into HEIS_SU.HI_APPLICATION_WF_OTH

--------------------------------------------------------
--  Constraints for Table HI_APPLICATION_WF_OTH
--------------------------------------------------------

  ALTER TABLE "HEIS_SU"."HI_APPLICATION_WF_OTH" ADD CONSTRAINT "HAWO_CHK_ACTIVE" CHECK (ACTIVE IN ('Y','N')) ENABLE;
 
  ALTER TABLE "HEIS_SU"."HI_APPLICATION_WF_OTH" MODIFY ("ORG_ID" NOT NULL ENABLE);
 
  ALTER TABLE "HEIS_SU"."HI_APPLICATION_WF_OTH" MODIFY ("SOURCE_SYS_ID" NOT NULL ENABLE);
 
  ALTER TABLE "HEIS_SU"."HI_APPLICATION_WF_OTH" MODIFY ("APPLICATION_NO" NOT NULL ENABLE);
 
  ALTER TABLE "HEIS_SU"."HI_APPLICATION_WF_OTH" MODIFY ("SOURCE_WK_CODE" NOT NULL ENABLE);
 
  ALTER TABLE "HEIS_SU"."HI_APPLICATION_WF_OTH" MODIFY ("SEQNO" NOT NULL ENABLE);
 
  ALTER TABLE "HEIS_SU"."HI_APPLICATION_WF_OTH" MODIFY ("WORKFLOWSTAGEID" NOT NULL ENABLE);
 
  ALTER TABLE "HEIS_SU"."HI_APPLICATION_WF_OTH" MODIFY ("CR_ID" NOT NULL ENABLE);
 
  ALTER TABLE "HEIS_SU"."HI_APPLICATION_WF_OTH" MODIFY ("CR_DT" NOT NULL ENABLE);
 
  ALTER TABLE "HEIS_SU"."HI_APPLICATION_WF_OTH" MODIFY ("ACTIVE" NOT NULL ENABLE);
 
  ALTER TABLE "HEIS_SU"."HI_APPLICATION_WF_OTH" MODIFY ("ACTIVATION_DT" NOT NULL ENABLE);
 
  ALTER TABLE "HEIS_SU"."HI_APPLICATION_WF_OTH" MODIFY ("DEACTIVATION_DT" NOT NULL ENABLE);
