package edu.grinnell.csc207.blockchain;

import java.util.Scanner;

/**
 * The main driver for the block chain program.
 */
public class BlockChainDriver {

    private static void help() {
        System.out.println("Valid commands:");
        System.out.println("    mine: discovers the nonce for a given transaction");
        System.out.println("    append: appends a new block onto the end of the chain");
        System.out.println("    remove: removes the last block from the end of the chain");
        System.out.println("    check: checks that the block chain is valid");
        System.out.println("    report: reports the balances of Alice and Bob");
        System.out.println("    help: prints this list of commands");
        System.out.println("    quit: quits the program");
    }

    /**
     * The main entry point for the program.
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        int initial = Integer.parseInt(args[0]);

        BlockChain bc = new BlockChain(initial);
        Scanner sc = new Scanner(System.in);

        label:
        while (true) {

            System.out.println(bc);

            System.out.print("Command? ");
            if (!sc.hasNext()) {
                break;
            }
            String command = sc.next();

            switch (command) {
                case "mine": {
                    System.out.print("Amount transferred? ");
                    int amount = sc.nextInt();
                    Block blk = bc.mine(amount);
                    System.out.printf("amount = %d, nonce = %d%n", amount, blk.getNonce());
                    break;
                }
                case "append": {
                    System.out.print("Amount transferred? ");
                    int amount = sc.nextInt();
                    System.out.print("Nonce? ");
                    long nonce = sc.nextLong();

                    try {
                        int num = bc.getSize();
                        Hash prevHash = bc.getHash();
                        Block blk = new Block(num, amount, prevHash, nonce);
                        bc.append(blk);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Exception: " + e.getMessage());
                    }
                    break;
                }
                case "remove":
                    bc.removeLast();
                    break;
                case "check":
                    if (bc.isValidBlockChain()) {
                        System.out.println("Chain is valid!");
                    } else {
                        System.out.println("Chain is invalid!");
                    }
                    break;
                case "report":
                    bc.printBalances();
                    break;
                case "help":
                    help();
                    break;
                case "quit":
                    break label;
            }

            System.out.println();
        }

        sc.close();
    }  
}
