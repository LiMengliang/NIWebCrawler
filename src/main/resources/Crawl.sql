use niweb;

drop procedure reinit;

delimiter //

create procedure reinit() 
begin

DECLARE CONTINUE HANDLER FOR SQLEXCEPTION BEGIN END;



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