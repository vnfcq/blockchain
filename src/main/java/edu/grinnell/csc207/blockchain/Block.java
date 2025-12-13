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

    /**
     * Creates a new block from the specified parameters, performing the mining operation to
     * discover the nonce and hash for this block given these parameters.
     *
     * @param num an integer
     * @param amount an integer
     * @param prevHash a hash
     */
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

    /**
     * Creates a new block from the specified parameters, using the provided nonce and additional
     * parameters to generate the hash for the block. Because the nonce is provided, this
     * constructor does not need to perform the mining operation; it can compute the hash directly.
     *
     * @param num an integer
     * @param amount an integer
     * @param prevHash a hash
     * @param nonce a long
     */
    public Block(int num, int amount, Hash prevHash, long nonce) {
        this.num = num;
        this.amount = amount;
        this.prevHash = prevHash;
        this.nonce = nonce;
        this.hash = calculateHash(num, amount, prevHash, nonce);
    }

    /**
     * @return the number of this block
     */
    public int getNum() {
        return num;
    }

    /**
     * @return the amount transferred that is recorded in this block
     */
    public int getAmount() {
        return amount;
    }

    /**
     * @return the nonce of this block
     */
    public long getNonce() {
        return nonce;
    }

    /**
     * @return the hash of the previous block in the blockchain
     */
    public Hash getPrevHash() {
        return prevHash;
    }

    /**
     * @return the hash of this block
     */
    public Hash getHash() {
        return hash;
    }

    /**
     * @return a string representation of the block
     */
    @Override
    public String toString() {
        String prevHashStr = (num == 0) ? "null" : prevHash.toString();
        String hashStr = hash.toString();
        return String.format("Block %s (Amount: %s, Nonce: %s, prevHash: %s, hash: %s)", num,
                amount, nonce, prevHashStr, hashStr);
    }

    /**
     * @param num an integer
     * @param amount an integer
     * @param prevHash a hash
     * @param nonce a long
     *
     * @return the hash calculated from the specified parameters, using the provided nonce and
     * additional parameters
     */
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
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }
}
