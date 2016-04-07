#!/usr/bin/env bash
mvn exec:java -Dexec.mainClass="io.github.kostyaby.initializer.MongoInitializer" -Dexec.args="/Users/kostya_by/Desktop/coursework/new-data/actors.json \
/Users/kostya_by/Desktop/coursework/new-data/countries.json \
/Users/kostya_by/Desktop/coursework/new-data/directors.json \
/Users/kostya_by/Desktop/coursework/new-data/genres.json \
/Users/kostya_by/Desktop/coursework/new-data/language.json \
/Users/kostya_by/Desktop/coursework/new-data/movies.json \
/Users/kostya_by/Desktop/coursework/new-data/movies2actors.json \
/Users/kostya_by/Desktop/coursework/new-data/movies2directors.json \
/Users/kostya_by/Desktop/coursework/new-data/ratings.json \
mongodb://localhost:27017/imdb"