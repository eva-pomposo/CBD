//package lab1_2;

import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Script_names_counting{
    public static void main(String[] args) {
        Map<String, Integer> mapa = new HashMap<>();

        try (Scanner input = new Scanner(new FileReader("names.txt"))) {
            while (input.hasNextLine()) {
                String letter = input.nextLine().substring(0, 1);
                letter = letter.toUpperCase();

                if (mapa.containsKey(letter)) {
                    int count = mapa.get(letter) + 1;
                    mapa.put(letter, count);
                }else{
                    mapa.put(letter, 1);
                }

            }

        } catch (FileNotFoundException e) {
            System.err.printf("ERRO: %s\n", e.getMessage());
        }

        try (PrintWriter out = new PrintWriter(new File("names_counting.txt"))){
            for (String key : mapa.keySet()) {
                out.printf("SET %s %d\n", key, mapa.get(key));
            }
            
            out.close();

        } catch (FileNotFoundException e){
            System.err.printf("ERRO: %s\n", e.getMessage());
        }
    }
}