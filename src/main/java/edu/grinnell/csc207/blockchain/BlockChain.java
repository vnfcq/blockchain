package edu.grinnell.csc207.blockchain;

/**
 * A linked list of hash-consistent blocks representing a ledger of
 * monetary transactions.
 */
public class BlockChain {

    private static class Node {
        Block data;
        Node next;

        Node(Block data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node first;
    private Node last;

    /**
     * creates a BlockChain that possess a single block the starts with the given initial amount.
     * Note that to create this block, the prevHash field should be ignored when calculating the
     * block's own nonce and hash.
     *
     * @param initial an integer
     */
    public BlockChain(int initial) {
        Block data = new Block(0, initial, null);
        Node node = new Node(data);
        this.first = node;
        this.last = node;
    }

    /**
     * @param amount an integer
     *
     * @return mines a new candidate block to be added to the list. The returned Block should be
     * valid to add onto this list
     */
    public Block mine(int amount) {
        Block lastBlock = last.data;
        return new Block(lastBlock.getNum() + 1, amount, lastBlock.getHash());
    }

    /**
     * @return the size of the blockchain. Note that number of the blocks provides a convenient
     * method for quickly determining the size of the chain
     */
    public int getSize() {
        return last.data.getNum() + 1;
    }

    /**
     * Adds this block to the list, throwing an IllegalArgumentException if this block cannot be
     * added to the list (because it is invalid wrt the rest of the blocks).
     *
     * @param blk a block
     */
    public void append(Block blk) {
        Block lastBlock = last.data;
        if (blk.getNum() != lastBlock.getNum() + 1) {
            throw new IllegalArgumentException("Illegal block number");
        }

        if (!blk.getPrevHash().equals(lastBlock.getHash())) {
            throw new IllegalArgumentException("Illegal block hash");
        }

        int balance = 0;
        Node current = this.first;
        while (current != null) {
            balance += current.data.getAmount();
            current = current.next;
        }
        balance += blk.getAmount();
        if (balance < 0) {
            throw new IllegalArgumentException("Illegal transaction amount");
        }

        Node node = new Node(blk);
        this.last.next = node;
        this.last = node;
    }

    /**
     * Removes the last block from the chain, returning true. If the chain only contains a single
     * block, then removeLast does nothing and returns false.
     *
     * @return a boolean
     */
    public boolean removeLast() {
        if (first == last) {
            return false;
        }

        Node current = first;
        while (current.next != last) {
            current = current.next;
        }
        current.next = null;
        last = current;
        return true;
    }

    /**
     * @return the hash of the last block in the chain
     */
    public Hash getHash() {
        return last.data.getHash();
    }

    /**
     * Walks the blockchain and ensures that its blocks are consistent and valid.
     *
     * @return a boolean
     */
    public boolean isValidBlockChain() {
        int balance = 0;
        Node current = first;
        Block prevBlock = null;

        while (current != null) {
            Block blk = current.data;

            if (current.data.getNum() != 0) {
                if (!blk.getPrevHash().equals(prevBlock.getHash())) {
                    return false;
                }
            }

            balance += blk.getAmount();
            if (balance < 0) {
                return false;
            }

            prevBlock = blk;
            current = current.next;
        }

        return true;
    }

    /**
     * Prints Alice's and Bob's respective balances in the form Alice: &lt;amt&gt;, Bob:
     * &lt;amt&gt;> on a single line, e.g., Alice: 300, Bob: 0.
     */
    public void printBalances() {
        int initial = first.data.getAmount();
        int balance = 0;

        Node current = first;
        while (current != null) {
            balance += current.data.getAmount();
            current = current.next;
        }

        System.out.printf("Alice: %d, Bob: %d%n", balance, initial - balance);
    }

    /**
     * @return a string representation of the BlockChain which is simply the string representation
     * of each of its blocks, earliest to latest, one per line
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node current = first;
        while (current != null) {
            sb.append(current.data.toString());
            if (current.next != null) {
                sb.append(System.lineSeparator());
            }
            current = current.next;
        }
        return sb.toString();
    }
}
