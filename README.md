# TODO-taskmanager

This is a backend project for task manager server. In saves tasks for users of this server.
You can easily add new task and marked it 'done' when you will done this task

## Getting Started

Install software that used in this application 

###Prerequisites

Install JDK. On the command line, type

```bash
$ sudo apt-get install openjdk-8-jdk
```

Install Apache Maven for building this project

```bash
$ sudo apt install maven
```

Install PostgreSQL. You may use this command

```bash
$ sudo apt-get install postgresql
```

### Installation

Now you can install this server

- Firstly, clone this repository

```bash
$ git clone https://github.com/victimoftrap/todo-taskmanager.git
```

- Now you need to create database and user for database. This code solves it

```bash
$ sudo -u postgres psql
postgres=# CREATE DATABASE eisetasks;
postgres=# CREATE USER someuser WITH ENCRYPTED PASSWORD 'somepass';
postgres=# GRANT ALL PRIVILEGES ON DATABASE eisetasks TO someuser;
postgres=# \q
```

- Move to project package and run this command for build project

```bash
$ mvn package
```

- Run project

```bash
cd ./target/
java -jar todo-taskmanager-1.0-SNAPSHOT.jar
```

Now this server will started on port 8080

## Usage

All methods, available in this project, you can see here:
- [API](https://app.swaggerhub.com/apis-docs/pawlaz/base-web-development-restfull-task-manager/6.0.0)