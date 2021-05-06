# Encodely

Transcode a directory full of files.

## Getting started

You really want to be doing this via `docker-compose`.

```yaml
version: '3.7'
services:

  encodely:
    image: peavers/encodely:latest
    container_name: encodely
    restart: unless-stopped
    ports:
      - "8080:8080"
    volumes:
      - /home/peavers/Movies:/home/peavers/Movies
      - /var/run/docker.sock:/var/run/docker.sock:rw
      - encodely-data:/.data
    logging:
      options:
        max-size: "2m"
        max-file: "5"

volumes:
  encodely-data:
```

The only thing to change here is the first volume entry. Map the working directory where your media files are stored.
You can fine tune this in the settings page of Encodely.

You can safely replace the `encodely-data` with a physical mount if you wish.

## Setting up Encodely

You can access the web UI via `http://localhost:8080`. The settings page should be fairly self-explanatory. Just
remember when you're setting input/output directories they're going to be based on the structure you passed into
the `volumes` section of the `docker-compose`.

## Automatic Jobs

Encodely will detect media files that land inside the input directory and will start transcoding with the default
project.

## Jobs

### Scan Job

After setting your directories, run a `Scan Job` from the `Launch Jobs` menu. This will find all media files in
the `input` directory. You can view those files in `Media files`.

### Transcode Job

If you've scanned some files to transcode, start a transcode job to actually transcode the files. Each transcode job
will start a new docker container with `FFMPEG` values pulled from the specified transcode profile you used when
launching the job.

## Feedback

This is a very early proof of concept and development build. Any additional features or bugs please just raise a GitHub
Issue. I'd love ideas on what people would find useful in terms of functionality.
