package edu.grinnell.csc207.blockchain;

import java.util.Arrays;

/**
 * A wrapper class over a hash value (a byte array).
 */
public class Hash {

    private final byte[] data;

    public Hash(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public boolean isValid() {
        return data.length >= 3 && data[0] == 0 && data[1] == 0 && data[2] == 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data){
            int unsignedInt = Byte.toUnsignedInt(b);
            sb.append(String.format("%02x", unsignedInt));
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object other){
        if (this == other){
            return true;
        }
        if (!(other instanceof Hash)){
            return false;
        }
        Hash o = (Hash) other;
        return Arrays.equals(this.data, o.data);
    }
}
