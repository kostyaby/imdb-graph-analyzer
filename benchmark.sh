#!/usr/bin/env bash
mvn exec:java -Dexec.mainClass="io.github.kostyaby.client.BenchmarkingClient" -Dexec.args="mongodb://localhost:27017/imdb"