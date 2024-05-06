import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientMain {

    private static final int NUMBER_OF_CLIENTS = 10;
    private static final String DOWNLOAD_FOLDER_PATH = "downloads";
    private static final String UPLOAD_FOLDER_PATH = "to_store";

    public static void main(String[] args) {
        int clientPort = Integer.parseInt(args[0]);
        int timeout = Integer.parseInt(args[1]);

        File downloadFolder = createFolder(DOWNLOAD_FOLDER_PATH);
        File uploadFolder = verifyFolderExists(UPLOAD_FOLDER_PATH);

        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_CLIENTS);
        for (int i = 0; i < NUMBER_OF_CLIENTS; i++) {
            executorService.execute(() -> runClientOperations(clientPort, timeout, downloadFolder, uploadFolder));
        }
        executorService.shutdown();
    }

    private static File createFolder(String path) {
        File folder = new File(path);
        if (!folder.exists() && !folder.mkdir()) {
            throw new RuntimeException("Cannot create folder: " + folder.getAbsolutePath());
        }
        return folder;
    }

    private static File verifyFolderExists(String path) {
        File folder = new File(path);
        if (!folder.exists()) {
            throw new RuntimeException(path + " folder does not exist");
        }
        return folder;
    }

    private static void runClientOperations(int clientPort, int timeout, File downloadFolder, File uploadFolder) {
        try (Client client = new Client(clientPort, timeout, Logger.LoggingType.ON_FILE_AND_TERMINAL)) {
            client.connect();
            performFileOperations(client, uploadFolder);
            displayAndRemoveFiles(client);
        } catch (IOException | NotEnoughDstoresException e) {
            System.err.println("Error during client operations: " + e.getMessage());
        }
    }

    private static void performFileOperations(Client client, File uploadFolder) throws IOException {
        Random random = new Random();
        File[] filesToStore = uploadFolder.listFiles();
        for (int i = 0; i < filesToStore.length / 2; i++) {
            File fileToStore = filesToStore[random.nextInt(filesToStore.length)];
            client.store(fileToStore);
        }
    }

    private static void displayAndRemoveFiles(Client client) throws IOException, NotEnoughDstoresException {
        String[] fileList = client.list();
        System.out.println("Files on the server:");
        Random random = new Random();
        for (int i = 0; i < fileList.length; i++) {
            System.out.println("[" + i + "] " + fileList[i]);
            if (i < fileList.length / 4) {
                client.remove(fileList[random.nextInt(fileList.length)]);
            }
        }
    }
}
