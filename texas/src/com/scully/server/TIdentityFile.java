package com.scully.server;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Random;

/**
 * Class representing a players identity
 */
public class TIdentityFile implements Serializable {

    /**
     * The identity of the player in a string
     */
    public String token = "NULL";

    /**
     * Directory where the identity file is stored
     */
    File rootDir;

    /**
     * File of the identity
     */
    File idenFile;

    /**
     * Filename in the directory
     */
    public static final String IDEN_FILE_NAME = "iden";

    public TIdentityFile() {
        try {
            rootDir  = new File(TPokerClient.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
            idenFile = new File(rootDir, IDEN_FILE_NAME);

            if(!idenFile.exists()) {
                createIdenFile();
            }
            readIdenFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.err.println("TIdentityFile: Error getting parent folder");
        }
    }

    /**
     * Read the identity file - if it exists
     */
    public void readIdenFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(idenFile));
            token = reader.readLine();
            System.out.println("TIdentity: Using identity token: " + token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the identity file
     */
    public void createIdenFile() {

        Random random = new Random();

        //TODO change this to be more secure
        int id = random.nextInt(Integer.MAX_VALUE);

        try {
            boolean created = idenFile.createNewFile();

            if(created) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(idenFile));

                writer.write(Integer.toString(id));

                writer.close();
            } else {
                System.err.println("IDENTITY FILE COULD NOT BE CREATED");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TIdentityFile that = (TIdentityFile) o;
        return token.equals(that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
