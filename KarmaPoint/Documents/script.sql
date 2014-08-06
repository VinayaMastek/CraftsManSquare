truncate msgsummary;
INSERT INTO msgsummary(
	userid, username, postreplied, avgresponses, threads, avglikes, noreponsethreads, responses)
(SELECT userid,fullname,0,0,0,0,0,0 FROM yamusers);


-- Get all the intiated threads
DROP TABLE IF EXISTS threads;
SELECT
	ym.msgid, 
	ym.senderid userid
INTO 	
	threads
FROM 	
	yammessages ym
WHERE
	ym.repliedtoid is null;

-- Get all Responses 
DROP TABLE IF EXISTS responses;
SELECT
	ym.msgid, 
	ym.threadid,
	ym.senderid responseuser, 
	0 threaduser
INTO 	
	responses
FROM 	
	yammessages ym
WHERE
	ym.repliedtoid is not null;
	
-- update user of the threads in the responses
UPDATE responses
SET threaduser = 
(Select userid from threads t where  t.msgid = responses.threadid);

	
-- Get userwise responses to thread
DROP TABLE IF EXISTS threadrsp;
SELECT 
	count(r.msgid) rspcount, 
	r.threadid,
	r.threaduser
INTO 
	threadrsp 
FROM 
	responses r
GROUP BY 
	r.threadid,
	r.threaduser;

	
-- Get Avg of responses for each user
drop table if exists avgthreads;
SELECT 
	sum(ut.rspcount) totalresp,
	count(ut.threadid) totthreads,
	round(sum(ut.rspcount)/
	count(ut.threadid),2) avg, 
	ut.threaduser,
	ym.fullname	
INTO avgthreads
FROM 
	threadrsp ut,
	yamusers ym
WHERE
	ut.threaduser = ym.userid
GROUP BY 
	ut.threaduser,
	ym.fullname	
ORDER BY ym.fullname;	


update 
	msgsummary 
set 
	threads = (select count(msgid) from threads t where msgsummary.userid = t.userid);

update 
	msgsummary 
set 
	responses = (select count(msgid) from responses r where msgsummary.userid = r.responseuser);

update 
	msgsummary 
set 
	avgresponses = a.avg
from avgthreads a
where msgsummary.userid = a.threaduser;


-- Get userwise count of responses to threads other than their own
select count(r.msgid)
from responses r, threads t
where t.msgid = r.threadid and
r.responseuser != t.userid
group by r.responseuser	;

update 
	msgsummary 
set 
	postreplied =(
	select count(r.msgid)
		from responses r, threads t
		where t.msgid = r.threadid and
		r.responseuser != t.userid and
		msgsummary.userid = r.responseuser
	) ;

	
--Get count of likes
update msgsummary 
set avglikes = 
(select round(sum(likedbycount) / (msgsummary.threads + msgsummary.responses),2)
from yammessages ym where ym.senderid = msgsummary.userid );

--count of threads without response
update msgsummary
set noreponsethreads =
(select 
	count(t.msgid)
FROM 
	threads t
WHERE t.msgid not in (select threadid from responses r where r.threaduser = t.userid) and 
msgsummary.userid = t.userid);






