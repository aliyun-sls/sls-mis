DELIMITER &&
CREATE PROCEDURE selectTags(IN version varchar(20))
BEGIN
    declare sleepTime integer default 0;
    SELECT sleep(sleepTime), name from tag;
end &&