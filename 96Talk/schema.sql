DROP TABLE IF EXISTS MEMBER CASCADE;
CREATE TABLE `MEMBER` (
	`MEMBER_ID`         BIGINT          NOT NULL    AUTO_INCREMENT,
	`MEMBER_EMAIL`      VARCHAR(50)     NOT NULL,
	`MEMBER_PWD`        VARCHAR(100)	NOT NULL,
	`MEMBER_NICKNAME`   VARCHAR(50)     NOT NULL,
	`MEMBER_REGDATE`    TIMESTAMP	    NOT NULL,
    PRIMARY KEY (MEMBER_ID)
);

DROP TABLE IF EXISTS MEMBER_AUTHORITY CASCADE;
CREATE TABLE `MEMBER_AUTHORITY` (
    `AUTHORITY_ID`	    BIGINT	        NOT NULL    AUTO_INCREMENT,
    `MEMBER_ID`         BIGINT	        NOT NULL,
    `AUTHORITY_ROLE`    VARCHAR(20)     NOT NULL,
    PRIMARY KEY (AUTHORITY_ID),
    FOREIGN KEY (MEMBER_ID) REFERENCES MEMBER (MEMBER_ID) ON DELETE CASCADE
);

DROP TABLE IF EXISTS PROFILE CASCADE;
CREATE TABLE `PROFILE` (
	`PROFILE_ID`	            BIGINT	        NOT NULL    AUTO_INCREMENT,
	`MEMBER_ID`	                BIGINT	        NOT NULL,
	`PROFILE_STATE_MESSAGE`	    VARCHAR(30)	        NULL,
	`PROFILE_UPLOAD_FILE_NAME`	VARCHAR(100)	    NULL,
	`PROFILE_STORE_FILE_NAME`	VARCHAR(100)	    NULL,
	PRIMARY KEY (PROFILE_ID),
    FOREIGN KEY (MEMBER_ID) REFERENCES MEMBER (MEMBER_ID) ON DELETE CASCADE
);

DROP TABLE IF EXISTS FRIEND CASCADE;
CREATE TABLE `FRIEND` (
	`FRIEND_ID`	        BIGINT	NOT NULL    AUTO_INCREMENT,
	`MEMBER_ID`	        BIGINT	NOT NULL,
	`FRIEND_MEMBER_ID`	BIGINT	NOT NULL,
	PRIMARY KEY (FRIEND_ID),
    FOREIGN KEY (MEMBER_ID) REFERENCES MEMBER (MEMBER_ID) ON DELETE CASCADE
);

DROP TABLE IF EXISTS CHATROOM CASCADE;
CREATE TABLE `CHATROOM` (
	`CHATROOM_ID`	            BIGINT	    NOT NULL    AUTO_INCREMENT,
	`CHATROOM_CHANNEL_ID`	    VARCHAR(36)	NOT NULL,
	`CHATROOM_WRITER_NICKNAME`	VARCHAR(50)	NOT NULL,
	`CHATROOM_TITLE`	        VARCHAR(50)	    NULL,
	`CHATROOM_IS_GROUP`         BOOLEAN     NOT NULL,
	`CHATROOM_REGDATE`	        TIMESTAMP	NOT NULL,
	PRIMARY KEY (CHATROOM_ID)
);

DROP TABLE IF EXISTS CHATROOM_MEMBER CASCADE;
CREATE TABLE `CHATROOM_MEMBER` (
	`CHATROOM_MEMBER_ID`	BIGINT	NOT NULL    AUTO_INCREMENT,
	`MEMBER_ID`	            BIGINT	NOT NULL,
	`CHATROOM_ID`	        BIGINT	NOT NULL,
    `CHATROOM_SUB_DATE`     TIMESTAMP   NULL,
    `CHATROOM_UN_SUB_DATE`  TIMESTAMP   NULL,
	PRIMARY KEY (CHATROOM_MEMBER_ID),
    FOREIGN KEY (MEMBER_ID) REFERENCES MEMBER (MEMBER_ID) ON DELETE CASCADE,
    FOREIGN KEY (CHATROOM_ID) REFERENCES CHATROOM (CHATROOM_ID) ON DELETE CASCADE
);