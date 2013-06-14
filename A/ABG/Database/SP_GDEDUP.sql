--------------------------------------------------------
--  File created - Friday-June-14-2013   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Procedure SP_GDEDUP
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "HEIS_SU"."SP_GDEDUP" 
(v_client_code in varchar2,v_application_id in varchar2,v_amount in varchar2,v_min_attach_id out varchar2,v_min_email_id out number,v_max_attach_id out varchar2,v_max_email_id out number,
cl_code out varchar2,app_id out varchar2,amt out number,countt out number)
as
begin
select min(attachment_id),min(email_id),max(attachment_id),max(email_id),client_code,application_id,amount into v_min_attach_id,v_min_email_id,v_max_attach_id,v_max_email_id,cl_code,app_id,amt from 
email_attachment_info
where 
client_code=v_client_code and
application_id=v_application_id and
amount=v_amount
group by client_code,application_id,amount;
select count(*) into countt from email_attachment_info
where client_code=cl_code and application_id=app_id and amount=amt
group by client_code,application_id,amount
having count(*) >1;
if countt > 1 then
update email_log
set process_flag='N',success_flag='D' where email_id=v_max_email_id;
update email_log
set success_flag='W' where email_id=v_min_email_id; 
--elsif countt < 1 then
--update email_log
--set 
--success_flag='S' where email_id=v_min_email_id;
--(select min(email_id) 
--into attach_id,v_email_id,cl_code,app_id,amt 
--from 
--email_attachment_info
--where 
--client_code=cl_code and
--application_id=app_id and
--amount=amt
--group by client_code,application_id,amount);
else
update email_log
set 
success_flag='D' where email_id=v_max_email_id ;
end if;
end;

/
