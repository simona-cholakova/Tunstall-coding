package org.example;
import java.math.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Main {


    public static void main(String[] args) {

        HashMap<String, Integer> encode = new HashMap<>();
        HashMap<Integer, String> decode = new HashMap<>();

        Random rand = new Random(42);
        final int lenOfBiggestCodeWord;
        Pair highestProbPair=null;

        int count = 0;


        String bigBoy = "dddcdddcddddddcdcdcdddddaddddddcddddddddddcddcccddddddddddddddcddddddbdaaddddddddddbdcddddddbdcdbbadcdddaddddddbddddddcdddcdddddddddddddddcddddddbddcdddddabbcdddddbdcdddddddcddcddbddddddddddbdddcddddbdcdddddddcddddadddddddccdddcdddccdcdddddddcddddbdbdbdddddddcddddddddddddddcdbdbbdcddbddbddacddbdddadddddddacbddddddbdbdcadddaddcdddddddddccdddddbdddccdddcddddcddcacdcdbcdddcdbddddddaaddbadccddbddddbdddddddcddddcddaddddbdaddddddadbddddddddcdcddbddcddddddcddcdccdddbddddbddccddddcddddbdadddbddcdddbdddddddbdddbdbcddcddddddddddddddddaddddddddddcddbcdbdddddddddcdbddddccdddcdaddbbadcdddddaddddddddddddddddbddddcddbdddcbbdcbbddddddcdcdcddddddcdddddddcdcdbdddddddddadddddbdddddcddddbcbcaddddddddddcddcdccbddaddcadddddbdddddcdbdadddddddcdddddddcdcdccdbddddcddcbddcdddddddadbddddadddddddddddddddcddcdddcddaddcdddddbbddcddddddcddddcdddcdcdddbddabddddcbbddbdddddddadcdbdbdccddddcbdddddddddddddddaddddcdddddddddddbdcbddcadbcdbccdcddddddddccddddddbddbcddcdddddddadbddddbcaddddacaddcddddcddddbddddbccccddddcdddcdd";

        System.out.printf(bigBoy);

        //initialise the character distribution
        ArrayList <Pair> initial = new ArrayList<>();
        initial.add(new Pair("d",BigDecimal.valueOf(0.7)));
        initial.add(new Pair("c",BigDecimal.valueOf(0.15)));
        initial.add(new Pair("b",BigDecimal.valueOf(0.1)));
        initial.add(new Pair("a",BigDecimal.valueOf(0.05)));

        ArrayList <Pair> codeBook = new ArrayList<>(initial);

        //BUILD CODEBOOK ----------------------------------------------------------------------------------------
        while (codeBook.size() < 16) {
            Pair biggestPair = codeBook.get(0);
            for (Pair p : codeBook) {
                if (p.value.compareTo(biggestPair.value) > 0) {
                    biggestPair = p;
                }
            }
            for (Pair p : initial) {
                Pair newPair = new Pair(biggestPair.key+p.key,p.value.multiply(biggestPair.value));
                codeBook.add(newPair);
            }
                codeBook.remove(biggestPair);
            count++;
        }


        int i=0;
        int tempBiggestCodeword=0;

        BigDecimal temp = null;
        BigDecimal maxValue = null;
        for (Pair p : codeBook) {
            if (tempBiggestCodeword<p.key.length()){ //save the longest codeword
                tempBiggestCodeword=p.key.length();
            }
            if (maxValue == null || p.value.compareTo(maxValue) > 0) { //save the most probable codeword
                maxValue = p.value;
                highestProbPair = new Pair(p.key, p.value);
            }
            encode.put(p.key, i); //actually keep the encoding
            decode.put(i, p.key); //and decoding
            System.out.println("\n "+ i + " "+ p.key +"  "+p.value);
            i++;
        }
        lenOfBiggestCodeWord=tempBiggestCodeword;

        //ENCODE ----------------------------------------------------------------------------------------
        String encodedNum="";
        String encodedBin="";
        count=0;
        int letter=lenOfBiggestCodeWord;
        String test="";
        String dirty = new String(); //dirty chars that remain at the end of the string
        boolean stop = false;

        while (!stop) {
            test = "";  // always start fresh

            for (int j = 0; j <= letter; j++) { //grabs the first 6 -> 5 -> 4 -> 3 -> 2
                if (count + j >= bigBoy.length()) continue; //might be wrong
                test = test + bigBoy.charAt(count + j);     //should get the dd as well
            }

            if (encode.containsKey(test)) {
                encodedNum = encodedNum + encode.get(test)+" ";
                encodedBin= encodedBin + String.format("%4s", Integer.toBinaryString(encode.get(test))).replace(' ', '0') +"";

                count = count + letter + 1;
                letter = lenOfBiggestCodeWord;
            } else {
                letter--;           //goes to negative which implies that codeword for the test doesn't exist
                if (letter < 0) {   //this can only happen at the end so code from !dirty.isEmpty() could go here but it would fire 2ce so call break
                    dirty = dirty + test;
                    count++;
                    letter = 5;
                }
            }
            if (count >= bigBoy.length()) {
                stop = true;
            }
        }

        if (!dirty.isEmpty()){
            System.out.println("There is a dirty bit - letters that dont have a code... Encoding it with "+highestProbPair.key);
            encodedNum = encodedNum + encode.get(highestProbPair.key);
            encodedBin = encodedBin + String.format("%4s", Integer.toBinaryString(encode.get(highestProbPair.key))).replace(' ', '0');
        }

        System.out.println("Encoded Message: ");
        System.out.println(encodedBin);


        //DECODING ----------------------------------------------------------------------------------------
        String decoded="";
        String[] decoding = encodedBin.split("(?<=\\G.{4})"); //splits string by 4

        for (int j = 0; j < decoding.length; j++) {
            decoded= decoded + decode.get(Integer.parseInt(decoding[j], 2));
        }

        if (decoded.length() > bigBoy.length()) {
            decoded = decoded.substring(0, bigBoy.length());
        }

        System.out.println("DECODED Messages : "+ decoded);

        if (decoded.equals(bigBoy)){
            System.out.println("Decoding successful: TRUE");
        }else {
            System.out.println("Decoding successful: FALSE");
        }
    }
}