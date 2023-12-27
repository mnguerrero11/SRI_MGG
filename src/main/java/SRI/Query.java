package SRI;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Query {

    public static final String filePath = "src/cisi/trec_solr_file.TREC";
    public static final String EQUIPO = "mague";


    public static String encodeUTF8(String input) {
        try {
            return URLEncoder.encode(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // Manejar la excepción apropiadamente
            e.printStackTrace();
            return null;
        }
    }

    public List<SolrDocument> BusquedaCadena(String cadena, String URL, String nombre_core) throws IOException, SolrServerException {

        SolrInputDocument doc = new SolrInputDocument();

        // Create Solr URL
        String solrServerUrl = URL + nombre_core;

        HttpSolrClient solr = new HttpSolrClient.Builder(solrServerUrl).build();



        SolrQuery query = new SolrQuery("text:(" + cadena + ")");
        query.set("fl", "*,score");
        System.out.println("Query: "+query);

        QueryResponse rsp = solr.query(query);
        SolrDocumentList results = rsp.getResults();

        List<SolrDocument> resultados = new ArrayList<>();

        for (org.apache.solr.common.SolrDocument document : rsp.getResults()) {
            resultados.add(document);
        }

        return resultados;

    }

    /*public List<String> Busqueda(String querys, String URL, String nombre_core) throws IOException, SolrServerException {

        Scanner scan = new Scanner(new File(querys));
        StringBuilder stringBuilder;
        String line;
        line = scan.nextLine();
        SolrInputDocument doc = new SolrInputDocument();
        List<SolrInputDocument> documents = new ArrayList<>();
        List<String> resp_string = new ArrayList<>();
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

        for (int i = 1; i < documents.size(); i++) {

            String cadenaCodificada = encodeUTF8(documents.get(i).getField("W").toString());

            SolrQuery query = new SolrQuery("text:(" +cadenaCodificada+ ")");
            query.set("fl", "*,score");
            QueryResponse rsp = solr.query(query);
            SolrDocumentList results = rsp.getResults();

            int ranking = 0;


            for (org.apache.solr.common.SolrDocument document : rsp.getResults()) {
                String docId = document.get("id").toString(); // Ajusta según el campo que contiene el identificador del documento
                float score = (float) document.getFieldValue("score"); // Obtiene el score del documento
                bufferedWriter.flush();
                bufferedWriter.write(i + " Q0 " + docId + " " + ranking++ + " " + score + " " + EQUIPO);
                resp_string.add(i + " Q0 " + docId + " " + ranking + " " + score + " " + EQUIPO);
                bufferedWriter.newLine();
            }
        }
        return resp_string;

    }*/
}
