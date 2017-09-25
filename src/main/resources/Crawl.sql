use niweb;

drop procedure reinit;

delimiter //

create procedure reinit() 
begin

DECLARE CONTINUE HANDLER FOR SQLEXCEPTION BEGIN END;

drop table tasks_discus;
drop table examples;

create table  tasks_discus(
			task_id int not null auto_increment,
			url varchar(1024) not null,
			local_path varchar(2048),
			status char(1) not null default 'a',
			category char(1) not null default 't',
			last_modi_time DATETIME,
			md5 varchar(256),
			primary key(task_id),
			unique key url (url)
	);	

create table examples(
			id int not null auto_increment,
			url varchar(1024),
			creation_time datetime,
			last_edit_time datetime,
			author varchar(256),
			kudos int,
			tags blob,
			overview blob,
			description blob,
			requirements blob,
			steps blob,
			additional_info blob,
			full_content blob,
			draft boolean,
			attachment_urls blob,
			primary key(id),
			unique key url (url)
);

end//
delimiter ;

call reinit();