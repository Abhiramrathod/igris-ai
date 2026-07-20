FROM jenkins/jenkins:lts-jdk17

USER root

RUN apt-get update && \
    apt-get install -y curl gnupg docker.io docker-compose && \
    curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs && \
    curl -fsSL https://archive.apache.org/dist/maven/maven-3/3.9.16/binaries/apache-maven-3.9.16-bin.tar.gz | tar -xz -C /opt && \
    ln -s /opt/apache-maven-3.9.16/bin/mvn /usr/local/bin/mvn && \
    usermod -aG docker jenkins && \
    rm -rf /var/lib/apt/lists/*

RUN node --version && npm --version && mvn --version

USER jenkins

RUN jenkins-plugin-cli --plugins workflow-job workflow-cps workflow-basic-steps workflow-durable-task-step pipeline-stage-view git
