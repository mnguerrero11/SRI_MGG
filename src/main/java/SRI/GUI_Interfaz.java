package SRI;

import org.apache.solr.client.solrj.SolrServerException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GUI_Interfaz {
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JButton indexarButton;
    private JTextField nombre_core_index;
    private JTextField nombre_puerto_index;
    private JComboBox selectarchivoIndex;
    private JTextField query_buscar;
    private JTextPane results_query_buscar;
    private JButton buscarButton;

    public GUI_Interfaz() {

        selectarchivoIndex.addItem("src/cisi/CISI.ALL");

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