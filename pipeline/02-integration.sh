#!/usr/bin/env bash

gradle integrationtest
if [ "$CI_BRANCH" != "master" ] && [ "$CI_BRANCH" != "develop" ]  && [ "$CI_PULL_REQUEST" != "" ] ; then
gradlew sonarqube -Dsonar.host.url=SONAR_URL -Dsonar.login=$SONAR_TOKEN -Dsonar.github.pullRequest=$CI_PULL_REQUEST -Dsonar.github.repository=$CI_REPO_NAME -Dsonar.github.oauth=$SONAR_OAUTH -Dsonar.analysis.mode=preview  -Dsonar.branch.name=$CI_BRANCH  -Dsonar.branch.target=develope
fi
