# Scala with Mill Boilerplate

This boilerplate repository serves as a place to play around with Scala, Slick, code-generation and hopefully more.
It uses @lihaoyi's *nifty af* built tool: Mill.
So far it's been pleasant to use and way more fun than using sbt and far less arduous than using bazel.

- **[Mill](https://github.com/lihaoyi/mill)** as our build tool
- **Scala 2.12.10** as our language 😉
- **[Slick](http://scala-slick.org/doc/3.3.2/introduction.html)** to type-safely query our db, and some neat generated code
- **[PostgreSQL](https://www.postgresql.org/download/macosx/)** for _databasing_
 
## Getting Started

If you're using macOS and you haven't already, grab [Homebrew](https://brew.sh) it makes package management on macOS much easier.
Otherwise, head over to [Mill's website](https://www.lihaoyi.com/mill/index.html).

Get mill
```zsh
$ brew install mill
``` 

Clone down this repo and navigate to it
```zsh
$ git clone git@github.com:raayan/boiler-mill-scala.git
$ cd boiler-mill-scala
```

Get postgres
```zsh
$ brew install postgresql
```

Start postgres up
```zsh
$ brew services start postgresql
```

List all the databases on our Postgres server
```zsh
$ psql -l
                                     List of databases
     Name     |    Owner     | Encoding |   Collate   |    Ctype    |   Access privileges   
--------------+--------------+----------+-------------+-------------+-----------------------
 postgres     | postgres     | UTF8     | en_US.UTF-8 | en_US.UTF-8 | 
 raayanpillai | raayanpillai | UTF8     | en_US.UTF-8 | en_US.UTF-8 | 
 template0    | postgres     | UTF8     | en_US.UTF-8 | en_US.UTF-8 | =c/postgres postgres=CTc/postgres
 template1    | postgres     | UTF8     | en_US.UTF-8 | en_US.UTF-8 | =c/postgres postgres=CTc/postgres
(5 rows)
```

In this sample application we use a database called `zoo` as you can see it's not here yet.

Let's create the db
```zsh
$ psql -c 'CREATE DATABASE zoo;'
CREATE DATABASE
```

Then let's run some SQL to do some setup
```zsh
$ psql zoo -f zoodb/tables/src/main/resources/sql/zoodb.sql
CREATE TYPE
CREATE TABLE
INSERT 0 10
```

That SQL script created a `pg_enum`, a table called `animals` and inserted 10 rows of sample data.

Now run the following `mill` command to run the template app
```zsh
$ mill app
```

You should see some heating up, compiling and then finally:

```zsh
[main] INFO app.Application$ - Connecting to DB
[main] INFO zoodb.profile.ZooDbPostgresProfile -  >>> binding uuid -> java.util.UUID 
[main] INFO zoodb.profile.ZooDbPostgresProfile -  >>> binding text -> java.lang.String 
[main] INFO zoodb.profile.ZooDbPostgresProfile -  >>> binding bool -> Boolean 
[main] INFO app.Application$ - 
Query Results:
AnimalsRow(1,2A44259755D38E6D163E820,Cetacea)
AnimalsRow(2,9CAA6E02C990B0A82652DCA,Dasyuromorphia)
AnimalsRow(3,FE97759AA27A0C99BFF6710,Afrosoricida)
AnimalsRow(4,124A10E0DB5E4B97FC2AF39,Erinaceomorpha)
AnimalsRow(5,2BCC25A6F606EB525FFDC56,Cingulata)
AnimalsRow(6,062936A96D3C8BD1F8F2FF3,Peramelemorphia)
AnimalsRow(7,EAE257E44AA9D5BADE97BAF,Scandentia)
AnimalsRow(8,9B086079795C442636B55FB,Perissodactyla)
AnimalsRow(9,568161A8CDF4AD2299F6D23,Macroscelidea)
AnimalsRow(10,9908345F7439F8FFABDFFC4,Pilosa)

[main] INFO app.Application$ - 
Insert Result:
AnimalsRow(11,Polo,Carnivora)
```

### Intellij Configuration

Running the following mill command will properly configure the project with Intellij and then open it.
```zsh
$ mill mill.scalalib.GenIdea/idea
$ idea .
```

That's really all there is to at the moment!
