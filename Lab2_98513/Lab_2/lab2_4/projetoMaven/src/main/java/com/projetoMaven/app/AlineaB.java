package com.projetoMaven.app;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;

import org.bson.Document;

import static com.mongodb.client.model.Filters.*;

public class AlineaB {
    static MongoCollection<Document> collection;

    public static void main(String[] args) {

        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {

            MongoDatabase database = mongoClient.getDatabase("cbd");
            collection = database.getCollection("restaurants");

            // Pesquisas sem indices:
            System.out.println("---- Pesquisas sem indices ----");
            System.out.println();
            System.out.println("Liste todos os restaurantes que tenham pelo menos um score superior a 85:");
            long inicio = System.nanoTime();
            try {
                FindIterable<Document> docs = collection.find(gte("grades.score", 85));
                for (Document d : docs) {
                    System.out.println(d.toJson());
                }
            }catch (Exception e){
                System.err.println("Error reading from MongoDB collection: " + e);
            }
            long fim = System.nanoTime();
            System.out.println();
            
            System.out.println("Duracao nas pesquisas sem indices: " + (fim - inicio));
            System.out.println();

            // Pesquisas com indices:
            System.out.println("---- Pesquisas com indices ----");
            System.out.println();

            try {
                collection.createIndex(Indexes.ascending("gastronomia"));
            } catch (Exception e) {
                System.err.println("Erro ao criar um indice ascendente para gastronomia: " + e);
            }

            try {
                collection.createIndex(Indexes.ascending("localidade"));
            } catch (Exception e) {
                System.err.println("Erro ao criar um indice ascendente para localidade: " + e);
            }

            try {
                collection.createIndex(Indexes.text(("nome")));
            } catch (Exception e) {
                System.err.println("Erro ao criar um indice de texto para nome: " + e);
            }

            System.out.println("Liste todos os restaurantes que tenham pelo menos um score superior a 85:");
            inicio = System.nanoTime();
            try {
                FindIterable<Document> docs = collection.find(gte("grades.score", 85));
                for (Document d : docs) {
                    System.out.println(d.toJson());
                }
            }catch (Exception e){
                System.err.println("Error reading from MongoDB collection: " + e);
            }
            fim = System.nanoTime();
            System.out.println();
            System.out.println("Duracao nas pesquisas com indices: " + (fim - inicio));

        } catch (Exception e) {
            System.err.println("Error connecting to MongoDB database: " + e);
        }
    }
}
