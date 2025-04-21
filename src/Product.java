import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

public class Product {
    private String ID;
    private String name;
    private String description;
    private double cost;

    public static final int NAME_LENGTH = 35;
    public static final int DESC_LENGTH = 75;
    public static final int ID_LENGTH = 6;
    public static final int RECORD_SIZE = NAME_LENGTH + DESC_LENGTH + ID_LENGTH + 8; // 8 bytes for double

    public Product(String ID, String name, String description, double cost) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.cost = cost;
    }

    public String getID() { return ID; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getCost() { return cost; }

    public void writeToFile(RandomAccessFile file) throws IOException {
        file.write(fixedLength(name, NAME_LENGTH).getBytes(StandardCharsets.UTF_8));
        file.write(fixedLength(description, DESC_LENGTH).getBytes(StandardCharsets.UTF_8));
        file.write(fixedLength(ID, ID_LENGTH).getBytes(StandardCharsets.UTF_8));
        file.writeDouble(cost);
    }

    public static Product readFromFile(RandomAccessFile file) throws IOException {
        byte[] nameBytes = new byte[NAME_LENGTH];
        byte[] descBytes = new byte[DESC_LENGTH];
        byte[] idBytes = new byte[ID_LENGTH];

        file.readFully(nameBytes);
        file.readFully(descBytes);
        file.readFully(idBytes);
        double cost = file.readDouble();

        String name = new String(nameBytes, StandardCharsets.UTF_8).trim();
        String desc = new String(descBytes, StandardCharsets.UTF_8).trim();
        String id = new String(idBytes, StandardCharsets.UTF_8).trim();

        return new Product(id, name, desc, cost);
    }

    private static String fixedLength(String str, int length) {
        return String.format("%-" + length + "s", str);
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Name: %s | Desc: %s | Cost: %.2f", ID, name, description, cost);
    }
}
