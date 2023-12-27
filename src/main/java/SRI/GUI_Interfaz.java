package SRI;

import org.apache.http.conn.HttpHostConnectException;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.CoreAdminParams;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

public class GUI_Interfaz {
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JButton indexarButton;
    private JTextField nombre_puerto_index;
    private JComboBox selectarchivoIndex;
    private JButton buscarCadenaButton;
    private JComboBox selectarchivoQuery;
    private JComboBox selectREL;
    private JButton convertirButton;
    private JLabel label1;
    private JTextField CadenaBuscar;
    private JButton CrearButton;
    private JComboBox comboBoxCores;
    private JList list1;

    public GUI_Interfaz() throws SolrServerException, IOException {

        selectarchivoIndex.addItem("src/cisi/CISI.ALL");
        selectarchivoQuery.addItem("src/cisi/CISI.QRY");
        selectREL.addItem("src/cisi/CISI.REL");


        SolrClient cliente = new HttpSolrClient.Builder("http://localhost:8983/solr").build();


        CoreAdminRequest request = new CoreAdminRequest();
        request.setAction(CoreAdminParams.CoreAdminAction.STATUS);
        CoreAdminResponse cores = request.process(cliente);


        for (int i = 0; i < cores.getCoreStatus().size(); i++) {
            comboBoxCores.addItem(cores.getCoreStatus().getName(i));
        }

        indexarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String nombre_core = comboBoxCores.getSelectedItem().toString();
                String port = nombre_puerto_index.getText();
                String corpusRuta = (String) selectarchivoIndex.getSelectedItem();

                System.out.println("OPCIÓN DE INDEXACIÓN");

                Indexer index = new Indexer();

                try {
                    index.Indexar(corpusRuta, "http://localhost:"+port+"/solr/", nombre_core);
                    JOptionPane.showMessageDialog(null, "Indexado exitoso en "+nombre_core, "Información", JOptionPane.INFORMATION_MESSAGE);
                } catch (org.apache.solr.client.solrj.impl.BaseHttpSolrClient.RemoteSolrException | IOException | SolrServerException e1) {
                    JOptionPane.showMessageDialog(null, "Se ha producido un error: " + e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                }
            }
        });

        CrearButton.addActionListener(new ActionListener() {
            /*@Override
            public void actionPerformed(ActionEvent e) {
                String nombre_core = nombre_core_index.getText();
                String port = nombre_puerto_index.getText();
                String corpusRutaQRY = (String) selectarchivoQuery.getSelectedItem();

                System.out.println("OPCIÓN DE CREAR ARCHIVO PARA EVALUACIÓN");

                Query busqueda = new Query();
                List<String> resp_string = new ArrayList<>();

                try {
                    resp_string = busqueda.Busqueda(corpusRutaQRY, "http://localhost:" + port + "/solr/", nombre_core);

                    // Crear la JList y JScrollPane en la interfaz principal
                    list1 = new JList<>(resp_string.toArray(new String[0]));
                    JScrollPane scrollPane = new JScrollPane(list1);

                    // Mostrar la JList en la interfaz principal
                    JOptionPane.showMessageDialog(null,scrollPane, "Resultados de búsqueda en " + nombre_core, JOptionPane.INFORMATION_MESSAGE);
                } catch (org.apache.solr.client.solrj.impl.BaseHttpSolrClient.RemoteSolrException | IOException | SolrServerException e1) {
                    JOptionPane.showMessageDialog(null, "Se ha producido un error: " + e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                }
            }*/

            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre_core = comboBoxCores.getSelectedItem().toString();
                String port = nombre_puerto_index.getText();
                String corpusRutaQRY = (String) selectarchivoQuery.getSelectedItem();

                System.out.println("OPCIÓN DE CREAR ARCHIVO PARA EVALUACIÓN");

                QueriesCorpus busqueda = new QueriesCorpus();
                List<String> resp_string = new ArrayList<>();

                try {
                    resp_string = busqueda.executeQueries(corpusRutaQRY, "http://localhost:" + port + "/solr/", nombre_core);

                    // Crear la JList y JScrollPane en la interfaz principal
                    list1 = new JList<>(resp_string.toArray(new String[0]));
                    JScrollPane scrollPane = new JScrollPane(list1);

                    // Mostrar la JList en la interfaz principal
                    JOptionPane.showMessageDialog(null,scrollPane, "Resultados de búsqueda en " + nombre_core, JOptionPane.INFORMATION_MESSAGE);
                } catch (org.apache.solr.client.solrj.impl.BaseHttpSolrClient.RemoteSolrException e1) {
                    JOptionPane.showMessageDialog(null, "Se ha producido un error: " + e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        convertirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String rutaREL = (String) selectREL.getSelectedItem();

                System.out.println("OPCION DE CONVERSOR DE REL A TREC PARA EVALUACIÓN");

                RELtoTREC conversor = new RELtoTREC();

                try {
                    conversor.convertRELtoTREC(rutaREL);
                    JOptionPane.showMessageDialog(null, "Archivo generado con éxito.", "Información", JOptionPane.INFORMATION_MESSAGE);

                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "Se ha producido un error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }

            }
        });

        buscarCadenaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre_core = comboBoxCores.getSelectedItem().toString();
                String port = nombre_puerto_index.getText();
                String CadenaBusqueda = CadenaBuscar.getText().toString();

                System.out.println("OPCIÓN DE BUSQUEDA DE CADENA");

                Query busqueda = new Query();

                try {

                    List<SolrDocument> resp_string = new ArrayList<>();


                    resp_string = busqueda.BusquedaCadena(CadenaBusqueda, "http://localhost:" + port + "/solr/", nombre_core);

                    List<SolrDocument> finalResp_string = resp_string;
                    SwingUtilities.invokeLater(() -> {
                        JFrame frame = new JFrame("Documentos Recuperados");
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                        JTextArea textArea = new JTextArea();
                        textArea.setEditable(false);

                        for (SolrDocument document : finalResp_string) {
                            // Aquí asumimos que "title", "author" y "text" son las claves de tus documentos Solr
                            String id = document.getFieldValue("id").toString();
                            String title = document.getFieldValue("title").toString();
                            String author = document.getFieldValue("author").toString();
                            String text = document.getFieldValue("text").toString();

                            textArea.append("[" + id + "]\nTitle: " + title + "\nAuthor: " + author + "\nText: " + text + "\n\n");
                        }

                        JScrollPane scrollPaneResultados = new JScrollPane(textArea);
                        frame.getContentPane().add(scrollPaneResultados, BorderLayout.CENTER);

                        frame.setSize(400, 300);
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);
                        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    });

                } catch (org.apache.solr.client.solrj.impl.BaseHttpSolrClient.RemoteSolrException | IOException | SolrServerException e1) {
                    JOptionPane.showMessageDialog(null, "Se ha producido un error: " + e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                }
            }
        });

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("SRI App [v2.0] | Manuel Guerrero Garcia");
            ImageIcon icon = new ImageIcon(("src/resources/logo.png"));
            frame.setIconImage(icon.getImage());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            try {
                frame.add(new GUI_Interfaz().panel1);
            }  catch (SolrServerException | ConnectException e) {
                System.err.println("Error: No se pudo conectar al servidor Solr. Verifica que el servidor esté en ejecución y la configuración de red.");
                JOptionPane.showMessageDialog(null, "Se ha producido un error de conexión con Solr: " + e.getMessage(), "Error de conexión a Solr", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            frame.pack();
            frame.setVisible(true);
        });
    }
}
