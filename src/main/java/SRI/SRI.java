package SRI;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.Scanner;

public class SRI {


    public static String URL = "http://localhost:8983/solr/";
    public static String nombre_core = "core2";
    public static String corpusRuta = "src/cisi/CISI.ALL";

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        int menu;

        do {
            System.out.println("SISTEMA DE RECUPERACIÓN DE INFORMACIÓN CON URL: "+URL+nombre_core);
            System.out.println("1. INDEXAR DOCUMENTO EN SOLR");
            System.out.println("2. CONSULTA MEDIANTE SOLR");
            System.out.println("4. Salir");
            System.out.print("Elige una opción: ");

            menu = scanner.nextInt();

            // Realizar acciones según la opción elegida
            switch (menu) {
                case 1:
                    System.out.println("OPCIÓN DE INDEXACIÓN DE DOCUMENTO A SOLR DESDE FICHERO");

                    Indexer index = new Indexer();

                    try {
                        index.Indexar(corpusRuta, URL, nombre_core);
                    } catch (IOException | SolrServerException e) {
                        e.printStackTrace();
                    }
                    break;

                case 2:
                    System.out.println("OPCIÓN DE CONSULTA");
                    break;
                case 3:
                    System.out.println("...");
                    break;
                case 4:
                    System.out.println("Cerrando programa.");
                    break;
                default:
                    System.out.println("Opción no válida. Inténtalo de nuevo.");
            }

        } while (menu != 4);

        scanner.close();
    }

}
