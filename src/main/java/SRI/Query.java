package SRI;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Query {

    public static final int NUM_PALABRAS = 5;
    public static final String filePath= "src/cisi/trec_solr_file.TREC";
    public static final String EQUIPO= "mague";

    // La app lee el fichero de consultas CISI.QRY, toma las primeras 5 palabras y lanza consulta a Apache Solr.
    // Recorremos la respuesta de Solr y la mostramos.

    public void Busqueda(String querys, String URL, String nombre_core) throws IOException, SolrServerException {

        Scanner scan = new Scanner(new File(querys));
        StringBuilder stringBuilder;
        String line;
        line = scan.nextLine();
        SolrInputDocument doc = new SolrInputDocument();
        List<SolrInputDocument> documents = new ArrayList<>();
        int contadorDeConsultas = 1;

        // Create Solr URL
        String solrServerUrl = URL + nombre_core;

        HttpSolrClient solr = new HttpSolrClient.Builder(solrServerUrl).build();

        while (scan.hasNextLine()) {

            stringBuilder = new StringBuilder();

            if (line.startsWith(".I") && scan.hasNextLine()) {
                while (!line.startsWith(".W")) {
                    line = scan.nextLine();
                }
                doc.addField("id", contadorDeConsultas);

                if (line.startsWith(".W")) {
                    line = scan.nextLine();
                    stringBuilder.append(line);
                    line = scan.nextLine();

                    while (scan.hasNextLine() && !line.startsWith(".I")) {
                        stringBuilder.append(" ").append(line);
                        line = scan.nextLine();
                    }
                    doc.addField("W", stringBuilder);

                } else System.out.println("Error, no se encuentra W para dicho ID");
            } else System.out.println("No hay más líneas habiendo ID o viceversa");

            contadorDeConsultas++;
            documents.add(doc);
            doc = new SolrInputDocument();
        }

        FileWriter fileWriter = new FileWriter(filePath);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        for (int i = 0; i < documents.size(); i++) {

            String[] first5Words = documents.get(i).getField("W").getValue().toString().split("\\s+");
            StringBuilder query5 = new StringBuilder();
            String word;
            int num_words = 0;

            for (int j = 0; (num_words < NUM_PALABRAS && j < first5Words.length); j++) {
                word = first5Words[j];
                if (!word.isBlank()) {
                    word = word.replaceAll("[\\(\\)\\[\\].,:_'\"]", "");
                    query5.append(word).append(" ");
                    num_words++;
                }
            }

            SolrQuery query = new SolrQuery("text:" + query5);
            query.set("fl", "*,score"); // Define los campos a devolver
            query.set("defType", "edismax"); // Configura el tipo de consulta (edismax para BM25)
            query.setSort("score", SolrQuery.ORDER.desc); // Ordena por score en orden descendente

            QueryResponse rsp = solr.query(query);
            SolrDocumentList results = rsp.getResults();

            int ranking = 0;



            for (org.apache.solr.common.SolrDocument document : rsp.getResults()) {
                String docId = document.get("id").toString(); // Ajusta según el campo que contiene el identificador del documento
                float score = (float) document.getFieldValue("score"); // Obtiene el score del documento
                bufferedWriter.flush();
                bufferedWriter.write(i + " Q0 " + docId + " " + ranking++ + " " + score + " " + EQUIPO);
                bufferedWriter.newLine();
            }
        }
    }
}
