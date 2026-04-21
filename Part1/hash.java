import java.io.*;
import java.util.*;

public class hash {
    static class OpenAddressingHashTable {
        private static final int tableLen = 100; 
        private String[] table;
        private int size;
        
        public OpenAddressingHashTable() {
            table = new String[tableLen];
            size = 0;
        }
        
        // Hash function using Java's built-in hashCode
        private int hash(String key) {
            return Math.abs(key.hashCode()) % tableLen;
        }
        
        // Linear probing for open addressing
        private int probe(String key) {
            int index = hash(key);
            int originalIndex = index;
            int i = 0;
            
            //check next position if collision occurs
            while (table[index] != null && !table[index].equals(key)) {
                i++;
                index = (originalIndex + i) % tableLen;
                
                // Prevent infinite loop if table is full
                if (i >= tableLen) {
                    return -1; 
                }
            }
            
            return index;
        }
        
        // Insert a key into the hash table
        public boolean insert(String key) {
            if (size >= tableLen * 0.75) { //to check if the table is getting too full
                return false; 
            }
            
            int index = probe(key);
            if (index == -1) {
                return false;
            }
            
            // Only insert if key doesn't already exist
            if (table[index] == null) {
                table[index] = key;
                size++;
                return true;
            }
            
            return false; 
        }
        
        
        public boolean search(String key) {
            int index = probe(key);
            return index != -1 && table[index] != null && table[index].equals(key);
        }
        
    
        public void display() {
            System.out.println("\n Open Addressing Hash Table ");
            System.out.println("Table Size: " + tableLen);
            System.out.println("Number of entries: " + size);
            System.out.println("\nHash Table Contents:");
            System.out.println(String.format("%-10s | %-20s", "Index", "Last Name"));
            
            
            for (int i = 0; i < tableLen; i++) {
                if (table[i] != null) {
                    System.out.println(String.format("%-10d | %-20s", i, table[i]));
                }
            }
        }
        
        
        public void displayStats() {
            System.out.println("\n Statistics ");
            System.out.println("Table Size: " + tableLen);
            System.out.println("Entries Stored: " + size);
            System.out.println("Load Factor: " + String.format("%.2f%%", (size * 100.0) / tableLen));
            System.out.println("Empty Slots: " + (tableLen - size));
        }
    }
    
    public static void main(String[] args) {
        OpenAddressingHashTable hashTable = new OpenAddressingHashTable();
        Set<String> uniqueLastNames = new HashSet<>();
        
        // Read patient.txt and extract last names
        try {
            BufferedReader reader = new BufferedReader(new FileReader("Part1/patient.txt"));
            String line;
            int lineCount = 0;
            
            while ((line = reader.readLine()) != null) {
                lineCount++;
                // Format: firstName, lastName, date1, date2
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String lastName = parts[1].trim();
                    uniqueLastNames.add(lastName);
                }
            }
            reader.close();
            
            System.out.println("Read " + lineCount + " patient records");
            System.out.println("Found " + uniqueLastNames.size() + " unique last names");
            System.out.println("\nInserting last names into hash table:");
            
            // Insert unique last names into hash table
            int inserted = 0;
            for (String lastName : uniqueLastNames) {
                if (hashTable.insert(lastName)) {
                    System.out.println("  Inserted: " + lastName);
                    inserted++;
                } else {
                    System.out.println("  Failed to insert: " + lastName);
                }
            }
            
            System.out.println("\nSuccessfully inserted " + inserted + " last names");
            
            // show the results
            hashTable.display();
            hashTable.displayStats();
            
            // testing code
            System.out.println("\n Search Test ");
            String[] testNames = {"Wong", "Chavez", "Gilbert", "Smith"};
            for (String name : testNames) {
                boolean found = hashTable.search(name);
                System.out.println("Search for '" + name + "': " + (found ? "FOUND" : "NOT FOUND"));
            }
            //was having trouble with read errors so the following is here so I can know
            //leaving it in just in case
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
