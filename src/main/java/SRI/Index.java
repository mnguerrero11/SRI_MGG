package SRI;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import org.apache.solr.client.solrj.response.UpdateResponse;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author mague
 */
public class Index {

    public static void indexador(SolrClient solr, String corpus) throws IOException, SolrServerException, InterruptedException {

        int contadorDocumentos = 1;
        SolrInputDocument doc = new SolrInputDocument();
        Scanner scan = new Scanner(new File(corpus));
        StringBuilder stringBuilder;
        String line;
        line = scan.nextLine();

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
                            }else System.out.println("No se encontro referencias para uno de los archivos");
                        }else System.out.println("No se encontro texto para uno de los archivos");
                    }else System.out.println("No se encontro autor para uno de los archivos");
                }else System.out.println("No se encontro titulo para uno de los archivos");

            }else {
                System.out.println("Error al obtener documento");
            }
            //Indexar documento en Solr
            
            solr.add("core2", doc);

            // Realizar la operación de commit para aplicar los cambios
            solr.commit();
            doc = new SolrInputDocument();
            contadorDocumentos++;

        }
    }
}
