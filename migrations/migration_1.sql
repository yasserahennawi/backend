DROP TABLE user;
DROP TAbLE crush;

CREATE TABLE user (
	userID varchar(255) primary key,
	fbToken varchar(255) NOT NULL 
);

CREATE TABLE crush (
	userID varchar(255) ,
	crushID varchar(255),
	PRIMARY KEY (userID, crushID),
	FOREIGN KEY (userID) REFERENCES user(userID)
);

INSERT INTO user (userID, token) 
VALUES 
('omaraya11', 'omaraya11@gmail'),
('omaraya12', 'omaraya1ssadaad1@gmail'), 
('omaraya13', 'omaraya1sad1@gmail'),
('omaraya14', 'omarayadsa11@gmail'),
('omaraya15', 'omarayadas11@gmail'),
('omaraya16', 'omarayadsa11@gmail'),
('omaraya17', 'omaraya1dsa1@gmail'),
('omaraya18', 'omarayadsa11@gmail'),
('omaraya19', 'omarayadas11@gmail'),
('omaraya20', 'omarayadsa11@gmail'),
('omaraya21', 'omarayadas11@gmail'),
('omaraya22', 'omarayadsa11@gmail');

INSERT INTO crush 
VALUES
('omaraya11','yasserhenawi'),
('omaraya11','karimaly'),
('omaraya11','nora'),
('omaraya12','lolo'),
('omaraya12','bosy505225'),
('omaraya13','yasserhenawi'),
('omaraya14','so3ad'),
('omaraya20','dede'),
('omaraya21','lolo');
