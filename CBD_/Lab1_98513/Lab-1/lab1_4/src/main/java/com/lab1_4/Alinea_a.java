package com.lab1_4;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Set;

import redis.clients.jedis.Jedis;

public class Alinea_a {
    private Jedis jedis;
	public static String USERS = "users"; // Key sorted set for users' name

    public Alinea_a() {
        this.jedis = new Jedis("localhost");
        this.jedis.flushDB();
    }
    public void saveUser(String username) {
        jedis.zadd(USERS, 1, username);
	}
	public Set<String> getUser(String username) {
        return jedis.zrangeByLex(USERS, "[" + username, "[" + username + Character.MAX_VALUE);
	}
	
	public Set<String> getAllKeys() {
		return jedis.keys("*");
	}
    
    public static void main(String[] args) {
        Alinea_a alinea_a = new Alinea_a();

        try (Scanner input = new Scanner(new FileReader("names.txt"))) {
            while (input.hasNextLine()) {
                String string = input.nextLine();
                alinea_a.saveUser(string);
            }

        } catch (FileNotFoundException e) {
            System.err.printf("ERRO: %s\n", e.getMessage());
        }


        //alinea_a.getAllKeys().stream().forEach(System.out::println);
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("Search for ('Enter' for quit):");
            String input = sc.nextLine();
            if (input.equals("")) {
                break;
            } else {
                alinea_a.getUser(input).stream().forEach(System.out::println);
            }
        }
        sc.close();
        
    }
}