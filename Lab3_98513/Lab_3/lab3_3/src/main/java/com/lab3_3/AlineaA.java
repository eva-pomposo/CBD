package com.lab3_3;


import java.text.SimpleDateFormat;
import java.util.Date;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class AlineaA {
    
    public static void main(String[] args) {
        try {
            Cluster cluster = Cluster.builder().addContactPoints("127.0.0.1").build();
            Session session = cluster.connect("cbd_3_2");
            ResultSet resultado = null;

            // Inserir um User
            try {   
                session.execute("INSERT INTO User (username, name, email, registration_time_stamp) VALUES ('mariap', 'Maria', 'mariap@ua.pt', '"  + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "');");
            }catch (Exception e){
                System.err.println("Erro ao inserir o User: " + e);
            }
            System.out.println("User mariap inserido!");

            //Verificar a inserçao do User
            try {   
                resultado = session.execute("select * from User;");
            }catch (Exception e){
                System.err.println("Erro ao ler os Users: " + e);
            }
            System.out.println(resultado.all());
            System.out.println();

            //Editar um User
            try {   
                session.execute("UPDATE User SET name='The Scorpoi' WHERE username='pedrosobral';");
            }catch (Exception e){
                System.err.println("Erro ao editar um User: " + e);
            }
            System.out.println("User pedrosobral editado!");

            //Verificar a edicao do User
            try {   
                resultado = session.execute("select * from User;");
            }catch (Exception e){
                System.err.println("Erro ao ler os Users: " + e);
            }
            System.out.println(resultado.all());
            System.out.println();

            //Pesquisar os Videos todos
            System.out.println("--- Videos ---");
            try {   
                resultado = session.execute("select * from Video_author;");
            }catch (Exception e){
                System.err.println("Erro ao ler os Videos: " + e);
            }
            System.out.println(resultado.all());
            System.out.println();

            //Pesquisar os Videos do autor evapomposo
            System.out.println("--- Video do autor evapomposo ---");
            try {   
                resultado = session.execute("select * from Video_author where author_username='evapomposo';");
            }catch (Exception e){
                System.err.println("Erro ao ler os Videos: " + e);
            }
            System.out.println(resultado.all());
            System.out.println();

            session.close();

        } catch (Exception e) {
            System.err.println("Erro ao conectar-se à base de dados Cassandra: " + e);
        }
    }
}