CHue [![Build Status](https://travis-ci.org/WISVCH/chue.svg?branch=master)](https://travis-ci.org/WISVCH/chue)
====

## Development
Build using Gradle, `./gradlew build`, or open the project in your favourite IDE. Before running, configure the application using `config/application.properties`.

Create new endpoints in `Webcontroller` by implementing either `HueState` or `HueEvent` and loading it using `HueService`. Events are temporary effects after which the lights will revert to the last known state.

## Production

TeamCity deploys the master branch to https://gadgetlab.chnet/chue/.

Log files can be viewed using `ssh gadgetlab.chnet sudo journalctl -u chue.service`.
