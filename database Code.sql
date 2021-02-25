USE scar
;

drop TABLE if exists drivers
;

drop TABLE if exists events
;

drop TABLE if exists owners
;

drop TABLE if exists login
;


CREATE  table login (
	loginId INT AUTO_INCREMENT,
	email VARCHAR (100) NOT NULL ,
	password VARCHAR (100) NOT NULL ,
		isOwner boolean NOT NULL ,
	constraint login_id primary key (loginId)
) 
;

CREATE  table owners (
	ownerId int AUTO_INCREMENT,
	loginId INT NOT NULL,
	fullName VARCHAR (100) NOT NULL ,
	phoneNumber VARCHAR (100) NOT NULL ,
	carNumber VARCHAR (100) NOT NULL ,
	keyNo VARCHAR (100) NOT NULL,
	imageId INT,
		constraint fk_drivers foreign key (loginId)
		references login (loginId),
	constraint pk_driverId PRIMARY key (ownerId)
) 
;

CREATE  table drivers (
	driverId int AUTO_INCREMENT,
	loginId INT NOT NULL,
	ownerId INT NOT NULL,
	fullName VARCHAR (100) NOT NULL ,
	phoneNumber VARCHAR (100) NOT NULL ,
	carNumber VARCHAR (100) NOT NULL ,
	keyNo VARCHAR (100) NOT NULL,
	drivingPermission VARCHAR (100) ,
	imageId INT,
		constraint fk_drivers_login foreign key (loginId)
		references login (loginId),
		constraint fk_drivers_owner foreign key (ownerId)
		references owners (ownerId),
	constraint pk_driverId PRIMARY key (driverId)
) 
;

CREATE table events (
	eventId int AUTO_INCREMENT,
	ownerId INT NOT NULL,
	title VARCHAR (100) NOT NULL,
	address VARCHAR (100) NOT NULL ,
	eventDate VARCHAR (100) NOT NULL ,
		constraint fk_events_owner foreign key (ownerId)
		references owners (ownerId),
	constraint pk_eventsId PRIMARY key (eventId)
) 
;

