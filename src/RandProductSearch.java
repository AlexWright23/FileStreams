import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class RandProductSearch extends JFrame {
    private JTextField searchField;
    private JTextArea resultArea;
    private RandomAccessFile file;

    public RandProductSearch() {
        super("RandProductSearch");

        try {
            file = new RandomAccessFile("products.dat", "r");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "File error: " + e.getMessage());
            System.exit(1);
        }

        searchField = new JTextField(20);
        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        JButton searchBtn = new JButton("Search");
        JButton quitBtn = new JButton("Quit");

        searchBtn.addActionListener(e -> searchProducts());
        quitBtn.addActionListener(e -> {
            try {
                file.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.exit(0);
        });

        JPanel panel = new JPanel();
        panel.add(new JLabel("Enter partial name:"));
        panel.add(searchField);
        panel.add(searchBtn);
        panel.add(quitBtn);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        setSize(500, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void searchProducts() {
        String search = searchField.getText().trim().toLowerCase();
        resultArea.setText("");

        try {
            file.seek(0);
            long numRecords = file.length() / Product.RECORD_SIZE;

            for (int i = 0; i < numRecords; i++) {
                file.seek(i * Product.RECORD_SIZE);
                Product p = Product.readFromFile(file);

                if (p.getName().toLowerCase().contains(search)) {
                    resultArea.append(p.toString() + "\n");
                }
            }

        } catch (IOException e) {
            resultArea.setText("Search error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new RandProductSearch();
    }
}
