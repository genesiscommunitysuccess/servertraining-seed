# servertraining-seed

Initial project for Genesis Application to support [server developer training](https://docs.genesis.global/secure/getting-started/server_training/ssdt-intro/). If you want to see the complete application built during the training use this [repo](https://github.com/genesiscommunitysuccess/servertraining-alpha) as a reference.

# Building and Running
From the command line, cd into the root directory of the project and then follow these steps.

## Build
```shell
./gradlew assemble
```

## Run
* Make sure Docker is running on your machine and run:
```shell
docker-compose build
docker-compose up -d
```

* Attach a terminal to the `gsf` Docker container:
```shell
docker exec -it gsf bash
```
> Alternatively, you can use Docker Desktop Integrated Terminal for the containers you have just created (as explained [here](https://www.docker.com/blog/integrated-terminal-for-running-containers-extended-integration-with-containerd-and-more-in-docker-desktop-4-12/))

* Now try logging in as alpha 
```shell
su - alpha
```

* Check if all services are running:
```shell
mon
```
> Feel free to keep running `mon` until all services are RUNNING.

# Start the training 

Go to the server developer training and follow the [agenda](https://docs.genesis.global/secure/getting-started/server_training/ssdt-intro/#programme). There are 5 sessions or days; each session covers specific advanced resources of the platform. To achieve that, we are extending the Trade & Positions application that was created during the [Developer Training](https://docs.genesis.global/secure/getting-started/developer-training/training-intro/).

# License

This is free and unencumbered software released into the public domain.

For full terms, see [LICENSE](./LICENSE)

**NOTE** This project uses licensed components listed in the next section, thus licenses for those components are required during development.

## Licensed components
Genesis low-code platform
