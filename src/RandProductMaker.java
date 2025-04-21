import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class RandProductMaker extends JFrame {
    private JTextField nameField, descField, idField, costField, countField;
    private RandomAccessFile file;
    private int recordCount = 0;

    public RandProductMaker() {
        super("RandProductMaker");

        try {
            file = new RandomAccessFile("products.dat", "rw");
            recordCount = (int)(file.length() / Product.RECORD_SIZE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "File error: " + e.getMessage());
            System.exit(1);
        }

        nameField = new JTextField(35);
        descField = new JTextField(75);
        idField = new JTextField(6);
        costField = new JTextField();
        countField = new JTextField(String.valueOf(recordCount));
        countField.setEditable(false);

        JButton addBtn = new JButton("Add Product");
        JButton quitBtn = new JButton("Quit");

        addBtn.addActionListener(e -> addProduct());
        quitBtn.addActionListener(e -> {
            try {
                file.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.exit(0);
        });

        setLayout(new GridLayout(7, 2));
        add(new JLabel("Name:")); add(nameField);
        add(new JLabel("Description:")); add(descField);
        add(new JLabel("ID:")); add(idField);
        add(new JLabel("Cost:")); add(costField);
        add(new JLabel("Record Count:")); add(countField);
        add(addBtn); add(quitBtn);

        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void addProduct() {
        try {
            String name = nameField.getText();
            String desc = descField.getText();
            String id = idField.getText();
            double cost = Double.parseDouble(costField.getText());

            if (name.isEmpty() || desc.isEmpty() || id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled.");
                return;
            }

            Product p = new Product(id, name, desc, cost);
            file.seek(file.length());
            p.writeToFile(file);

            recordCount++;
            countField.setText(String.valueOf(recordCount));

            nameField.setText("");
            descField.setText("");
            idField.setText("");
            costField.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding product: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new RandProductMaker();
    }
}
