-- Query to fetch the request_type for a specific IP address
select request_type from logrecords where ip_address="192.168.102.136";

-- Query IPs that mode more than a certain number of requests for a given time period.
Select ip_address from logrecords where date >="2017-01-01.15:00:00" and date <="2017-01-01.16:00:00" GROUP BY ip_address HAVING count(ip_address) > 200;
