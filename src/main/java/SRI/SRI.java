package SRI;

import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

/**
 *
 * @author mague
 */
public class SRI {

    public static String URL = "http://localhost:8983/solr/"; 
    public static String nombre_core = "core2";
    public static String corpusRuta = "../cisi/cisi.all";

    public static void main(String[] args) throws IOException, SolrServerException, InterruptedException {

        Scanner scanner = new Scanner(System.in);
        int menu;

        do {
            System.out.println("SISTEMA DE RECUPERACIÓN DE INFORMACIÓN");
            System.out.println("1. INDEXAR DOCUMENTO EN SOLR");
            System.out.println("2. CONSULTA MEDIANTE SOLR");
            System.out.println("4. Salir");
            System.out.print("Elige una opción: ");

            menu = scanner.nextInt();

            // Realizar acciones según la opción elegida
            switch (menu) {
                case 1:
                    System.out.println("OPCIÓN DE INDEXACIÓN DE DOCUMENTO A SOLR DESDE FICHERO");
                    
                    try {
                    SolrClient solrClient = new HttpSolrClient.Builder(URL+nombre_core).build();
                    Index.indexador(solrClient, corpusRuta);
                    solrClient.close();
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
