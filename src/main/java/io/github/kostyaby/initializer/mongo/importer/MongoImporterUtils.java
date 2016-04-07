package io.github.kostyaby.initializer.mongo.importer;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.UpdateOneModel;
import org.bson.Document;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by kostya_by on 4/7/16.
 */
public class MongoImporterUtils {

    public static <T> void insertElements(
            MongoCollection<Document> collection,
            Collection<T> elements,
            Function<T, Document> elementToDocument) {
        collection.bulkWrite(
                elements.stream().map(elementToDocument).map(InsertOneModel::new).collect(Collectors.toList()));
    }

    public static <T> void updateElements(
            MongoCollection<Document> collection,
            Collection<T> elements,
            Function<T, Document> elementToDocument,
            Function<T, Document> elementToReferences) {
        collection.bulkWrite(
                elements.stream()
                        .map(element -> new UpdateOneModel<Document>(
                                elementToDocument.apply(element),
                                new Document("$set", elementToReferences.apply(element))))
                        .collect(Collectors.toList()));
    }
}
