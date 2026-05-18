package org.example;
import java.math.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Main {


    public static void main(String[] args) {

        HashMap<String, Integer> encode = new HashMap<>();
        HashMap<Integer, String> decode = new HashMap<>();

        Random rand = new Random();
        final int lenOfBiggestCodeWord;
        Pair highestProbPair=null;

        int count = 0;
        String bigBoy ="";

        for (int i = 0; i < 1000; i++) {
            int a = rand.nextInt(101);
            if (a<70){
                bigBoy = bigBoy.concat("d");
            }else if (a<85){
                bigBoy=bigBoy.concat("c");
            }else if (a<95){
                bigBoy=bigBoy.concat("b");
            }else{
                bigBoy=bigBoy.concat("a");
            }
        }


        System.out.println("\n================ GENERATED SOURCE SEQUENCE ================\n");
        System.out.println(bigBoy);
        System.out.println("\n Sequence length: "+bigBoy.length());

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

        int tempBiggestCodeword=0;
        BigDecimal temp = null;
        BigDecimal maxValue = null;
        System.out.println("\n================ TUNSTALL DICTIONARY ================\n");
        System.out.printf("%-8s %-12s %-20s %-15s%n", "Index", "Phrase", "Probability", "Codeword (binary)");
        System.out.println("----------------------------------------------------");
        for (int i=0; i<codeBook.size(); i++) {
            Pair p = codeBook.get(i);

            if (tempBiggestCodeword<p.key.length()){ //save the longest codeword
                tempBiggestCodeword=p.key.length();
            }
            if (maxValue == null || p.value.compareTo(maxValue) > 0) { //save the most probable codeword
                maxValue = p.value;
                highestProbPair = new Pair(p.key, p.value);
            }
            encode.put(p.key, i); //actually keep the encoding
            decode.put(i, p.key); //and decoding
            String binary = String.format("%4s", Integer.toBinaryString(i)).replace(' ', '0');  // ← same as encode loop
            System.out.printf("%-8d %-12s %-20s %-15s%n",
                                i,
                                p.key,
                                p.value,
                                binary);
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

                    dirty = bigBoy.substring(count);
                    System.out.println("\n Phrase: \""+ dirty + "\" is not in the dictionary so we will be encoding it with highest probability phrase: " + highestProbPair.key);
                    encodedNum = encodedNum + encode.get(highestProbPair.key);
                    encodedBin = encodedBin + String.format("%4s", Integer.toBinaryString(encode.get(highestProbPair.key))).replace(' ', '0');
                    stop=true;
                }
            }
            if (count >= bigBoy.length()) {
                stop = true;
            }
        }

        System.out.println();
        System.out.println("================ ENCODED MESSAGE ================\n");
        System.out.println(encodedBin);


        //DECODING ----------------------------------------------------------------------------------------
        String decoded="";
        String[] decoding = encodedBin.split("(?<=\\G.{4})"); //splits string by 4

        for (int j = 0; j < decoding.length; j++) {
            decoded= decoded + decode.get(Integer.parseInt(decoding[j], 2));
        }

        if (decoded.length() > bigBoy.length()) { //this means we had the dirty bit which was encoded with more letters
            decoded = decoded.substring(0, bigBoy.length());
        }

        System.out.println();
        System.out.println("================ DECODED MESSAGE ================\n");
        System.out.println(decoded);

        if (decoded.equals(bigBoy)){
            System.out.println("\nDecoding successful: TRUE");
        }else {
            System.out.println("Decoding successful: FALSE");
        }


        //TUNSTALL STATISTICS ---------------------------------------------------------
        double averagePhraseLength = 0.0;
        for (Pair entry : codeBook) {                       // dictionary → codeBook
            averagePhraseLength +=
                    entry.value.doubleValue()               // getValue() → .value
                            * entry.key.length();           // getKey()   → .key
        }
        double averageBitsPerSymbol = 4.0 / averagePhraseLength;

        System.out.println("\n================ TUNSTALL STATISTICS ================\n");

        System.out.printf("%-35s %.4f symbols%n", "Average phrase length:", averagePhraseLength);
        System.out.printf("%-35s %.4f bits/symbol%n", "Average encoded length:", averageBitsPerSymbol);

        double entropy = 0.0;

        for (Pair e : initial) {                            // alphabetAndProbabilities → initial
            double p = e.value.doubleValue();               // getValue() → .value
            entropy -= p * (Math.log(p) / Math.log(2));
        }
        System.out.printf("%-35s %.4f bits/symbol%n", "Source entropy:", entropy);
        System.out.printf("%-35s %.2f%%", "Tunstall efficiency:", (entropy / averageBitsPerSymbol) * 100);
        System.out.println();
        huffmanComparison(bigBoy);
    }

    public static void huffmanComparison(String sequence) {
        // Huffman codes using YOUR structures ---------------------------------------------------------
        HashMap<String, Integer> huffmanEncode = new HashMap<>();
        HashMap<Integer, String> huffmanDecode = new HashMap<>();

        // Manual Huffman tree for {d=0.70, c=0.15, b=0.10, a=0.05}
        // d→0, c→10, b→110, a→111  (stored as bit-length; decode via lookup)
        huffmanEncode.put("d",  0);   // code "0"    → index 0
        huffmanEncode.put("c",  1);   // code "10"   → index 1
        huffmanEncode.put("b",  2);   // code "110"  → index 2
        huffmanEncode.put("a",  3);   // code "111"  → index 3

        huffmanDecode.put(0, "0");    // actual bit-string stored in decode map
        huffmanDecode.put(1, "10");
        huffmanDecode.put(2, "110");
        huffmanDecode.put(3, "111");

        // Alphabet using YOUR Pair + ArrayList ---------------------------------------------------------
        ArrayList<Pair> alphabetAndProbabilities = new ArrayList<>();
        alphabetAndProbabilities.add(new Pair("d", BigDecimal.valueOf(0.70)));
        alphabetAndProbabilities.add(new Pair("c", BigDecimal.valueOf(0.15)));
        alphabetAndProbabilities.add(new Pair("b", BigDecimal.valueOf(0.10)));
        alphabetAndProbabilities.add(new Pair("a", BigDecimal.valueOf(0.05)));

        //Average code length ---------------------------------------------------------
        double expectedLength = 0;
        for (Pair p : alphabetAndProbabilities) {
            // bit-string length = huffmanDecode.get( huffmanEncode.get(symbol) ).length()
            int codeLen = huffmanDecode.get(huffmanEncode.get(p.key)).length();
            expectedLength += p.value.doubleValue() * codeLen;
        }

        System.out.println("\n================ HUFFMAN COMPARISON ================\n");
        System.out.printf("%-35s %.4f bits/symbol%n",
                "Huffman average length:", expectedLength);

        // Encoded bit count ---------------------------------------------------------
        int totalHuffBits = 0;
        for (char ch : sequence.toCharArray()) {
            Integer idx = huffmanEncode.get(String.valueOf(ch));
            if (idx != null) {
                totalHuffBits += huffmanDecode.get(idx).length();
            }
        }

        double originalBits = sequence.length() * 8.0;
        System.out.printf("%-35s %d bits%n", "Huffman encoded length:", totalHuffBits);
        System.out.printf("%-35s %.2f%%%n", "Huffman compression vs ASCII:", (1.0 - totalHuffBits / originalBits) * 100);

        //Entropy ---------------------------------------------------------
        double entropy = 0;
        for (Pair p : alphabetAndProbabilities) {
            double prob = p.value.doubleValue();
            entropy -= prob * (Math.log(prob) / Math.log(2));
        }

        System.out.printf("%-35s %.2f%%%n", "Huffman efficiency:", (entropy / expectedLength) * 100);

        System.out.println("\n====================================================");
    }
}