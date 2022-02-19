package com.projetoMaven.app;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;
import java.util.List;

public class AlineaA 
{
    static MongoCollection<Document> collection;
    public static void main( String[] args )
    {

        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {

            MongoDatabase database = mongoClient.getDatabase("cbd");
            collection = database.getCollection("restaurants");

            List<Double> coords = new ArrayList<>(); 
            coords.add(-67.397425);
            coords.add(38.45346);

            List<Document> grades = new ArrayList<>(); 
            grades.add(new Document("date", new Date())
                .append("grade", "B")
                .append("socre", 8));
            
            grades.add(new Document("date", new Date())
                .append("grade", "A")
                .append("socre", 11));

            // Inserir um restaurante
            Document rest = new Document("_id", new ObjectId())
                    .append("address",new Document()
                        .append("building", "150")
                        .append("coord", coords)
                        .append("rua", "Rua Urbanizacao Chave")
                        .append("zipcode", "10373")
                    )
                    .append("localidade", "Aveiro")
                    .append("gastronomia", "Portuguesa")
                    .append("grades", grades)
                    .append("nome", "Cantiflas")
                    .append("restaurant_id", "50000000");

            try {
                collection.insertOne(rest);
            }catch (Exception e){
                System.err.println("Erro ao inserir o restaurente: " + e);
            }
            System.out.println("Restaurante inserido!");

            // Editar um restaurante
            try {
                collection.updateOne(eq("nome", "Cantiflas"),Updates.set("nome","Restaurante da Vila"));
            } catch (Exception e) {
                System.err.println("Erro ao editar o restaurante no MongoDB: " + e);
            }
            System.out.println("Restaurante editado!");

            // Pesquisar restaurantes
            System.out.println("Liste todos os restaurantes que tenham pelo menos um score superior a 85:");
            try {
                FindIterable<Document> docs = collection.find(gte("grades.score", 85));
                for (Document d : docs) {
                    System.out.println(d.toJson());
                }
            }catch (Exception e){
                System.err.println("Error reading from MongoDB collection: " + e);
            }


        } catch (Exception e) {
            System.err.println("Erro ao conectar-se à base de dados MongoDB: " + e);
        }
    }
}
