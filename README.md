# AppointmentsStandalone
Standalone Appointments App

### Cloud9: Java 8
  * `sudo yum install java-1.8.0-openjdk-devel`
  * `sudo alternatives --config java`
  * `sudo yum remove java-1.7.0-openjdk-devel`

### Cloud9: PostgreSQL
  * `sudo yum install postgresql postgresql-server postgresql-devel postgresql-contrib postgresql-docs`
  * `sudo service postgresql initdb`
  * `sudo vim /var/lib/pgsql9/data/postgresql.conf`
    * Uncomment `#listen_addresses = 'localhost'` -> `listen_addresses = 'localhost'`
    * Uncomment `#port = 5432` -> `port = 5432`
  * `sudo vim /var/lib/pgsql9/data/pg_hba.conf`
    * ```
      # TYPE  DATABASE  USER      ADDRESS      METHOD
      # "local" is for Unit domain socket connections only
      local   all       all                    trust
      # IPv4 local connections:
      host    all       ec2-user  127.0.0.1/0  trust
      # IPv6 local connections:
      host    all       all       ::1/128      md5
      ```
  * `sudo service postgresql start` -> See: *Scripts > Start Database*

#### Configure Database
  * `sudo su - postgres`
  * `psql -U postgres`
  * `ALTER USER postgres WITH PASSWORD 'INSERT PASSWORD HERE';`
  * `CREATE USER "INSERT USER HERE" SUPERUSER;` e.g. `"ec2-user"`
  * `ALTER USER "INSERT USER HERE" WITH PASSWORD 'INSERT PASSWORD HERE';`
  * `CREATE DATABASE appointments;`
  * `\c appointments`
  * `CREATE TABLE appointments(id serial PRIMARY KEY, date TIMESTAMP NOT NULL, description VARCHAR(255) NOT NULL);`
  * `\q`
  * `exit`

### Scripts
#### Start Database
  * `cd AppointmentsStandalone`
  * `./init.sh`

#### Run Tests
  * `cd AppointmentsStandalone`
  * `./test.sh`

#### Run App
  * `cd AppointmentsStandalone`
  * `./run.sh`

### TODO list
  * Debug by time zone query string parameter
  * appointments/create redirect to appointments?
  * TimeZone selector
  * Appointment updating
  * Appointment deletion
  * index page?
