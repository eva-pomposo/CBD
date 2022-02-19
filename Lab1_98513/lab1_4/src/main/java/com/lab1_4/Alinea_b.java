package com.lab1_4;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import redis.clients.jedis.Jedis;


public class Alinea_b {
    private Jedis jedis;
	public static String USERS_POPULARIDADE = "users_popularidade";
	public static String USERS_ALFABETICA = "users_alfabetica"; 

    public Alinea_b() {
        this.jedis = new Jedis("localhost");
        this.jedis.flushDB();
    }

    public void saveUser(int popularidade, String username) {
        jedis.zadd(USERS_POPULARIDADE, popularidade, username);
        jedis.zadd(USERS_ALFABETICA, 0, username);
	}
	
    public List<String> getUser(String username) {
        Set<String> users_alfabetica = jedis.zrangeByLex(USERS_ALFABETICA, "[" + username, "[" + username + Character.MAX_VALUE);
        Set<String> users_popularidade = jedis.zrevrange(USERS_POPULARIDADE, 0,-1);

        List<String> users = new ArrayList<>();
        for (String name : users_popularidade) {
            if (users_alfabetica.contains(name)) {
                users.add(name);
            }
        }

        return users;
	}
	
	public Set<String> getAllKeys() {
		return jedis.keys("*");
	}
    
    public static void main(String[] args) {
        Alinea_b alinea_b = new Alinea_b();

        try (Scanner input = new Scanner(new FileReader("nomes-pt-2021.csv"))) {
            while (input.hasNextLine()) {
                String[] line = input.nextLine().split(";");
                alinea_b.saveUser(Integer.parseInt(line[1]), line[0]);
            }

        } catch (FileNotFoundException e) {
            System.err.printf("ERRO: %s\n", e.getMessage());
        }


        //alinea_b.getAllKeys().stream().forEach(System.out::println);
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("Search for ('Enter' for quit):");
            String input = sc.nextLine();
            if (input.equals("")) {
                break;
            } else {
                alinea_b.getUser(input).stream().forEach(System.out::println);
            }
        }
        sc.close();
    }
}