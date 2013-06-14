--------------------------------------------------------
--  File created - Friday-June-14-2013   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Procedure SP_INS_TEMP_SYS_TXN
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "HEIS_SU"."SP_INS_TEMP_SYS_TXN" (
p_SOURCE_WK_CODE In Number,
p_workflow_type In varchar2,
p_WORKFLOWID In Varchar2,
p_SEQNO In Number, 
p_FROM_EMAIL_ADDRESS In Varchar2) AS

  
BEGIN

DECLARE 
  
v_WORKFLOWID      varchar2(50) := p_WORKFLOWID ;
v_SEQNO           Number(8) :=  p_SEQNO ;
v_FROM_EMAIL_ADDRESS VARCHAR2(2000) := p_FROM_EMAIL_ADDRESS  ;
v_REQUEST_CODE    NUMBER(8) := p_SOURCE_WK_CODE ;
v_WORKFLOW_TYPE VARCHAR2(100) := p_WORKFLOW_TYPE ;


v_cur_SOURCE_SYS_ID NUMBER(8);
v_cur_WORKFLOWSTAGEID VARCHAR2(50);
v_cur_SEQNO NUMBER(8);
v_cur_APPLSTAGE VARCHAR2(50);
v_cur_stage_desc VARCHAR2(50); 
v_cur_TIMER_IN_MINS NUMBER(8);
  
/* Defining cursor */
  CURSOR cur_workflow_stages
  is 
	SELECT a.SOURCE_SYS_ID, a.WORKFLOWSTAGEID,a.SEQNO, 
	       a.APPLSTAGE, substr(a.stage_desc1,1,50) stage_desc, nvl(a.TIMER_IN_MINS,0)
	FROM UM_WORKFLOW_STAGES a 
	WHERE a.ORG_ID = 9000 -- hardcoded
	AND   a.WORKFLOWID = v_WORKFLOWID
	AND   a.SEQNO > v_seqno
	AND   a.WORKFLOW_TYPE = v_WORKFLOW_TYPE
	AND   a.ACTIVE = 'Y'
	AND   a.module_desc1 like 'SIMULATED%'
	ORDER BY SEQNO
; 
   

BEGIN
   Open cur_workflow_stages;
INSERT INTO DUMMY_FOR_PROC
    (
      rundate,
      seq_no,
      msg
    )
    VALUES
    (
      Sysdate,
      10,
      'Just before entering the loop for request '|| v_request_code
    );
commit;

   loop
      fetch cur_workflow_stages into 
  		v_cur_SOURCE_SYS_ID, v_cur_WORKFLOWSTAGEID, v_cur_SEQNO, v_cur_APPLSTAGE, v_cur_stage_desc, v_cur_TIMER_IN_MINS ;
	EXIT WHEN cur_workflow_stages%NOTFOUND;
      
  -- Insert into TEMP_SYSTEM_TXN2
INSERT INTO DUMMY_FOR_PROC
    (
      rundate,
      seq_no,
      msg
    )
    VALUES
    (
      Sysdate,
      10,
      'Just before insert in temp for request '|| v_request_code
    );
commit;

  INSERT INTO TEMP_SYSTEM_TXN2
	(
	REQUEST_CODE,
	SOURCE_SYS_ID,
	WORKFLOWSTAGEID,
	WORKFLOW_TYPE,
	WORKFLOWID,
	SEQNO,
	APPLSTAGE,
	STAGE_DESC,
	FROM_EMAIL_ADDRESS1,
  TIMER_IN_MINS,
	PROCESS_FLAG,
	CR_DT       
	)
	VALUES
  	(
	v_REQUEST_CODE,
	v_cur_SOURCE_SYS_ID, 
	v_cur_WORKFLOWSTAGEID, 
	v_WORKFLOW_TYPE, 
	v_WORKFLOWID,
	v_cur_SEQNO, 
	v_cur_APPLSTAGE, 
	v_cur_stage_desc,
	v_FROM_EMAIL_ADDRESS,
	v_cur_TIMER_IN_MINS,
  'N',
	sysdate
	)
	;


	COMMIT;
   END LOOP; -- cur_workflow_stages end loop
      close cur_workflow_stages;
END;
END;

/
