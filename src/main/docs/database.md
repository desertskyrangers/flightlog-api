# DSR FlightDeck Database

## Database Platform

The DSR FlightDeck production database uses MariaDB [https://mariadb.org] version 10
or higher.

## Initial Configuration
* Log in to MariaDB as the root user:
  ```
  sudo mariadb
  ```
* Create the DSR FlightDeck database:
  ```
  create database flightdeck default character set utf8 default collate utf8_general_ci;
  ```
* Create the perform user:
  ```
  grant all privileges on flightdeck.* to 'flightdeck'@'localhost' identified by '<password>' with grant option;
  ```
