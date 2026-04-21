import java.io.*;
import java.util.*;

public class chainhash {
    static class ChainingHashTable {
        private LinkedList<String>[] table;
        private int tableSize;
        private int collisionCount;
        private int totalItemsInserted;
        //Suppresswarnings is used to take care of the generic type warning
        //I am using generic types because initializing it as a string seems
        //  to cause it to lose data
        @SuppressWarnings("unchecked")
        public ChainingHashTable(int size) {
            tableSize = size;
            table = new LinkedList[tableSize];
            collisionCount = 0;
            totalItemsInserted = 0;
            
            // Initialize each chain
            for (int i = 0; i < tableSize; i++) {
                table[i] = new LinkedList<>();
            }
        }
        
        // Hash function using Java's built in hashCode
        private int hash(String key) {
            return Math.abs(key.hashCode()) % tableSize;
        }
        
        // Inserts a key into the hash table
        public void insert(String key) {
            int index = hash(key);
            
            // Check if this is a collision
            if (!table[index].isEmpty() && !table[index].contains(key)) {
                collisionCount++;
                System.out.println("  [COLLISION] Index: " + index + " | Inserting key: '" + key + 
                                   "' | Chain size before: " + table[index].size());
            } else if (!table[index].contains(key)) {
                System.out.println("  Inserted: '" + key + "' at index " + index);
            }
            
            // Adds if not already present
            if (!table[index].contains(key)) {
                table[index].add(key);
                totalItemsInserted++;
            }
        }
        
        
        public boolean search(String key) {
            int index = hash(key);
            return table[index].contains(key);
        }
        
        
        public void display() {
            System.out.println("\n Chaining Hash Table ");
            System.out.println("Table Size: " + tableSize);
            System.out.println("Total Items: " + totalItemsInserted);
            System.out.println("Total Collisions: " + collisionCount);
            System.out.println("\nHash Table Contents:");
            System.out.println(String.format("%-10s | %-50s", "Index", "Last Names (Chain)"));
        
            
            for (int i = 0; i < tableSize; i++) {
                if (!table[i].isEmpty()) {
                    StringBuilder chain = new StringBuilder();
                    for (int j = 0; j < table[i].size(); j++) {
                        if (j > 0) chain.append(" -> ");
                        chain.append(table[i].get(j));
                    }
                    System.out.println(String.format("%-10d | %-50s", i, chain.toString()));
                }
            }
        }
        
        
        public void displayStats() {
            System.out.println("\n Statistics ");
            System.out.println("Table Size: " + tableSize);
            System.out.println("Total Items Inserted: " + totalItemsInserted);
            System.out.println("Total Collisions Detected: " + collisionCount);
            System.out.println("Load Factor: " + String.format("%.2f%%", (totalItemsInserted * 100.0) / tableSize));
            
            int emptySlots = 0;
            int maxChainLength = 0;
            double avgChainLength = 0;
            
            for (int i = 0; i < tableSize; i++) {
                if (table[i].isEmpty()) {
                    emptySlots++;
                } else {
                    maxChainLength = Math.max(maxChainLength, table[i].size());
                    avgChainLength += table[i].size();
                }
            }
            
            avgChainLength = (tableSize - emptySlots > 0) ? avgChainLength / (tableSize - emptySlots) : 0;
            
            System.out.println("Empty Slots: " + emptySlots);
            System.out.println("Max Chain Length: " + maxChainLength);
            System.out.println("Average Chain Length: " + String.format("%.2f", avgChainLength));
        }
        
        // Rehash the table 
        @SuppressWarnings("unchecked")
        public void rehash() {
            System.out.println("\n>>> Starting Rehash Operation...");
            System.out.println("    Old table size: " + tableSize);
            
            LinkedList<String>[] oldTable = table;
            int oldSize = tableSize;
            
            tableSize = tableSize * 2;
            table = new LinkedList[tableSize];
            
            // Initialize new chains
            for (int i = 0; i < tableSize; i++) {
                table[i] = new LinkedList<>();
            }
            
            // Reset collision count for rehashing
            int rehashCollisions = 0;
            
            // adds all items from old table
            System.out.println("    Rehashing items into new table of size " + tableSize + "...");
            for (int i = 0; i < oldSize; i++) {
                for (String key : oldTable[i]) {
                    int newIndex = hash(key);
                    if (!table[newIndex].isEmpty()) {
                        rehashCollisions++;
                    }
                    table[newIndex].add(key);
                }
            }
            
            System.out.println(">>> Rehash Complete!");
            System.out.println("    New table size: " + tableSize);
            System.out.println("    Collisions during rehash: " + rehashCollisions);
            
            // Update total collision count with rehash collisions
            collisionCount += rehashCollisions;
        }
        
        public int getTableSize() {
            return tableSize;
        }
        
        public int getTotalItemsInserted() {
            return totalItemsInserted;
        }
        
        public int getCollisionCount() {
            return collisionCount;
        }
    }
    
    public static void main(String[] args) {
        ChainingHashTable hashTable = new ChainingHashTable(50);
        Set<String> uniqueLastNames = new HashSet<>();
        
        // Read patient.txt and extract last names
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader("patient.txt"));
            String line;
            int lineCount = 0;
            
            while ((line = fileReader.readLine()) != null) {
                lineCount++;
                // Format: firstName, lastName, date1, date2
                String[] parts = line.split("\\s*,\\s*");
                if (parts.length >= 2) {
                    String lastName = parts[1];
                    uniqueLastNames.add(lastName);
                }
            }
            fileReader.close();
            
            System.out.println(" Initial Loading ");
            System.out.println("Read " + lineCount + " patient records");
            System.out.println("Found " + uniqueLastNames.size() + " unique last names");
            System.out.println("\nInserting last names into hash table (Size: 50):");
            
            // Insert unique last names into hash table
            for (String lastName : uniqueLastNames) {
                hashTable.insert(lastName);
            }
            
            System.out.println("\n Initial Population Complete ");
            hashTable.displayStats();
            
            // Interactive menu
            Scanner scanner = new Scanner(System.in);
            boolean running = true;
            
            while (running) {
                System.out.println("\n MENU ");
                System.out.println("1. Display Hash Table");
                System.out.println("2. Rehash Table");
                System.out.println("3. Quit");
                System.out.print("Enter your choice (1-3): ");
                
                String choice = scanner.next();
                
                switch (choice) {
                    case "1":
                        hashTable.display();
                        hashTable.displayStats();
                        break;
                        
                    case "2":
                        hashTable.rehash();
                        hashTable.display();
                        hashTable.displayStats();
                        break;
                        
                    case "3":
                        System.out.println("\nFinal Statistics:");
                        hashTable.displayStats();
                        System.out.println("\nGoodbye!");
                        running = false;
                        break;
                        
                    default:
                        System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                }
            }
            
            scanner.close();
            //handling for the same reason as in hash.java
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}

