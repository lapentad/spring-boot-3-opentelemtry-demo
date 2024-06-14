#!/bin/bash

# Function to check if a command exists
command_exists () {
    command -v "$1" >/dev/null 2>&1 ;
}

# Check if Java is installed
if ! command_exists java; then
    echo "Java is not installed. Please install Java and try again."
    exit 1
fi

# Check if Docker is installed
if ! command_exists docker; then
    echo "Docker is not installed. Please install Docker and try again."
    exit 1
fi

# Check if Docker Compose is installed
if ! command_exists docker-compose; then
    echo "Docker Compose is not installed. Please install Docker Compose and try again."
    exit 1
fi

# List of subfolders to navigate and run `make build`
subfolders=("notrace" "resttemplate" "webflux")

# Loop through each subfolder and run `make build`
for folder in "${subfolders[@]}"; do
    if [ -d "$folder" ]; then
        echo "Entering $folder"
        cd "$folder" || exit
        if make build; then
            echo "Build successful in $folder"
        else
            echo "Build failed in $folder"
            exit 1
        fi
        cd - || exit
    else
        echo "Directory $folder does not exist"
        exit 1
    fi
done

# Run `docker compose up -d`
echo "Running docker compose up -d"
docker-compose up -d
echo "http://localhost:16686 Jaeger UI"
echo "curl http://localhost:8091/web/notrace"