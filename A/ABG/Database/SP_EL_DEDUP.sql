--------------------------------------------------------
--  File created - Sunday-June-02-2013   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Procedure SP_EL_DEDUP
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "HEIS_SU"."SP_EL_DEDUP" (
    v_email_id       IN NUMBER,
    v_client_code    IN VARCHAR2,
    v_application_id IN VARCHAR2,
    v_amount         IN VARCHAR2 )
AS
  countt NUMBER(2);
BEGIN
  --- TO FIND IF THE NEW EMAIL ENTRY HAS A DUPLICATE RECORD OR NOT
  SELECT COUNT(*)
  INTO countt
  FROM email_attachment_info
  WHERE client_code =v_client_code
  AND application_id=v_application_id
  AND amount        =v_amount;
  IF countt         > 1 THEN
    UPDATE email_log
    SET process_flag='N',
      success_flag  ='D'
    WHERE email_id  =v_email_id;
  ELSE
    UPDATE email_log SET success_flag ='W' WHERE email_id =v_email_id;
  END IF;
END;

/
