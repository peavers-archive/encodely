# Build

## Dockerfile
Encodely needs Java 15 and Docker to be installed. For portability this is managed through the `Dockerfile`. The container
is very light and built a standard Java alpine image. 

### Building the base image
```bash
docker build -t peavers/encodely-base . 
```

### Release the base image
```bash
docker push peavers/encodely-base 
```
