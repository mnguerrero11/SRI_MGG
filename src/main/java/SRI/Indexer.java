package SRI;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;


public class Indexer {

    public void Indexar(String corpus, String URL, String nombre_core) throws IOException, SolrServerException {


        //Function definitions
        int contadorDocumentos = 1;
        SolrInputDocument doc = new SolrInputDocument();
        List<SolrInputDocument> documents = new ArrayList<>();
        Scanner scan = new Scanner(new File(corpus));
        StringBuilder stringBuilder;
        String line;
        line = scan.nextLine();


        //Create Solr URL
        String solrServerUrl = URL + nombre_core; // Reemplaza con la URL de tu servidor Solr y núcleo


        while (scan.hasNextLine()) {

            stringBuilder = new StringBuilder();

            if (line.startsWith(".I") && scan.hasNextLine()) {
                while (!line.startsWith(".T")) {
                    line = scan.nextLine();
                }
                doc.addField("id", contadorDocumentos);
                stringBuilder = new StringBuilder();
                if (line.startsWith(".T")) {
                    line = scan.nextLine();
                    stringBuilder.append(line);
                    line = scan.nextLine();

                    while (!line.startsWith(".A")) {
                        stringBuilder.append(" ").append(line);
                        line = scan.nextLine();

                    }
                    doc.addField("title", stringBuilder);
                    stringBuilder = new StringBuilder();

                    if (line.startsWith(".A")) {
                        line = scan.nextLine();
                        stringBuilder.append(line);
                        line = scan.nextLine();

                        while (!line.startsWith(".W")) {
                            stringBuilder.append(line);
                            line = scan.nextLine();

                        }
                        doc.addField("author", stringBuilder);
                        stringBuilder = new StringBuilder();

                        if (line.startsWith(".W")) {
                            line = scan.nextLine();
                            stringBuilder.append(line);
                            line = scan.nextLine();

                            while (!line.startsWith(".X")) {
                                stringBuilder.append(line);
                                line = scan.nextLine();

                            }
                            doc.addField("text", stringBuilder);
                            stringBuilder = new StringBuilder();
                            if (line.startsWith(".X")) {
                                line = scan.nextLine();
                                stringBuilder.append(line);
                                line = scan.nextLine();

                                while (scan.hasNextLine() && !line.startsWith(".I")) {
                                    stringBuilder.append(" ").append(line);
                                    line = scan.nextLine();

                                }
                                doc.addField("references", stringBuilder);
                            } else System.out.println("No se encontro referencias para uno de los archivos");
                        } else System.out.println("No se encontro texto para uno de los archivos");
                    } else System.out.println("No se encontro autor para uno de los archivos");
                } else System.out.println("No se encontro titulo para uno de los archivos");

            } else {
                System.out.println("Error al obtener documento");
            }

            contadorDocumentos++;
            documents.add(doc);
            doc = new SolrInputDocument();

        }

        // Crear un cliente Solr
        SolrClient solrClient = new HttpSolrClient.Builder(solrServerUrl).build();

        try {
            // Agregar el documento al índice
            solrClient.add(documents);

            // Enviar los cambios al servidor Solr
            solrClient.commit();

            System.out.println("Documento indexado con éxito en Solr.");
        } catch (SolrServerException | IOException e) {
            System.err.println("Error al indexar el documento en Solr: " + e.getMessage());
        } finally {
            // Cerrar el cliente Solr
            solrClient.close();
        }

    }

}