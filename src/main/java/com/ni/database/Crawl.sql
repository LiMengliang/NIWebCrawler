use niweb;

drop procedure reinit;

delimiter //

create procedure reinit() 
begin

DECLARE CONTINUE HANDLER FOR SQLEXCEPTION BEGIN END;

drop table tasks;

create table  tasks(
			task_id int not null auto_increment,
			url varchar(2048) not null,
			local_path varchar(2048),
			status char(1) not null default 'a',
			category char(1) not null default 't',
			last_modi_time DATETIME,
			md5 varchar(256),
	primary key(task_id)			
	);	

end//
delimiter ;

call reinit();