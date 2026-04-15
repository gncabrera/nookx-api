## Development

### IntelliJ Idea

- Go to Settings > Build, Execution > Deployment > Compiler > Shared build process VM options: `-Djps.track.ap.dependencies=false`
- Run configuration > Environment variables: `SPRING_PROFILES_ACTIVE=dev`

## Build

Built with Docker:

- `"./nookx-build-root":/root` volume is used for caching mvn packages for future faster builds

```
docker run -i --rm --name nookx-build -v "./nookx-build-root":/root -v "${PWD}":/build -w /build adoptopenjdk/maven-openjdk17:latest mvn clean package -Pprod -Dmaven.test.skip jib:build
```

## Deployment

```
sudo docker login ghcr.io --username gncabrera --password superMegaPassword
sudo docker pull ghcr.io/gncabrera/nookx:latest
sudo docker compose down
sudo docker compose up -d
```

## Development

### New Mega Assets

Prompt for one Image

```
Add MyEntityImage following the example  @src/main/java/com/nookx/api/domain/ProfileCollection.java with dto, mapper, liquibase migrations, repository

Also add the field MyEntity { MyEntityImage image} so it has only one image
```

Prompt for multiple Images

```
Add MyEntityImage following the example  @src/main/java/com/nookx/api/domain/MegaPartImage.java with dto, mapper, liquibase migrations, repository

```
