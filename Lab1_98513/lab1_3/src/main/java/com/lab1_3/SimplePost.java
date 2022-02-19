package com.lab1_3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import redis.clients.jedis.Jedis;
 
public class SimplePost {
 
	private Jedis jedis;
	public static String USERS = "users"; // Key set for users' name
	public static String USERSLIST = "users_list"; // Key list for users' name
	public static String USERSHASHMAP = "users_hashmap"; // Key hashmap for users' name

	
	public SimplePost() {
		this.jedis = new Jedis("localhost");
	}
 
	public void saveUser(String username) {
		jedis.sadd(USERS, username);
	}
	public Set<String> getUser() {
		return jedis.smembers(USERS);
	}
	
	public Set<String> getAllKeys() {
		return jedis.keys("*");
	}

	public void saveUser_list(String username) {
		jedis.rpush(USERSLIST, username);
	}
	public List<String> getUser_list() {
		return jedis.lrange(USERSLIST, 0, -1);
	}

	public void saveUser_hashmap(Map<String, String> usernames) {
		jedis.hmset(USERSHASHMAP, usernames);
	}
	public Map<String, String> getUser_hashmap() {
		return jedis.hgetAll(USERSHASHMAP);
	}
 
	public static void main(String[] args) {
		SimplePost board = new SimplePost();

		// set some users
		String[] users = { "Ana", "Pedro", "Maria", "Luis" };
		for (String user: users) 
			board.saveUser(user);
		board.getAllKeys().stream().forEach(System.out::println);
		board.getUser().stream().forEach(System.out::println);

		System.out.println();

		//list some users
		List<String> users_list = new ArrayList<>();
		users_list.add("Ana");
		users_list.add("Pedro");
		users_list.add("Maria");
		users_list.add("Luis");
		for (String user: users_list) 
			board.saveUser_list(user);
		board.getAllKeys().stream().forEach(System.out::println);
		board.getUser_list().stream().forEach(System.out::println);

		System.out.println();
		
		//map some users
		Map<String, String> users_hashmap = new HashMap<>();
		users_hashmap.put("User1", "Ana");
		users_hashmap.put("User2", "Pedro");
		users_hashmap.put("User3", "Maria");
		users_hashmap.put("User4", "Luis");
		board.saveUser_hashmap(users_hashmap);
		board.getAllKeys().stream().forEach(System.out::println);
		Map<String, String> m = board.getUser_hashmap();
        for (String key: m.keySet()) {
            System.out.println(m.get(key));
        }
	}
}



