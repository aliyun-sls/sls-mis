DELIMITER &&
CREATE PROCEDURE selectTags(IN version varchar(20))
BEGIN
    declare sleepTime integer default 0;
    /*Mock sleep time*/
    if version = '1.3.0' then
        set sleepTime = 1;
    end if;

    SELECT sleep(sleepTime), name from tag;
end &&