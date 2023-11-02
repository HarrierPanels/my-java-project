[![CICD](https://img.shields.io/badge/HarrierPanels-CI%2FCD-blue)](./)
[![EPAM](https://img.shields.io/badge/Cloud&DevOps%20UA%20Lab%202nd%20Path-Jenkins%20Task-orange)](./)
[![HitCount](https://hits.dwyl.com/HarrierPanels/my-java-project.svg?style=flat&show=unique)](http://hits.dwyl.com/HarrierPanels/my-java-project)
<br>
## Create a CI pipeline:
1. Looks at the main branch of the repository (main)
2. Triggered by merge into main
3. Clones the repository
4. Running static code analysis, Bugs, Vulnerabilities, Security Hotspots, Code Smells are available on the SonarQube server
5. Build a Docker image
6. Tags the image 2 times (latest and build version)
7. Push the image to Docker Hub
