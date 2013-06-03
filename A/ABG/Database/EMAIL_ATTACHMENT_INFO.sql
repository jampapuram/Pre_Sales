--------------------------------------------------------
--  File created - Sunday-June-02-2013   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table EMAIL_ATTACHMENT_INFO
--------------------------------------------------------

  CREATE TABLE "HEIS_SU"."EMAIL_ATTACHMENT_INFO" 
   (	"ATTACHMENT_ID" NUMBER(8,0), 
	"EMAIL_ID" NUMBER(8,0), 
	"CLIENT_CODE" VARCHAR2(50 BYTE), 
	"CLIENT_NAME" VARCHAR2(4000 BYTE), 
	"APPLICATION_ID" VARCHAR2(50 BYTE), 
	"AMOUNT" NUMBER(16,2), 
	"DATE_OF_BIRTH" DATE, 
	"GENDER" VARCHAR2(50 BYTE), 
	"GROUP_NAME" VARCHAR2(50 BYTE), 
	"TAX_STATUS" VARCHAR2(50 BYTE), 
	"BUSINESS_UNIT" VARCHAR2(50 BYTE), 
	"DATE_OF_ACTIVATION" DATE, 
	"CONTACT_PERSON_NAME" VARCHAR2(4000 BYTE), 
	"ADDRESS1" VARCHAR2(4000 BYTE), 
	"ADDRESS2" VARCHAR2(4000 BYTE), 
	"ADDRESS3" VARCHAR2(4000 BYTE), 
	"CITY" VARCHAR2(4000 BYTE), 
	"STATE" VARCHAR2(50 BYTE), 
	"ZIP" VARCHAR2(4000 BYTE), 
	"FAX" VARCHAR2(50 BYTE), 
	"TELEPHONE" VARCHAR2(50 BYTE), 
	"MOBILE_NO" VARCHAR2(50 BYTE), 
	"EMAIL" VARCHAR2(4000 BYTE), 
	"INTRODUCED_BY" VARCHAR2(4000 BYTE), 
	"TDS_APPLICABILITY" VARCHAR2(4000 BYTE), 
	"PAN_NO" VARCHAR2(4000 BYTE), 
	"BRANCH" VARCHAR2(4000 BYTE), 
	"RM_NAME" VARCHAR2(4000 BYTE), 
	"INTEREST_RATE" VARCHAR2(4000 BYTE), 
	"TYPE_OF_REQUEST" VARCHAR2(4000 BYTE), 
	"CR_DT" DATE, 
	"CR_BY" VARCHAR2(4000 BYTE), 
	"ACTIVE" VARCHAR2(1 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 65536 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "HDM_TBS" ;
REM INSERTING into HEIS_SU.EMAIL_ATTACHMENT_INFO

--------------------------------------------------------
--  DDL for Index EAI_PK_ATTACHMENT_ID
--------------------------------------------------------

  CREATE UNIQUE INDEX "HEIS_SU"."EAI_PK_ATTACHMENT_ID" ON "HEIS_SU"."EMAIL_ATTACHMENT_INFO" ("ATTACHMENT_ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 65536 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "HDM_TBS" ;
--------------------------------------------------------
--  Constraints for Table EMAIL_ATTACHMENT_INFO
--------------------------------------------------------

  ALTER TABLE "HEIS_SU"."EMAIL_ATTACHMENT_INFO" ADD CONSTRAINT "EAI_CHK_ACTIVE" CHECK (ACTIVE IN ('Y','N')) ENABLE;
 
  ALTER TABLE "HEIS_SU"."EMAIL_ATTACHMENT_INFO" ADD CONSTRAINT "EAI_CHK_TYPE_OF_REQUEST" CHECK (TYPE_OF_REQUEST in ('D','P','R')) ENABLE;
 
  ALTER TABLE "HEIS_SU"."EMAIL_ATTACHMENT_INFO" ADD CONSTRAINT "EAI_PK_ATTACHMENT_ID" PRIMARY KEY ("ATTACHMENT_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 65536 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "HDM_TBS"  ENABLE;
 
  ALTER TABLE "HEIS_SU"."EMAIL_ATTACHMENT_INFO" MODIFY ("ATTACHMENT_ID" NOT NULL ENABLE);
 
  ALTER TABLE "HEIS_SU"."EMAIL_ATTACHMENT_INFO" MODIFY ("EMAIL_ID" NOT NULL ENABLE);
 
  ALTER TABLE "HEIS_SU"."EMAIL_ATTACHMENT_INFO" MODIFY ("CR_DT" NOT NULL ENABLE);
 
  ALTER TABLE "HEIS_SU"."EMAIL_ATTACHMENT_INFO" MODIFY ("CR_BY" NOT NULL ENABLE);
 
  ALTER TABLE "HEIS_SU"."EMAIL_ATTACHMENT_INFO" MODIFY ("ACTIVE" NOT NULL ENABLE);
--------------------------------------------------------
--  Ref Constraints for Table EMAIL_ATTACHMENT_INFO
--------------------------------------------------------

  ALTER TABLE "HEIS_SU"."EMAIL_ATTACHMENT_INFO" ADD CONSTRAINT "EAI_FK_EMAIL_ID" FOREIGN KEY ("EMAIL_ID")
	  REFERENCES "HEIS_SU"."EMAIL_LOG" ("EMAIL_ID") ENABLE;