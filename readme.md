Part 1: Open Addressing Hash Table
 Hash Function
private int hash(String key) {
    return Math.abs(key.hashCode()) % tableLen;
}
Optimized hash function that ensures non negative index values
% tableLen: Maps the hash code to a valid index within the table size (0 to tableLen-1).
Math.abs(): Ensures non-negative index values since hashCode() can return negative numbers.
Collision Handling: Linear Probing
When a collision occurs the algorithm uses Linear probing:
1. If the target index is occupied, check the next position: (originalIndex + i) % tableLen
2. Continue probing until an empty slot is found
3. Insert the key at the first available empty position

Part 2: Chaining Hash Table

Hash Function
private int hash(String key) {
    return Math.abs(key.hashCode()) % tableSize;
}
I chose this hash function for the same reasons as open addressing. This function is perfectly optimal, it is just the collision handling that needs to be changed

Collision Handling: Separate Chaining
Each bucket in the hash table is a Linked List that can store multiple keys. When a collision occurs, the new key is simply added to the chain:
1. Compute hash index
2. Check if chain already contains items (collision detection)
3. Append the new key to the LinkedList at that index
4. Both keys coexist in the same chain

I included a collision count method so I could track the ammount of collisions so I could know if it was acting as expected.

I did something similar to this assignment in one of my programming fundamentals courses so this was all pretty familiar. Its not outright stated in the instructions but I included a basic menu in chainhash.java to make testing it easier. Also included comments since some things are not immediately obvious as to why they are used such as @suppresswarnings.