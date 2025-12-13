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

    public BlockChain(int initial) {
        Block data = new Block(0, initial, null);
        Node node = new Node(data);
        this.first = node;
        this.last = node;
    }

    public Block mine(int amount) {
        Block lastBlock = last.data;
        return new Block(lastBlock.getNum() + 1, amount, lastBlock.getHash());
    }

    public int getSize() {
        return last.data.getNum() + 1;
    }

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

    public Hash getHash() {
        return last.data.getHash();
    }

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
