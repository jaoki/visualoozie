
CREATE DATABASE lpbw CHARACTER SET utf8;
GRANT ALL PRIVILEGES ON lpbw.* TO lpbw_user@localhost identified by '1234';
FLUSH PRIVILEGES;

-- mvn clean compile
-- src\main\script\deployWar.bat
-- mysql -h localhost --protocol=tcp -u lpbw_user -p -D lpbw < target\generated-sources\hibernate3\sql\schema.sql

-- ftpData.bat

-- mysql -u lpbw_user -p -D lpbw < data.sql 

