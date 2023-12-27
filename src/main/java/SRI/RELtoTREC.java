package SRI;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class RELtoTREC {

    public static int num_doc_corpus = 1460;
    public static int num_querys = 111;

    public static String filePath = "src/cisi/CISI_REL.TREC";

    public void convertRELtoTREC(String ruta) throws FileNotFoundException {

        ArrayList <String[]> read = new ArrayList<>();

        try (Scanner scanQREL = new Scanner(new File(ruta))) {

            String line = scanQREL.nextLine();
            int num_reads = 0;
            boolean end = false;

            while (!line.isEmpty() && end == false ){
                String[] parts = line.split("\\s+");
                read.add(parts);
                num_reads++;

                if(scanQREL.hasNextLine()) line = scanQREL.nextLine();
                else end = true;
            }

            int count_reads = 0;

            System.out.println(num_reads);

            FileWriter fileWriter = new FileWriter(filePath);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);



            for(int i = 1; i <= num_querys; i++){


                for(int j = 1; j <= num_doc_corpus; j++){

                    if(count_reads < num_reads && (Integer.parseInt(read.get(count_reads)[1]) == i &&  Integer.parseInt(read.get(count_reads)[2]) == j )){
                        bufferedWriter.flush();
                        bufferedWriter.write(i + " 0 " + j +" 1");
                        bufferedWriter.newLine();
                        count_reads++;
                    }
                    else {

                        bufferedWriter.flush();
                        bufferedWriter.write(i + " 0 " + j +" 0");
                        bufferedWriter.newLine();
                    }
                }
            }

            bufferedWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}