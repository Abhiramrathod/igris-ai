FROM jenkins/jenkins:lts-jdk17

USER root

RUN apt-get update && \
    apt-get install -y curl gnupg docker.io docker-compose && \
    rm -rf /var/lib/apt/lists/*

USER jenkins

RUN jenkins-plugin-cli --plugins workflow-job workflow-cps workflow-agents pipeline-model-definition pipeline-model-extensions pipeline-input-step pipeline-stage-view git
