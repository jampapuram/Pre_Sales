--------------------------------------------------------
--  File created - Sunday-June-02-2013   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Trigger TRG_WORKFLOW_SYSTXN_U
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "HEIS_SU"."TRG_WORKFLOW_SYSTXN_U" 
AFTER UPDATE  --Source Table
    OF DECISION, REASONFOR_APPR_REJ, UPD_DT, UPD_BY 
    ON HI_REQUEST_TXN FOR EACH ROW
DECLARE 
  t_EMAIL_ID NUMBER (8,0) ;
  t_ROWID ROWID;
  t_WORKFLOWSTAGEID VARCHAR2(50 BYTE);
  t_WORKFLOWID      VARCHAR2(50 BYTE);
  t_SEQNO           NUMBER(8,0);
  t_APPLSTAGE       VARCHAR2(50 BYTE);
  t_STAGEDESC1      VARCHAR2(4000 BYTE);
  -- PROCESS: To create workflow in HI_APPLICATION_WF_OTH from System Transactions on update in HI_REQUEST_TXN.
  -- DATE:    31-May-2013
  BEGIN
    /*   BEGIN 
         -- SELECTING THE EMAIL ID FOR  MAPPING AND ROW ID FOR UPDATE OF REQUEST CODE IN EMAIL_LOG.
      SELECT DISTINCT EL.EMAIL_ID, EL.ROWID
      INTO t_EMAIL_ID,
        t_ROWID
      FROM EMAIL_ATTACHMENT_INFO EAI,
        EMAIL_LOG EL
      WHERE EAI.email_id      = EL.email_id
      AND EAI.APPLICATION_ID  = :NEW.APPLICATION_ID
      AND EAI.AMOUNT          = :NEW.AMOUNT
      AND EAI.TYPE_OF_REQUEST = :NEW.TYPE_OF_REQUEST
      AND EAI.CLIENT_CODE     = :NEW.CLIENT_CODE
      AND EL.PROCESS_FLAG     = 'Y'
      AND EL.SUCCESS_FLAG    <> 'D'
      AND EL.APPLSTAGE       IN ('DAPPR_REQ_MAIL', 'PAPPR_REQ_MAIL')
      AND EL.ORG_ID=9000; --hardcoded the ord id;
           EXCEPTION      
       WHEN NO_DATA_FOUND THEN 
      t_EMAIL_ID := :NEW.REQUEST_CODE; 
      END;
     */
     
        ----SELECTING WORKFLOW ID FROM WORKFLOW MASTER FOR DISB N PLEDGE FOR APPROVED AND REJECTED
      IF (:NEW.DECISION='A') THEN
      
      IF (:NEW.TYPE_OF_REQUEST = 'D') THEN
        SELECT UWS.WORKFLOWSTAGEID,
          UWS.WORKFLOWID,
          UWS.SEQNO,
          uws.applstage,
          uws.stage_desc1
        INTO t_WORKFLOWSTAGEID,
          t_WORKFLOWID,
          t_SEQNO,
          t_APPLSTAGE,
          t_STAGEDESC1
        FROM um_workflow_stages UWS
        WHERE UWS.APPLSTAGE = 'DDECISION_APP_SYS'; ---hardcoded the stage...
      ELSE
        SELECT UWS.WORKFLOWSTAGEID,
          UWS.WORKFLOWID,
          UWS.SEQNO,
          uws.applstage,
          uws.stage_desc1
        INTO t_WORKFLOWSTAGEID,
          t_WORKFLOWID,
          t_SEQNO,
          t_APPLSTAGE,
          t_STAGEDESC1
        FROM um_workflow_stages UWS
        WHERE UWS.APPLSTAGE = 'PDECISION_APP_SYS'; ---hardcoded the stage...
      END IF;
    ELSE  
     IF (:NEW.TYPE_OF_REQUEST = 'D') THEN
        SELECT UWS.WORKFLOWSTAGEID,
          UWS.WORKFLOWID,
          UWS.SEQNO,
          uws.applstage,
          uws.stage_desc1
        INTO t_WORKFLOWSTAGEID,
          t_WORKFLOWID,
          t_SEQNO,
          t_APPLSTAGE,
          t_STAGEDESC1
        FROM um_workflow_stages UWS
        WHERE UWS.APPLSTAGE = 'DDECISION_REJ_SYS'; ---hardcoded the stage...
      ELSE
        SELECT UWS.WORKFLOWSTAGEID,
          UWS.WORKFLOWID,
          UWS.SEQNO,
          uws.applstage,
          uws.stage_desc1
        INTO t_WORKFLOWSTAGEID,
          t_WORKFLOWID,
          t_SEQNO,
          t_APPLSTAGE,
          t_STAGEDESC1
        FROM um_workflow_stages UWS
        WHERE UWS.APPLSTAGE = 'PDECISION_REJ_SYS'; ---hardcoded the stage...
      END IF;
     END IF;     
     
    /*
    -- SELECTING THE previous stage entry FOR  MAPPING AND ROW ID FOR UPDATE OF REQUEST CODE IN WORKFLOW STAGE. 
  
     SELECT DISTINCT HAWO.SOURCE_WK_CODE, HAWO.ROWID
      INTO t_EMAIL_ID,
        t_ROWID
      FROM HI_APPLICATION_WK_OTH HAWO
        WHERE HAWO.APPLICATION_NO  = :NEW.APPLICATION_ID
        and max(HAWO.SEQNO) < t_SEQNO
        and hawo.WORKFLOWID = t_WORKFLOWID 
                    AND hawo.ORG_ID=9000; --hardcoded the ord id;
           EXCEPTION      
       WHEN NO_DATA_FOUND THEN 
      t_EMAIL_ID := :NEW.REQUEST_CODE; 
      END;
      */

  
   
      ---INSERTING TO CREATE A ENTRY IN WORKFLOW TABLE
      INSERT
      INTO HI_APPLICATION_WF_OTH
        (
          ORG_ID ,
          SOURCE_SYS_ID ,
          APPLICATION_NO ,
          SOURCE_WK_CODE,
          SEQNO,
          APPLICATION_STAGE ,
          WORKFLOWSTAGEID ,
          WORKFLOWID ,
          STAGE_START_DT,
          STAGE_END_DT ,
          STAGE_STATUS ,
          STATUS_BUSINESS_DT ,
          SYS_DECISION_CODE,
          FINAL_DECISION_CODE ,
          DECISION_OWNER_CODE ,
          REASON_FOR_DECISION ,
          CR_ID ,
          CR_DT ,
          ACTIVE ,
          ACTIVATION_DT
        )
        VALUES
        (
          9000 ,               --hardcoded for now
          5101 ,              ---ABG system ID from UM_SRC_SYS
          :NEW.APPLICATION_ID, -- APPLICATION_NO ,
          :NEW.REQUEST_CODE,--t_EMAIL_ID,          --SOURCE_WK_CODE,
          t_SEQNO,
          t_APPLSTAGE ,
          t_WORKFLOWSTAGEID ,
          T_WORKFLOWID ,
          SYSDATE, --:NEW.UPD_DT,             --STAGE_START_DT ,--CHANGED BY RITESH , TEMP FOR TAT
          NULL, -- SYSDATE,                   --STAGE_END_DT ,--CHANGED BY RITESH TO CORRECT WRONG STAMPING IN STAGE END DATE (01-06-2013)
          'P',                    --STAGE_STATUS ,
          sysdate ,               --STATUS_BUSINESS_DT
          :NEW.DECISION,          --SYS_DECISION_CODE
          t_STAGEDESC1,          --FINAL_DECISION_CODE , DESC FROM um_workflow_stages
          :NEW.CHECKER_Id,        --DECISION_OWNER_CODE ,
          :NEW.REASONFOR_APPR_REJ,--REASON_FOR_DECISION ,
          :NEW.UPD_BY,          --CR_ID ,
          :NEW.UPD_DT ,            --CR_DT
          'Y' ,
          SYSDATE
        );
        
        BEGIN
      ---UPDATE THE PREVIOUS SSTAGE WITH COMPLETE FLAG AND STAGE END DATE
        UPDATE HI_APPLICATION_WF_OTH HAWO
        SET hawo.stage_end_dt = SYSDATE,
       HAWO.STAGE_STATUS ='C'
        WHERE HAWO.APPLICATION_NO = :NEW.APPLICATION_ID
        AND HAWO.SOURCE_WK_CODE = :NEW.REQUEST_CODE --t_EMAIL_ID
        AND HAWO.SEQNO = (SELECT MAX(SEQNO) FROM HI_APPLICATION_WF_OTH
                 WHERE ORG_ID = 9000 -- hardcoded
                  AND APPLICATION_NO = :NEW.APPLICATION_ID
                  AND SEQNO < t_SEQNO
                  AND SOURCE_WK_CODE = :NEW.REQUEST_CODE --t_EMAIL_ID
                  ) 
        AND HAWO.ORG_ID=9000; --hardcoded the ord id
        EXCEPTION WHEN OTHERS THEN  NULL;
        END;
    
   END;
/
ALTER TRIGGER "HEIS_SU"."TRG_WORKFLOW_SYSTXN_U" ENABLE;
