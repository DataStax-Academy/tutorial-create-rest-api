# Setup your environment

Follow this instruction to setup your environment. And go through the tutorial

### Install Git, Java and Maven

If not already available please, install 
* [Download and install GIT](https://git-scm.com/downloads)
* [Download and install Java8+](http://www.oracle.com/technetwork/java/javase/downloads/jdk10-downloads-4416644.html)
* [Download and install Maven](https://maven.apache.org/download.cgi)

Here are the commands to install on MAC with `brew`:
```
brew install git
brew tap caskroom/versions
brew update
brew cask install java8
brew install maven
```

You should be able to run `mvn -v`
```
Apache Maven 3.5.2 (138edd61fd100ec658bfa2d307c43b76940a5d7d; 2017-10-18T09:58:13+02:00)
Maven home: /usr/local/Cellar/maven/3.5.2/libexec
Java version: 1.8.0_162, vendor: Oracle Corporation
Java home: /Library/Java/JavaVirtualMachines/jdk1.8.0_162.jdk/Contents/Home/jre
Default locale: en_FR, platform encoding: UTF-8
OS name: "mac os x", version: "10.13.4", arch: "x86_64", family: "mac"
```

### Install your IDE

Download and install your favorite IDE. 
Here are some of the most relevant. Video tutorial have been captured with Eclipse STS.
* [Eclipse](https://www.eclipse.org/downloads/eclipse-packages/)
* [Eclipse STS](https://spring.io/tools/sts/all)
* [IntelliJ](https://www.jetbrains.com/idea/)

### Download and install Docker

* [Download and install Docker](https://docs.docker.com/install)

Here are the commands to install on MAC with `brew`:
```
brew install docker
```

You should be able to execute `docker -v`
```
Docker version 18.03.0-ce, build 0520e24
```

### Start DSE and Studio

* Clone the tutorial repository 
```
git clone https://github.com/DataStax-Academy/tutorial-create-rest-api.git
cd tutorial-create-rest-api
```

* Startup Studio and DSE using [Docker Compose](https://docs.docker.com/compose/) :
```
docker-compose up -d
```

Your should get output :
```
cedricklunven@Cedricks-MBP:~/dev/workspace-misc/tutorial-create-rest-api> docker-compose up -d
Creating network "tutorialcreaterestapi_default" with the default driver
Creating tutorialcreaterestapi_dse6_1 ... done
Creating tutorialcreaterestapi_studio_1 ... done
```

To see your running containers :
```
docker ps | grep datastax
```

*Datastax provides docker images on the [Dockerhub](https://hub.docker.com/u/datastax). This tutorial will use latest version of DSE and studio. At anytime you can download and use them in development. Production usages require licenses. Here the commands to start the container on your station at anytime.*
```
docker run -e "DS_LICENSE=accept" -it -d -p 9042:9042 --name dse6 datastax/dse-server
docker run -e "DS_LICENSE=accept" -it -d -p 9091:9091 --link dse6:dse6 datastax/dse-studio
```


### Setup studio

* Connect to [Datastax Studio](http://localhost:9091)

* Edit connection to use hostname dse6 (and not 127.0.0.1)
<img src="https://raw.githubusercontent.com/DataStax-Academy/tutorial-create-rest-api/master/img/studio-connection.png" width="500" />

* Create a notebook "REST API"
<img src="https://raw.githubusercontent.com/DataStax-Academy/tutorial-create-rest-api/master/img/studio-notebook.png" width="500" />


* Test Connectivity
```sql
describe keyspaces;
```

* Create KeySpace (sample)
```sql
CREATE KEYSPACE IF NOT EXISTS tuto_rest_api 
WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };
```

* Create Schema (sample)
```sql
// Comments for a given video
CREATE TABLE IF NOT EXISTS comments_by_video (
    videoid uuid,
    commentid timeuuid,
    userid uuid,
    comment text,
    PRIMARY KEY (videoid, commentid)
) WITH CLUSTERING ORDER BY (commentid DESC);

truncate comments_by_video;

// Comments for a given user
CREATE TABLE IF NOT EXISTS comments_by_user (
    userid uuid,
    commentid timeuuid,
    videoid uuid,
    comment text,
    PRIMARY KEY (userid, commentid)
) WITH CLUSTERING ORDER BY (commentid DESC);

truncate comments_by_user;
```

* Create Some Data

```sql
// ------------------------------------------------------------------------
// CREATE 
// ------------------------------------------------------------------------
INSERT INTO comments_by_user (commentid, userid, videoid, comment)
VALUES (1aae5f50-445e-11e8-8977-abaff7c8fa1d, 
		b17e0fa3-62f7-47f6-a47a-552c925d4d79, 
		12b5b195-46d7-492a-a7ec-1909688901da,
		'Video A / Comment A');	
                
INSERT INTO comments_by_user (commentid, userid, videoid, comment)
VALUES (1aae5f50-445e-11e8-8977-abaff7c8fa1e, 
		b17e0fa3-62f7-47f6-a47a-552c925d4d79, 
		12b5b195-46d7-492a-a7ec-1909688901da,
		'Video A / Comment B');
		
INSERT INTO comments_by_user (commentid, userid, videoid, comment)
VALUES (1abe5f50-445e-11e8-8977-abdff7c8fa1e, 
		b17e0fa3-62f7-47f6-a47a-552c925d4d79, 
		13b5b195-46d7-492a-a7ec-1909688901da,
		'Video B / Comment C');
		
// ------------------------------------------------------------------------
// READ
// ------------------------------------------------------------------------
SELECT comment 
FROM comments_by_user 
where userid=b17e0fa3-62f7-47f6-a47a-552c925d4d79;


// ------------------------------------------------------------------------
// UPDATE
// ------------------------------------------------------------------------
INSERT INTO comments_by_user (commentid, userid, videoid, comment)
VALUES (1abe5f50-445e-11e8-8977-abdff7c8fa1e, 
		b17e0fa3-62f7-47f6-a47a-552c925d4d79, 
		13b5b195-46d7-492a-a7ec-1909688901da,
		'C1');

SELECT comment 
FROM comments_by_user 
where userid=b17e0fa3-62f7-47f6-a47a-552c925d4d79 and commentid=1abe5f50-445e-11e8-8977-abdff7c8fa1e;

INSERT INTO comments_by_user (commentid, userid, videoid, comment)
VALUES (1abe5f50-445e-11e8-8977-abdff7c8fa1e, 
		b17e0fa3-62f7-47f6-a47a-552c925d4d79, 
		13b5b195-46d7-492a-a7ec-1909688901da,
		'C2');

UPDATE comments_by_user 
SET comment = 'Hello'  
where userid=b17e0fa3-62f7-47f6-a47a-552c925d4d79 
and commentid=1abe5f50-445e-11e8-8977-abdff7c8fa1e;

// ------------------------------------------------------------------------
// DELETE
// ------------------------------------------------------------------------
DELETE FROM comments_by_user 
where userid=b17e0fa3-62f7-47f6-a47a-552c925d4d79;
```

### Import project

* Browse to project and check everything ok
```
cd tutorial-create-rest-api
mvn clean install
```

* Import the maven project into your environment

* Open class `TestConnection`
* Remove the `@Ignore` annotation.
* execute `checkConnection` test.




