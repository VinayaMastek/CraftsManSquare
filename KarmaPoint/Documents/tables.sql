-- Table: avgthreads

DROP TABLE IF EXISTS "avgthreads";

CREATE TABLE avgthreads
(
  totalresp numeric,
  totthreads bigint,
  avg numeric,
  threaduser integer,
  fullname character varying(100)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE avgthreads
  OWNER TO postgres;

-- Table: msgsummary
DROP TABLE IF EXISTIS "msgsummary";
CREATE TABLE msgsummary
(
  userid integer,
  postreplied integer,
  avgresponses integer,
  threads integer,
  avglikes integer,
  noreponsethreads integer,
  username character varying,
  responses integer
)
WITH (
  OIDS=FALSE
);
ALTER TABLE msgsummary
  OWNER TO postgres;


-- Table: parameters
DROP TABLE IF EXISTS "parameters";
CREATE TABLE parameters
(
  latestid integer,
  oldestid integer,
  download character varying,
  pages integer
)
WITH (
  OIDS=FALSE
);
ALTER TABLE parameters
  OWNER TO postgres;

  
-- Table: responses
DROP TABLE IF EXISTS "responses";
CREATE TABLE responses
(
  msgid integer,
  threadid integer,
  responseuser integer,
  threaduser integer
)
WITH (
  OIDS=FALSE
);
ALTER TABLE responses
  OWNER TO postgres;


-- Table: responseswithoutthread
DROP TABLE IF EXISTS "responseswithoutthread";
CREATE TABLE responseswithoutthread
(
  msgid integer,
  senderid integer,
  repliedtoid integer,
  msgtext character varying(1000),
  likedbycount integer,
  threadid integer
)
WITH (
  OIDS=FALSE
);
ALTER TABLE responseswithoutthread
  OWNER TO postgres;


-- Table: threadresponses
DROP TABLE IF EXISTS "threadresponses";
CREATE TABLE threadresponses
(
  msgid integer,
  userid integer,
  responsecount bigint
)
WITH (
  OIDS=FALSE
);
ALTER TABLE threadresponses
  OWNER TO postgres;

-- Table: threadrsp
DROP TABLE IF EXISTS "threadrsp";
CREATE TABLE threadrsp
(
  rspcount bigint,
  threadid integer,
  threaduser integer
)
WITH (
  OIDS=FALSE
);
ALTER TABLE threadrsp
  OWNER TO postgres;

  -- Table: threads

-- DROP TABLE threads;

CREATE TABLE threads
(
  msgid integer,
  userid integer
)
WITH (
  OIDS=FALSE
);
ALTER TABLE threads
  OWNER TO postgres;


-- Table: usrthread
DROP TABLE IF EXISTS "usrthread";

CREATE TABLE usrthread
(
  rspcount bigint,
  threadid integer,
  userid integer
)
WITH (
  OIDS=FALSE
);
ALTER TABLE usrthread
  OWNER TO postgres;

  
-- Table: yammessages
DROP TABLE "yammessages";
CREATE TABLE yammessages
(
  msgid integer,
  senderid integer,
  repliedtoid integer,
  msgtext character varying(1000),
  likedbycount integer,
  threadid integer
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yammessages
  OWNER TO postgres;

-- Table: yamusers

DROP TABLE IF EXISTS "yamusers";
CREATE TABLE yamusers
(
  userid integer,
  fullname character varying(100),
  email character varying(100),
  jobtitle character varying(100),
  activatedat character varying(100)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE yamusers
  OWNER TO postgres;
  