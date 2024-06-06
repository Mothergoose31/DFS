package client;

import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import baseInterface.MessageNotFoundException;

public class ClientMain {

    public static void main(String[] args) {
        int testNumber = 1;
        for (int test = 1; test <= testNumber; test++) {
            String transactionFilePath = "tests/transaction" + test + ".txt";
            Thread clientThread = new Thread(() -> {
              
                long minSleep = 1000;
                long maxSleep = 5000;
                long sleepTime = minSleep + (long) (Math.random() * (maxSleep - minSleep));
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
          
                Logger.getLogger("client").log(Level.INFO, "Start Client executing " + transactionFilePath);
                try {
                    Client client = new Client();
                    client.executeTransaction(transactionFilePath);
                } catch (RemoteException | NotBoundException | IOException | MessageNotFoundException e) {
                    e.printStackTrace();
                }
            });
            clientThread.start();
        }
    }
}