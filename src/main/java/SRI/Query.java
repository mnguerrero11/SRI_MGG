package SRI;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Query {

    public static final int NUM_PALABRAS = 5;

    //La app lee el fichero de consultas CISI.QRY, toma las primeras 5 palabras y lanza consulta a Apache Solr.
    // Recorremos la respuesta de Solr y la mostramos.

    public void Busqueda(String querys, String URL, String nombre_core) throws IOException, SolrServerException {

        //Function definitions

        //SolrInputDocument doc = new SolrInputDocument();
        //List<SolrInputDocument> documents = new ArrayList<>();
        Scanner scan = new Scanner(new File(querys));
        StringBuilder stringBuilder;
        String line;
        line = scan.nextLine();
        SolrInputDocument doc = new SolrInputDocument();
        List<SolrInputDocument> documents = new ArrayList<>();
        int contadorDeConsultas = 1;


        //Create Solr URL
        String solrServerUrl = URL + nombre_core;


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
            } else System.out.println("No hay más lineas habiendo ID o viceversa");

            contadorDeConsultas++;
            //System.out.println(doc);
            documents.add(doc);
            doc = new SolrInputDocument();

        }

        HttpSolrClient solr = new HttpSolrClient.Builder(solrServerUrl).build();

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
            //System.out.println(i+" - "+query5);

            SolrQuery query = new SolrQuery();
            query.setQuery("text:" + query5.toString());
            QueryResponse rsp = solr.query(query);


            for (org.apache.solr.common.SolrDocument document : rsp.getResults()) {
                String docId = document.get("id").toString(); // Ajusta según el campo que contiene el identificador del documento
                //float score = document. // Ajusta según el campo que contiene el puntaje
                System.out.println(i + "\t" + docId + " 0 ");
                System.out.println(document.getFieldNames());

            }

        }

    }

}
