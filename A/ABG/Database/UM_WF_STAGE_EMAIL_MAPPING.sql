--------------------------------------------------------
--  File created - Sunday-June-02-2013   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table UM_WF_STAGE_EMAIL_MAPPING
--------------------------------------------------------

  CREATE TABLE "HEIS_SU"."UM_WF_STAGE_EMAIL_MAPPING" 
   (	"WSEM_ID" NUMBER(8,0), 
	"WS_ID" NUMBER(8,0), 
	"ORG_ID" NUMBER(8,0), 
	"TYPE_OF_REQUEST" VARCHAR2(4000 BYTE), 
	"EMAIL_SUBJECT" VARCHAR2(4000 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 65536 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "HDM_TBS" ;
REM INSERTING into HEIS_SU.UM_WF_STAGE_EMAIL_MAPPING
SET DEFINE OFF;
Insert into HEIS_SU.UM_WF_STAGE_EMAIL_MAPPING (WSEM_ID,WS_ID,ORG_ID,TYPE_OF_REQUEST,EMAIL_SUBJECT) values (1,1,9000,'D',null);
Insert into HEIS_SU.UM_WF_STAGE_EMAIL_MAPPING (WSEM_ID,WS_ID,ORG_ID,TYPE_OF_REQUEST,EMAIL_SUBJECT) values (2,3,9000,'D','Approval Request');
Insert into HEIS_SU.UM_WF_STAGE_EMAIL_MAPPING (WSEM_ID,WS_ID,ORG_ID,TYPE_OF_REQUEST,EMAIL_SUBJECT) values (3,4,9000,'D','Approved');
Insert into HEIS_SU.UM_WF_STAGE_EMAIL_MAPPING (WSEM_ID,WS_ID,ORG_ID,TYPE_OF_REQUEST,EMAIL_SUBJECT) values (4,6,9000,'D','Handover to Accounts');
Insert into HEIS_SU.UM_WF_STAGE_EMAIL_MAPPING (WSEM_ID,WS_ID,ORG_ID,TYPE_OF_REQUEST,EMAIL_SUBJECT) values (5,7,9000,'P',null);
Insert into HEIS_SU.UM_WF_STAGE_EMAIL_MAPPING (WSEM_ID,WS_ID,ORG_ID,TYPE_OF_REQUEST,EMAIL_SUBJECT) values (6,9,9000,'P','Approval Request');
Insert into HEIS_SU.UM_WF_STAGE_EMAIL_MAPPING (WSEM_ID,WS_ID,ORG_ID,TYPE_OF_REQUEST,EMAIL_SUBJECT) values (7,10,9000,'P','Approved');
Insert into HEIS_SU.UM_WF_STAGE_EMAIL_MAPPING (WSEM_ID,WS_ID,ORG_ID,TYPE_OF_REQUEST,EMAIL_SUBJECT) values (8,12,9000,'P','Handover to DP');
Insert into HEIS_SU.UM_WF_STAGE_EMAIL_MAPPING (WSEM_ID,WS_ID,ORG_ID,TYPE_OF_REQUEST,EMAIL_SUBJECT) values (11,13,9000,'D','Rejected');
Insert into HEIS_SU.UM_WF_STAGE_EMAIL_MAPPING (WSEM_ID,WS_ID,ORG_ID,TYPE_OF_REQUEST,EMAIL_SUBJECT) values (12,15,9000,'P','Rejected');
--------------------------------------------------------
--  DDL for Index UWSEM_PK_WSEM_ID
--------------------------------------------------------

  CREATE UNIQUE INDEX "HEIS_SU"."UWSEM_PK_WSEM_ID" ON "HEIS_SU"."UM_WF_STAGE_EMAIL_MAPPING" ("WSEM_ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 65536 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "HDM_TBS" ;
--------------------------------------------------------
--  Constraints for Table UM_WF_STAGE_EMAIL_MAPPING
--------------------------------------------------------

  ALTER TABLE "HEIS_SU"."UM_WF_STAGE_EMAIL_MAPPING" MODIFY ("WSEM_ID" NOT NULL ENABLE);
 
  ALTER TABLE "HEIS_SU"."UM_WF_STAGE_EMAIL_MAPPING" MODIFY ("WS_ID" NOT NULL ENABLE);
 
  ALTER TABLE "HEIS_SU"."UM_WF_STAGE_EMAIL_MAPPING" MODIFY ("ORG_ID" NOT NULL ENABLE);
 
  ALTER TABLE "HEIS_SU"."UM_WF_STAGE_EMAIL_MAPPING" MODIFY ("TYPE_OF_REQUEST" NOT NULL ENABLE);
 
  ALTER TABLE "HEIS_SU"."UM_WF_STAGE_EMAIL_MAPPING" ADD CONSTRAINT "UWSEM_PK_WSEM_ID" PRIMARY KEY ("WSEM_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 65536 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "HDM_TBS"  ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table UM_WF_STAGE_EMAIL_MAPPING
--------------------------------------------------------

  ALTER TABLE "HEIS_SU"."UM_WF_STAGE_EMAIL_MAPPING" ADD CONSTRAINT "UWSEM_FK_WS_ID" FOREIGN KEY ("WS_ID")
	  REFERENCES "HEIS_SU"."UM_WORKFLOW_STAGES" ("WS_ID") ENABLE;
