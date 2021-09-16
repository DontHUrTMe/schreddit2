schreddit
===
To log in:
```bash
  docker login registry.k8s.sch.bme.hu
```

To update docker file:
```bash
  ./mvnw clean install
  docker build -t schreddit .
  docker image tag schreddit:latest registry.k8s.sch.bme.hu/schreddit/schreddit:latest
  docker push registry.k8s.sch.bme.hu/schreddit/schreddit:latest
```
