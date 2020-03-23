BEGIN TRANSACTION;

DROP TABLE IF EXISTS player_challenge;
DROP TABLE IF EXISTS player;
DROP TABLE IF EXISTS team;
DROP TABLE IF EXISTS contest;
DROP TABLE IF EXISTS challenge;
DROP TABLE IF EXISTS organization;

CREATE TABLE organization (
	org_id SERIAL,
	org_name VARCHAR(200) NOT NULL,
	CONSTRAINT pk_org PRIMARY KEY (org_id)
);

CREATE TABLE contest (
    contest_id SERIAL,
    org_id INTEGER NOT NULL,
    start_date TIMESTAMP DEFAULT now(),
    end_date TIMESTAMP,
    CONSTRAINT pk_contest PRIMARY KEY (contest_id),
    FOREIGN KEY (org_id) REFERENCES organization(org_id)
);

CREATE table team (
    team_id SERIAL,
    team_name VARCHAR(200) NOT NULL,
    contest_id INTEGER NOT NULL,
    CONSTRAINT pk_team PRIMARY KEY (team_id),
    FOREIGN KEY (contest_id) REFERENCES contest(contest_id)
);

CREATE TABLE player (
    player_id SERIAL,
    player_name VARCHAR(200) NOT NULL,
    points INTEGER DEFAULT 0,
    team_id INTEGER NOT NULL,
    CONSTRAINT pk_player PRIMARY KEY(player_id),
    FOREIGN KEY (team_id) REFERENCES team(team_id)
);

CREATE TABLE challenge (
    challenge_id SERIAL,
    challenge_name VARCHAR(100) NOT NULL,
    points INTEGER NOT NULL,
    time_frame VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL,
    CONSTRAINT pk_challenge PRIMARY KEY (challenge_id)
 );
 
 CREATE TABLE player_challenge (
 	player_id INTEGER NOT NULL,
 	challenge_id INTEGER NOT NULL,
 	CONSTRAINT pk_player_challenge PRIMARY KEY (player_id, challenge_id),
 	FOREIGN KEY (player_id) REFERENCES player(player_id),
 	FOREIGN KEY (challenge_id) REFERENCES challenge(challenge_id)
 );
 
 INSERT INTO challenge (challenge_name, points, time_frame, description)
 VALUES ('100 Push Ups', 20, 'day', 'Do 100 push in one Day');
 
 COMMIT;