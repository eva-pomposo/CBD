package com.projetoMaven.app;

import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Projections;

import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Aggregates.group;

import com.mongodb.client.model.Sorts;

import java.util.Arrays;

public class AlineaC {
    static MongoCollection<Document> collection;
    
    public static void main(String[] args) {
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {

            MongoDatabase database = mongoClient.getDatabase("cbd");
            collection = database.getCollection("restaurants");

            System.out.println("Encontre os restaurantes que obtiveram uma ou mais pontuações (score) entre [80 e 100]:");
            try {
                FindIterable<Document> docs = collection.find(and(gte("grades.score", 80), lte("grades.score", 100)));
                for (Document d : docs) {
                    System.out.println(d.toJson());
                }
            }catch (Exception e){
                System.err.println("Error reading from MongoDB collection: " + e);
            }

            System.out.println();

            System.out.println("Liste o restaurant_id, o nome, a localidade e a gastronomia dos restaurantes localizados em \"Staten Island\", \"Queens\",  ou \"Brooklyn\":");
            try {
                Bson projection = Projections.fields(Projections.include("restaurant_id"), Projections.include("nome"), Projections.include("localidade"), Projections.include("gastronomia"), Projections.excludeId());
                FindIterable<Document> docs = collection.find(or(eq("localidade", "Staten Island"), eq("localidade", "Queens"), eq("localidade", "Brooklyn"))).projection(projection);

                for (Document d : docs) {
                    System.out.println(d.toJson());
                }
            }catch (Exception e){
                System.err.println("Error reading from MongoDB collection: " + e);
            }

            System.out.println();

            System.out.println("Liste o restaurant_id, o nome, o endereço (address) e as coordenadas geográficas (coord) dos restaurantes onde o 2º elemento da matriz de coordenadas tem um valor superior a 42 e inferior ou igual a 52.:");
            try {
                Bson projection = Projections.fields(Projections.include("restaurant_id"), Projections.include("address"), Projections.include("address.coord"), Projections.include("nome"));
                FindIterable<Document> docs = collection.find(and(gt("address.coord.1", 42), lte("address.coord.1", 52))).projection(projection);

                for (Document d : docs) {
                    System.out.println(d.toJson());
                }
            }catch (Exception e){
                System.err.println("Error reading from MongoDB collection: " + e);
            }

            System.out.println();

            System.out.println("Conte o total de restaurante existentes em cada localidade:");
            try {

                AggregateIterable<Document> docs = collection.aggregate(Arrays.asList(group("$localidade", Accumulators.sum("num", 1))));
                for (Document d : docs) {
                    System.out.println(d.toJson());
                }

            }catch (Exception e){
                System.err.println("Error reading from MongoDB collection: " + e);
            }

            System.out.println();

            System.out.println("Liste nome, localidade, grade e gastronomia de todos os restaurantes localizados em Brooklyn que não incluem gastronomia \"American\" e obtiveram uma classificação  (grade) \"A\". Deve apresentá-los por ordem decrescente de gastronomia:");
            try {
                Bson projection = Projections.fields(Projections.include("nome"), Projections.include("localidade"), Projections.include("grades.grade"), Projections.include("gastronomia"));
                FindIterable<Document> docs = collection.find(and(eq("localidade", "Brooklyn"), ne("gastronomia", "American"), eq("grades.grade", "A"))).projection(projection).sort(Sorts.descending("gastronomia"));

                for (Document d : docs) {
                    System.out.println(d.toJson());
                }
            }catch (Exception e){
                System.err.println("Error reading from MongoDB collection: " + e);
            }

        } catch (Exception e) {
            System.err.println("Error connecting to MongoDB database: " + e);
        }
    }
}
