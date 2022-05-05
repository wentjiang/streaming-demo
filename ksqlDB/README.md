# Demo Usage

## Start ksqlDB server

```
docker-compose up
```

## Start ksqlDB CLI

```
docker exec -it ksqldb-cli ksql http://ksqldb-server:8088
```

## Create a stream

```
CREATE STREAM riderLocations (profileId VARCHAR, latitude DOUBLE, longitude DOUBLE)
  WITH (kafka_topic='locations', value_format='json', partitions=1);
```

## Create materialized views

```
CREATE TABLE currentLocation AS
  SELECT profileId,
         LATEST_BY_OFFSET(latitude) AS la,
         LATEST_BY_OFFSET(longitude) AS lo
  FROM riderlocations
  GROUP BY profileId
  EMIT CHANGES;
```

```
CREATE TABLE ridersNearMountainView AS
  SELECT ROUND(GEO_DISTANCE(la, lo, 37.4133, -122.1162), -1) AS distanceInMiles,
         COLLECT_LIST(profileId) AS riders,
         COUNT(*) AS count
  FROM currentLocation
  GROUP BY ROUND(GEO_DISTANCE(la, lo, 37.4133, -122.1162), -1);
```

## Run a push query over the stream

```
-- Mountain View lat, long: 37.4133, -122.1162
SELECT * FROM riderLocations
  WHERE GEO_DISTANCE(latitude, longitude, 37.4133, -122.1162) <= 5 EMIT CHANGES;
```

## Start another CLI session

```
docker exec -it ksqldb-cli ksql http://ksqldb-server:8088
```

## Populate the stream with events

```
INSERT INTO riderLocations (profileId, latitude, longitude) VALUES ('c2309eec', 37.7877, -122.4205);
INSERT INTO riderLocations (profileId, latitude, longitude) VALUES ('18f4ea86', 37.3903, -122.0643);
INSERT INTO riderLocations (profileId, latitude, longitude) VALUES ('4ab5cbad', 37.3952, -122.0813);
INSERT INTO riderLocations (profileId, latitude, longitude) VALUES ('8b6eae59', 37.3944, -122.0813);
INSERT INTO riderLocations (profileId, latitude, longitude) VALUES ('4a7c7b41', 37.4049, -122.0822);
INSERT INTO riderLocations (profileId, latitude, longitude) VALUES ('4ddad000', 37.7857, -122.4011);
```

## Run a Pull query against the materialized view

```
SELECT * from ridersNearMountainView WHERE distanceInMiles <= 10;
```

## Clean all the containers after stop the docker-compose

```
curl https://raw.githubusercontent.com/wentjiang/shell-box/master/tools/clean_docker_containers.sh | bash
```