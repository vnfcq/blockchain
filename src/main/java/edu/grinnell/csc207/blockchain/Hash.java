package edu.grinnell.csc207.blockchain;

import java.util.Arrays;

/**
 * A wrapper class over a hash value (a byte array).
 */
public class Hash {

    private final byte[] data;

    /**
     * Constructs a new Hash object that contains the given hash (as an array of bytes).
     *
     * @param data an array of bytes
     */
    public Hash(byte[] data) {
        this.data = data;
    }

    /**
     * @return the hash contained in this object
     */
    public byte[] getData() {
        return data;
    }

    /**
     * @return true if this hash meets the criteria for validity, i.e., its first three indices
     * contain zeroes
     */
    public boolean isValid() {
        return data.length >= 3 && data[0] == 0 && data[1] == 0 && data[2] == 0;
    }

    /**
     * @return the string representation of the hash as a string of hexadecimal digits, 2 digits
     * per byte
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data) {
            int unsignedInt = Byte.toUnsignedInt(b);
            sb.append(String.format("%02x", unsignedInt));
        }
        return sb.toString();
    }

    /**
     * @param other the reference object with which to compare
     *
     * @return true if this hash is structurally equal to the argument
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Hash)) {
            return false;
        }
        Hash o = (Hash) other;
        return Arrays.equals(this.data, o.data);
    }
}
