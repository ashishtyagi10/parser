select request_type from logrecords where ip_address="192.168.102.136";
Select ip_address from logrecords where date >="2017-01-01.15:00:00" and date <="2017-01-01.16:00:00" GROUP BY ip_address HAVING count(ip_address) > 200;
