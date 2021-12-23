# DSR FlightLog Database

## Database Platform

The DSR FlightLog production database uses MariaDB [https://mariadb.org] version 10
or higher.

## Initial Configuration
* Log in to MariaDB as the root user:
  ```
  sudo mariadb
  ```
* Create the DSR FlightLog database:
  ```
  create database flightlog default character set utf8 default collate utf8_general_ci;
  ```
* Create the perform user:
  ```
  grant all privileges on flightlog.* to 'flightlog'@'localhost' identified by '<password>' with grant option;
  ```
* Create the backup user:
  ```
  grant all privileges on flightlog.* to '<user>'@'localhost' identified by '<password>' with grant option;
  ```
