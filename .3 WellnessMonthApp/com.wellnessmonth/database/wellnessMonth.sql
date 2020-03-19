BEGIN TRANSACTION;

DROP TABLE IF EXISTS player;
DROP TABLE IF EXISTS team;
DROP TABLE IF EXISTS contest;
DROP TABLE IF EXISTS challenge;

CREATE TABLE player (
    player_id SERIAL,
    player_name VARCHAR(200) NOT NULL,
    points INTEGER DEFAULT 0,
    team_id INTEGER NOT NULL,
    CONSTRAINT pk_player PRIMARY KEY(player_id)
);

CREATE table team (
    team_id SERIAL,
    team_name VARCHAR(200) NOT NULL,
    contest_id INTEGER NOT NULL,
    CONSTRAINT pk_team PRIMARY KEY (team_id)
);

CREATE TABLE contest (
    contest_id SERIAL,
    org_id INTEGER NOT NULL,
    start_date TIMESTAMP DEFAULT now(),
    end_date TIMESTAMP
);

CREATE TABLE challenge (
    challenge_id SERIAL,
    challenge_name VARCHAR(100) NOT NULL,
    points INTEGER NOT NULL,
    timeframe VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL
 );
 
 COMMIT;