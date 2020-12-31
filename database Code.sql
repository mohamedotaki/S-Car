USE SCar
;
drop TABLE if exists login
;
drop TABLE if exists otherUsers
;

CREATE  table login (
	id Int NOT NULL AUTO_INCREMENT,
	email varchar (20) NOT NULL ,
	password varchar (20) NOT NULL ,
	carNo varchar (20) NOT NULL ,
	constraint login_id primary key (id)
) 
;

CREATE  table otherUsers (
	id Int NOT NULL AUTO_INCREMENT,
	name varchar (20) NOT NULL ,
	password varchar (20) NOT NULL ,
	constraint otherUsers_id PRIMARY key (id)
) 
;
