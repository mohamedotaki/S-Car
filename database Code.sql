USE SCar
;
drop TABLE if exists login
;
drop TABLE if exists owners
;
drop TABLE if exists drivers
;

CREATE  table login (
	loginId INT AUTO_INCREMENT,
		ownerId Int NOT NULL,
	email VARCHAR (100) NOT NULL ,
	password VARCHAR (100) NOT NULL ,
	constraint fk_login foreign key (ownerId)
		references owners (ownerId),
	constraint login_id primary key (loginId)
) 
;
CREATE  table owners (
	ownerId int AUTO_INCREMENT,
	fullName VARCHAR (100) NOT NULL ,
	phoneNumber VARCHAR (100) NOT NULL ,
	carNumber VARCHAR (100) NOT NULL ,
	keyNo VARCHAR (100) NOT NULL,
	constraint pk_ownerId PRIMARY key (ownerId)
) 
;

CREATE  table drivers (
	driverId  int AUTO_INCREMENT,
		ownerId Int NOT NULL,
	fullName varchar (20) NOT NULL ,
	emailAddress varchar (20) NOT NULL ,
	phoneNumber varchar (20) NOT NULL ,
	admin BOOLEAN NOT NULL,
	allowedTime  DATE,
	constraint fk_drivers foreign key (ownerId)
		references owners (ownerId),
	constraint pk_drivers PRIMARY key (driverId)
) 
;
