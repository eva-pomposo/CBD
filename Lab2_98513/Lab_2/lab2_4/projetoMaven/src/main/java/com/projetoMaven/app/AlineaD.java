package com.projetoMaven.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;

import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Filters.*;

import org.bson.Document;

public class AlineaD {
    static MongoCollection<Document> collection;

    public static void main(String[] args) {

        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {

            MongoDatabase database = mongoClient.getDatabase("cbd");
            collection = database.getCollection("restaurants");

            AlineaD alineaD = new AlineaD();

            try (PrintWriter out = new PrintWriter(new File("CBD_L204_98513.txt"))) {

                out.println("Numero de localidades distintas: " + alineaD.countLocalidades());
    
                out.println();
                out.println("Numero de restaurantes por localidade:");
                Map<String, Integer> restByLocalidade = alineaD.countRestByLocalidade();
                for (String key : restByLocalidade.keySet()) {
                    out.println("-> " + key + " - " + restByLocalidade.get(key));
                }
    
                out.println();
                out.println("Nome de restaurantes contendo 'Park' no nome:");
                List<String> lista= alineaD.getRestWithNameCloserTo("Park");
                for (String restaurante : lista) {
                    out.println("-> " + restaurante);
                }
    
                out.close();
            } catch (FileNotFoundException e) {
                System.err.printf("ERRO: %s\n", e.getMessage());
            }

           
        } catch (Exception e) {
            System.err.println("Error connecting to MongoDB database: " + e);
        }
    }

    public int countLocalidades() {
        int count = 0;
        try {
            AggregateIterable<Document> docs = collection.aggregate(Arrays.asList(group("$localidade")));

            for (Document d : docs) {
                count++;
            }

        }catch (Exception e){
            System.err.println("Error reading from MongoDB collection: " + e);
        }
        return count;
    }

    Map<String, Integer> countRestByLocalidade(){
        Map<String, Integer> mapa = new HashMap<>();
        try {
            AggregateIterable<Document> docs = collection.aggregate(Arrays.asList(group("$localidade", Accumulators.sum("num", 1))));
            for (Document d : docs) {
                mapa.put(d.get("_id").toString(), (int) (d.get("num")));
            }

        }catch (Exception e){
            System.err.println("Error reading from MongoDB collection: " + e);
        }
        return mapa;
    }

    List<String> getRestWithNameCloserTo(String name) {
        List<String> lst = new ArrayList<String>();
        try {
            FindIterable<Document> docs = collection.find(regex("nome", String.format("(%s)", name))); 
            for (Document d : docs) {
                lst.add((String) d.get("nome"));
            }

        }catch (Exception e){
            System.err.println("Error reading from MongoDB collection: " + e);
        }
        return lst;
    }
}
