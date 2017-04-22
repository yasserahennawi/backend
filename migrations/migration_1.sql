DROP TAbLE crush;
DROP TABLE user;

CREATE TABLE user (
	appUserID varchar(255) primary key,
	fbUserID varchar(255),
	fbToken varchar(255) NOT NULL 
);

CREATE TABLE crush (
	appUserID varchar(255) ,
	fbCrushID varchar(255),
	createdAt datetime DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (appUserID, fbCrushID),
	FOREIGN KEY (appUserID) REFERENCES user(appUserID)
);

INSERT INTO user (appUserID, fbUserID, fbToken) 
VALUES 
('omaraya11', 'omar' , 'omaraya11@gmail'),
('omaraya12', 'so3ad' , 'omaraya1ssadaad1@gmail'), 
('omaraya13', 'yasserhenawi' , 'omaraya1sad1@gmail'),
('omaraya14', 'koko' , 'omarayadsa11@gmail'),
('omaraya15', 'karimaly' , 'omarayadas11@gmail'),
('omaraya16', '' , 'omarayadsa11@gmail'),
('omaraya17', 'nora' , 'omaraya1dsa1@gmail'),
('omaraya18', '' , 'omarayadsa11@gmail'),
('omaraya19', '' , 'omarayadas11@gmail'),
('omaraya20', '343532' , 'omarayadsa11@gmail'),
('omaraya21', '56576' , 'omarayadas11@gmail'),
('omaraya22', '' , 'omarayadsa11@gmail');

INSERT INTO crush (appUserID, fbCrushID)
VALUES
('omaraya11', 'yasserhenawi'),
('omaraya11', 'karimaly'),
('omaraya11', 'nora'),
('omaraya12', 'lolo'),
('omaraya12', 'bosy505225'),
('omaraya13', 'yasserhenawi'),
('omaraya14', 'so3ad'),
('omaraya20', 'dede'),
('omaraya21', 'lolo'),
('omaraya12', 'omar'),
('omaraya17', 'omar'),
('omaraya15', 'omar');


