package SRI;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueriesCorpus {

    public static final String filePath = "src/cisi/trec_solr_file.TREC";
    public static final String EQUIPO = "mague";

    public static List<SolrQuery> readSolrQueries(String filePathQueries) {
        List<SolrQuery> solrQueries = new ArrayList<>();
        String currentLine;
        int currentId = -1;
        StringBuilder currentText = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePathQueries))) {
            currentLine = br.readLine();

            while (currentLine != null) {

                if (currentLine.startsWith(".I")) {
                    if (currentId != -1 && currentText.length() > 0) {
                        solrQueries.add(buildSolrQuery(encodeUTF8(removeSpecialCharacters(currentText.toString().trim()))));
                        currentText = new StringBuilder();
                    }
                    currentId = Integer.parseInt(currentLine.substring(3).trim()); // Extracting the ID
                    currentLine = br.readLine();
                } else if (currentLine.startsWith(".W")) {
                    // Read the text
                    while ((currentLine = br.readLine()) != null && !currentLine.startsWith(".")) {
                        currentText.append(currentLine).append(" ");
                    }
                } else { currentLine = br.readLine(); }
            }

            if (currentId != -1 && currentText.length() > 0) {
                solrQueries.add(buildSolrQuery(encodeUTF8(removeSpecialCharacters(currentText.toString().trim()))));
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return solrQueries;
    }


    public static List<String> executeQueries(String queries_file, String URL_core, String core) throws IOException {

        List<SolrQuery> solrQueries = readSolrQueries(queries_file);

        List<String> results = new ArrayList<>();
        FileWriter fileWriter = new FileWriter(filePath);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        try (HttpSolrClient solrClient = new HttpSolrClient.Builder(URL_core+"/"+core).build()) {

            int i =1;

            for (SolrQuery query : solrQueries) {


                int ranking = 0;

                try {
                    QueryResponse response = solrClient.query(query);

                    for (org.apache.solr.common.SolrDocument document : response.getResults()) {
                        String docId = document.get("id").toString(); // Ajusta según el campo que contiene el identificador del documento
                        float score = (float) document.getFieldValue("score"); // Obtiene el score del documento
                        bufferedWriter.flush();
                        bufferedWriter.write(i + " Q0 " + docId + " " + ranking++ + " " + score + " " + EQUIPO);
                        results.add(i + " Q0 " + docId + " " + ranking + " " + score + " " + EQUIPO);
                        bufferedWriter.newLine();

                    }   i++;

                } catch (SolrServerException | IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;

    }

    private static SolrQuery buildSolrQuery(String text) {

        SolrQuery query = new SolrQuery("text:(" + text + ")");
        query.set("fl", "*,score");

        return query;
    }

    public static String removeSpecialCharacters(String input) {

        String regex = "[^a-zA-Z0-9\\s]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        String result = matcher.replaceAll("");

        result = result.replaceAll("[\\n\\t]", " ");

        result = result.replaceAll("\\s+", " ");

        return result;
    }

    public static String encodeUTF8(String input) {
        try {
            return URLEncoder.encode(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // Manejar la excepción apropiadamente
            e.printStackTrace();
            return null;
        }
    }

}

