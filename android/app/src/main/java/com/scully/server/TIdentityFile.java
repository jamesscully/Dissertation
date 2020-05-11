package com.scully.server;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;

import com.scullyapps.RekopApplication;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Random;

public class TIdentityFile implements Serializable {
    public String token = "NULL";

    File rootDir;
    File idenFile;

    public static final String IDEN_FILE_NAME = "iden";

    public TIdentityFile(File root) {
        rootDir  = root;
        idenFile = new File(rootDir, IDEN_FILE_NAME);

        if(!idenFile.exists()) {
            createIdenFile();
        }

        readIdenFile();
    }

    public void readIdenFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(idenFile));
            token = reader.readLine();
            System.out.println("TIdentity: Using identity token: " + token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
