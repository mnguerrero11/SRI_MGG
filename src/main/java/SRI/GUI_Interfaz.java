package SRI;

import org.apache.solr.client.solrj.SolrServerException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUI_Interfaz {
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JButton indexarButton;
    private JTextField nombre_core_index;
    private JTextField nombre_puerto_index;
    private JComboBox selectarchivoIndex;
    private JButton buscarButton;
    private JComboBox selectarchivoQuery;
    private JComboBox selectREL;
    private JButton convertirButton;
    private JLabel label1;
    private JList list1;

    public GUI_Interfaz() {

        selectarchivoIndex.addItem("src/cisi/CISI.ALL");
        selectarchivoQuery.addItem("src/cisi/CISI.QRY");
        selectREL.addItem("src/cisi/CISI.REL");

        indexarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String nombre_core = nombre_core_index.getText();
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

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre_core = nombre_core_index.getText();
                String port = nombre_puerto_index.getText();
                String corpusRutaQRY = (String) selectarchivoQuery.getSelectedItem();

                System.out.println("OPCIÓN DE BUSQUEDA");

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


    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("SRI App [v1.0]");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new GUI_Interfaz().panel1);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
