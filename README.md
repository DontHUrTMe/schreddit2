schreddit
===

```bash
  ./mvnw clean install
  docker build -t schreddit .
  docker image tag schreddit:latest registry.k8s.sch.bme.hu/schreddit/schreddit:latest
  docker login registry.k8s.sch.bme.hu
  docker push registry.k8s.sch.bme.hu/schreddit/schreddit:latest
```
