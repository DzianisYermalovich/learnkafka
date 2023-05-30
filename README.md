# learnkafka

To build all backend images: `./gradlew bootBuildImage`  
To launch all services: `docker compose up -d`  

Manual testing can be applied via IntelliJ built-in HTTP client [producer/producer-api.http](https://github.com/DzianisYermalovich/learnkafka/blob/producer/producer/producer-api.http)  
To connect to the DB use url: `jdbc:postgresql://localhost:5432/learnkafka` and credentials postgres/postgres.
