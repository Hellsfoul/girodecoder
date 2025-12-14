# GiroDecoder

This project is a web app, that takes images of GiroCode QR codes (EPC-QR codes) and displays the information in a structured way. 
The backend is made with Spring Boot 4.
The frontend uses vite and vue 3.

The code was completely done with vibe coding using Claude in VS Code.


## backend

to run the backend, first go to the backend folder

```bat 
cd backend
```

just run in backend folder

```bat 
mvn spring-boot:run
```

## frontend

to run the frontend, first go to the frontend folder

```bat
cd frontend
```

install packages

```bat
npm install
```

just run in frontend folder

```bat
npm run dev
```

## run with compose locally

You can use the build.bat in backend and frontend folder to build the docker images.<br>
Docker Desktop and Docker Engine must be running to perform that task.<br>
Afterwards you can see the images in Docker Desktop Images tab.

Then you can use "run compose.bat" to create and run the containers.

## how the app looks

![Image of the app](appimage.png "Image of the app")

## how to push to a custom registry

To build your images either you already used the build.bat files in backend/fronend folders or here are the two commands:

```bat
docker build -t hellsfoul/girodecoder-backend -f Dockerfile .
docker build -t hellsfoul/girodecoder-frontend -f Dockerfile .
```

If needed, use "docker login" to login to your custom registry first.

```bat
docker login https://forgejo.my-registry.noip.net
```
Docker will ask für Username and Password if needed. (The registry must use a certificate. Otherwise the login will fail.)

Then you have to add the correct tags to the images. Docker must know, where to push the images and "hellsfoul/girodecoder-backend" would make Docker believe, you want to push to Docker Hub, which is the default registry.
If e.g. forgejo.my-registry.noip.net is your custom registry, add these tags:

```bat
docker tag hellsfoul/girodecoder-backend:latest forgejo.my-registry.noip.net/hellsfoul/girodecoder-backend:latest
docker tag hellsfoul/girodecoder-frontend:latest forgejo.my-registry.noip.net/hellsfoul/girodecoder-frontend:latest
```

Now you are ready to push your images to your custom registry:

```bat
docker push forgejo.my-registry.noip.net/hellsfoul/girodecoder-backend:latest
docker push forgejo.my-registry.noip.net/hellsfoul/girodecoder-frontend:latest
```

The images should now be published to your registry and the compose file to run the application should looks something like this:

```
services:
  backend:
    image: forgejo.my-registry.noip.net/hellsfoul/girodecoder-backend
    ports:
      - "8082:8080"
    networks:
      - girodecoder-network

  frontend:
    image: forgejo.my-registry.noip.net/hellsfoul/girodecoder-frontend
    ports:
      - "3003:80"
    depends_on:
      - backend
    networks:
      - girodecoder-network

networks:
  girodecoder-network:
    driver: bridge
```

________________________
________________________

### how to make docker login to use http for a home network registry (attention: insecure)

When deploying to a registry in your home network, which does not use https, docker login will fail, as https is obligatory.

You can change this in Docker Desktop under: Settings -> Docker Engine
add e.g.:

```json
{
  "insecure-registries": [
    "192.168.1.123:3000"
  ]
}
```