package com.lab3_3;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class AlineaB {
    public static void main(String[] args) {
        try {
            Cluster cluster = Cluster.builder().addContactPoints("127.0.0.1").build();
            Session session = cluster.connect("cbd_3_2");
            ResultSet resultado = null;

            // Vídeos partilhados por determinado utilizador (maria1987, por exemplo) num determinado período de tempo (Agosto de 2017, por exemplo); 
            System.out.println("---  Vídeos partilhados por o utilizador evapomposo antes de: 2021-12-20 19:57:29 ---");
            try {   
                resultado = session.execute("select * from Video_author where author_username = 'evapomposo' and upload_time_stamp < '2021-12-20 19:57:29';");
            }catch (Exception e){
                System.err.println("Erro ao ler os Videos: " + e);
            }
            System.out.println(resultado.all());
            System.out.println();

            //Os últimos 5 eventos de determinado vídeo realizados por um utilizador
            System.out.println("--- Os últimos 5 eventos do vídeo com id 2 realizados por martaf ---");
            try {   
                resultado = session.execute(" select * from Event_Video where video_id = 2 and author_username = 'martaf' limit 5;");
            }catch (Exception e){
                System.err.println("Erro ao ler os Videos: " + e);
            }
            System.out.println(resultado.all());
            System.out.println();

            // Lista das tags de determinado vídeo
            System.out.println("--- Lista das tags do video com id 2 ---");
            try {   
                resultado = session.execute("select tags from Video_idVideo where id = 2;");
            }catch (Exception e){
                System.err.println("Erro ao ler os Videos: " + e);
            }
            System.out.println(resultado.all());
            System.out.println();

            // Os últimos 3 comentários introduzidos para um vídeo
            System.out.println("--- Os últimos 3 comentários introduzidos do video com id 1 ---");
            try {   
                resultado = session.execute("select * from Comment_Video where video_id=1 Limit 3;");
            }catch (Exception e){
                System.err.println("Erro ao ler os Videos: " + e);
            }
            System.out.println(resultado.all());

            session.close();

        } catch (Exception e) {
            System.err.println("Erro ao conectar-se à base de dados Cassandra: " + e);
        }
    }
}
