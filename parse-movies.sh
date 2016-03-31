#!/usr/bin/env bash
mvn exec:java -Dexec.mainClass="io.github.kostyaby.parser.ImdbDataParser" -Dexec.args="/Users/kostya_by/Desktop/coursework/new-data/actors.json \
/Users/kostya_by/Desktop/coursework/new-data/countries.json \
/Users/kostya_by/Desktop/coursework/new-data/directors.json \
/Users/kostya_by/Desktop/coursework/new-data/genres.json \
/Users/kostya_by/Desktop/coursework/new-data/language.json \
/Users/kostya_by/Desktop/coursework/new-data/movies.json \
/Users/kostya_by/Desktop/coursework/new-data/movies2actors.json \
/Users/kostya_by/Desktop/coursework/new-data/movies2directors.json \
/Users/kostya_by/Desktop/coursework/new-data/ratings.json \
/Users/kostya_by/Desktop/coursework/log.txt"