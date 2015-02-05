/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ftp;



import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import it.sauronsoftware.ftp4j.FTPListParseException;
import java.io.IOException;
import java.util.Scanner;

public class FTP {

    private static final String URL = "ftp.mozilla.org";
    private static final String LOGIN = "anonymous";
    private static final String PASSWORD = "ftp4j";
    private static final String HELP = "Enter 'parent' to change directory up, 'exit' - to exit. "
            + "Your choice:";
    private static final String EXIT = "exit";
    private static final String PARENT = "parent";
    private static final String PATH = "D:\\";
    private static final String IOEX = "I/O exception of some sort has occurred";
    private static final String ILLEGALSTATEEX = "Java environment or Java application is "
            + "not in an appropriate state for the requested operation";
    private static final String FTPEX = "Something goes wrong during the FTP transfer";
    private static final int IS_FILE = 0;
    private static final int IS_FOLDER = 1;
    private static final int IS_LINK = 2;

    public static void main(String[] args) throws FTPIllegalReplyException, FTPException, IllegalStateException, IOException {
        FTPClient client = new FTPClient();
        try {
            client.connect(URL);
            client.login(LOGIN, PASSWORD);
            showDirectories(client);
            processChoice(client);
        } catch (IOException e) {
            System.out.println(IOEX);
        } catch (IllegalStateException e) {
            System.out.println(ILLEGALSTATEEX);
        } catch (FTPIllegalReplyException | FTPException | FTPDataTransferException | FTPAbortedException | FTPListParseException e) {
            System.out.println(FTPEX);
        } finally {
            client.disconnect(true);
        }

    }

    public static void showDirectories(FTPClient client) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException, FTPListParseException {
        FTPFile[] list = client.list();
        for (FTPFile fTPFile : list) {
            System.out.println(fTPFile.getName());
        }
    }

    public static void processChoice(FTPClient client) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException, FTPListParseException {
        while (true) {
            System.out.println(HELP);
            Scanner in = new Scanner(System.in);
            String choice = in.nextLine();
            if (choice.equals(EXIT)) {
                break;
            }
            if (choice.equals(PARENT)) {
                client.changeDirectoryUp();
                showDirectories(client);
            }
            realizeChoice(client, choice);
        }
    }

    public static void realizeChoice(FTPClient client, String choice) throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException, FTPListParseException {
        FTPFile[] list = client.list();
        for (FTPFile fTPFile : list) {
            if (fTPFile.getName().equals(choice) && (fTPFile.getType() == IS_FILE)) {
                client.download(fTPFile.getName(), new java.io.File(PATH + fTPFile.getName()));
            } else {
                if (fTPFile.getName().equals(choice) && ((fTPFile.getType() == IS_FOLDER) || (fTPFile.getType() == IS_LINK))) {
                    client.changeDirectory(choice);
                    showDirectories(client);
                }
            }

        }
    }
}
