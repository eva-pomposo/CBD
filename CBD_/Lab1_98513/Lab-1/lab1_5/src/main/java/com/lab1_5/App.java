package com.lab1_5;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

import redis.clients.jedis.Jedis;

public class App 
{
	private Jedis jedis;
	public static String USERS = "users"; 
	public static String MESSAGES = "Messages"; 
	public static String FOLLOWING = "Following"; 

    public App() {
        this.jedis = new Jedis("localhost");
    }

    public void saveUser(Scanner sc) {
        System.out.print("Name: "); 
        String username = sc.nextLine();
        boolean flag = jedis.sismember(USERS, username);
        while (flag){
            System.out.println("This name already exists!");
            System.out.print("Name: ");
            username = sc.nextLine();
            flag = jedis.sismember(USERS, username);
        }
		jedis.sadd(USERS, username);
	}

    public void login(App app, Scanner sc){
        System.out.print("Insert your name: ");
        String username = sc.nextLine();
        boolean flag = jedis.sismember(USERS, username);
        while (!flag){
            System.out.println("Name invalid!");
            System.out.print("Insert your name: ");
            username = sc.nextLine();
            flag = jedis.sismember(USERS, username);
        }

        flag = true;
        while (flag) {
            System.out.println();
            System.out.print("Select an option:\n 1 - Follow user\n 2 - Unfollow user \n 3 - Send message\n 4 - Read my messages\n 5 - Read messages from other users\n 6- Logout\n");
            System.out.print("Option: ");
            String input = sc.nextLine();
            System.out.println();
            switch (input) {
                case "1":
                    app.followUser(username, sc);
                    break;
                case "2":
                    app.unfollowUser(username, sc);
                    break;
                case "3":
                    System.out.print("Insert message: ");
                    app.storeMsg(username, sc.nextLine());
                    break;
                case "4":
                    app.readMyMsgs(username, sc);
                    break;
                case "5":
                    app.readMsgs(username, sc);
                    break;
                case "6":
                    flag = false;
                    break;
                default:
                    System.out.println("Option invalid!");
                    break;
            }
            
        }
    }

    public void followUser(String userFollow, Scanner sc){
        Set<String> porSeguir = jedis.sdiff(USERS, userFollow + FOLLOWING);
        porSeguir.remove(userFollow);   //retirar o próprio user pq n pode seguir ele mesmo
        
        if (porSeguir.size() == 0) {
            System.out.println("There are no users to follow");
        } else {
            System.out.println("Users to follow:");
            porSeguir.stream().forEach(System.out::println);
            System.out.println();
            System.out.print("Insert the username that want to follow: ");
            String username = sc.nextLine();
            boolean flag = porSeguir.contains(username);
            while (!flag){
                System.out.println("Name invalid!");
                System.out.print("Insert the username that want to follow: ");
                username = sc.nextLine();
                flag = porSeguir.contains(username);
            }
            jedis.sadd(userFollow + FOLLOWING, username);
            System.out.println("Following " + username + "!");
        }
    }

    public void unfollowUser(String userFollow, Scanner sc){
        Set<String> aSeguir = jedis.smembers(userFollow + FOLLOWING);

        if (aSeguir.size() == 0) {
            System.out.println("There are no users to unfollow");
        } else {
            System.out.println("Following:");
            aSeguir.stream().forEach(System.out::println);
            System.out.println();
            System.out.print("Insert the username that want to unfollow: ");
            String username = sc.nextLine();
            boolean flag = aSeguir.contains(username);
            while (!flag){
                System.out.println("Name invalid!");
                System.out.print("Insert the username that want to unfollow: ");
                username = sc.nextLine();
                flag = aSeguir.contains(username);
            }
            jedis.srem(userFollow + FOLLOWING, username);
            System.out.println("Unfollowing " + username + "!");
        }
    }

    public void storeMsg(String username, String msg){
        jedis.rpush(username + MESSAGES, msg);
    }

    public void readMyMsgs(String username, Scanner sc){
        List<String> msgs = jedis.lrange(username + MESSAGES, 0, -1);
        if (msgs.size() == 0) {
            System.out.println("There are no messages to read!");
        } else {
            System.out.println("My messages:");
            msgs.stream().forEach(System.out::println);
        }
    }

    public void readMsgs(String username, Scanner sc){
        Set<String> aSeguir = jedis.smembers(username + FOLLOWING);

        if (aSeguir.size() == 0) {
            System.out.println("There are no users to read messages!");
        } else {
            boolean haMensagens = false;
            for (String user : aSeguir) {
                List<String> msgs = jedis.lrange(user + MESSAGES, 0, -1);
                
                if (msgs.size() != 0) {
                    haMensagens = true;
                    System.out.println("Messages sent from " + user + ":");
                    msgs.stream().forEach(System.out::println);
                    System.out.println();
                }
            }

            if (!haMensagens) {
                System.out.println("There are no messages to read");
            }
        }
    }

    public static void main( String[] args )
    {
        App app = new App();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.print("Select an option:\n 1 - Create user\n 2 - Login \n 3 - Quit\n");
            System.out.print("Option: ");
            String input = sc.nextLine();
            System.out.println();
            switch (input) {
                case "1":
                    app.saveUser(sc);
                    break;
                case "2":
                    app.login(app, sc);
                    break;
                case "3":
                    System.exit(0);
                default:
                    System.out.println("Option invalid!");
                    break;
            }
            
        }
        //sc.close();
    }
}
