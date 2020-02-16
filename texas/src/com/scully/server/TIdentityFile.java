package com.scully.server;

import java.io.File;
import java.net.URISyntaxException;

public class TIdentityFile {
    String token = "NULL";

    File dir;

    public TIdentityFile() {
        try {
            dir = new File(TPokerClient.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();

            System.out.println(dir.getAbsolutePath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.err.println("TIdentityFile: Error getting parent folder");
        }
    }
}
