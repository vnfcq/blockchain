package edu.grinnell.csc207.blockchain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.ByteBuffer;

/**
 * A single block of a blockchain.
 */
public class Block {

    private int num;
    private int amount;
    private Hash prevHash;
    private long nonce;
    private Hash hash;

    public Block(int num, int amount, Hash prevHash) {
        this.num = num;
        this.amount = amount;
        this.prevHash = prevHash;

        long candidateNonce = 0L;

        while (true) {
            Hash candidateHash = calculateHash(num, amount, prevHash, candidateNonce);
            if (candidateHash.isValid()) {
                this.nonce = candidateNonce;
                this.hash = candidateHash;
                break;
            }
            candidateNonce++;
        }
    }

    public Block(int num, int amount, Hash prevHash, long nonce) {
        this.num = num;
        this.amount = amount;
        this.prevHash = prevHash;
        this.nonce = nonce;
        this.hash = calculateHash(num, amount, prevHash, nonce);
    }

    public int getNum(){
        return num;
    }

    public int getAmount() {
        return amount;
    }

    public long getNonce() {
        return nonce;
    }

    public Hash getPrevHash(){
        return prevHash;
    }

    public Hash getHash() {
        return hash;
    }

    @Override
    public String toString() {
        String prevHashStr = (num == 0) ? "null" : prevHash.toString();
        String hashStr = hash.toString();
        return String.format("Block %s (Amount: %s, Nonce: %s, prevHash: %s, hash: %s)", num, amount, nonce, prevHashStr, hashStr);
    }

    private static Hash calculateHash(int num, int amount, Hash prevHash, long nonce) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update(ByteBuffer.allocate(4).putInt(num).array());

            md.update(ByteBuffer.allocate(4).putInt(amount).array());

            if (num != 0) {
                md.update(prevHash.getData());
            }

            md.update(ByteBuffer.allocate(8).putLong(nonce).array());

            byte[] bytes = md.digest();
            return new Hash(bytes);
        } catch (NoSuchAlgorithmException e){
            throw new AssertionError(e);
        }
    }
}
