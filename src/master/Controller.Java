package master;
java,io*;
java.io.IOException;
java.net.ServerSocket;
java.net.Socket;
import common.Logger;


public class Controller {

    private final int cport;
    static int R;
    private final int timeout;
    private final int rebalance_period;
    private ServerSocket serverSocket;
    private final Logger logger;

    public Controller(int cport, int R, int timeout, int rebalance_period) {
        this.cport = cport;
        Controller.R = R;
        this.timeout = timeout;
        this.rebalance_period = rebalance_period;
        this.logger = ControllerLogger.getInstance();
    }

    // ================================================================================================================

    // ================================================================================================================

    public void startAndListen() {
        try {
            logger.info("Initializing server");
            serverSocket = new ServerSocket(cport);
            logger.info("Starting controller on port " + cport);
            while (true) {
                try {
                    Socket client = serverSocket.accept();
                    logger.info("Accepted connection from " + client.getRemoteSocketAddress());
                    new Thread(() -> {
                        try {
                            handleClient(client);
                        } catch (Exception e) {
                            logger.error("Error handling client: " + e.getMessage());
                            e.printStackTrace();
                        } finally {
                            try {
                                client.close();
                            } catch (IOException e) {
                                logger.error("Error closing client socket: " + e.getMessage());
                            }
                        }
                    }).start();
                } catch (IOException e) {
                    logger.error("Error accepting connection: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            logger.error("Error starting controller: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                    logger.info("Server socket closed");
                } catch (IOException e) {
                    logger.error("Error closing server socket: " + e.getMessage());
                }
            }
        }
    }

    // ================================================================================================================

    // ================================================================================================================

    public void handleClient(Socket client){
        try
        {
            BufferReader in  = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out  = new PrintWriter(client.getOutputStream(), true);
            String request; 

            while ((request = in.readLine()) != null) {
                logger.info("Received request: " + request);
                String response = processRequest(request);
                logger.info("Sending response: " + response);
                out.println(response);
            }
        }catch (IOException e){
            logger.error("Error handling client: " + e.getMessage());
            e.printStackTrace();
        }
        
        private String processRequest(String request){
            switch (request.toLowerCase()) {
 
                //  TO DO  FILL OUT  IMPLEMENTATION
                case "upload":
                    return "Upload handled";
                case "download":
                    return "Download handled";
                case "list":
                    return "List handled";
                case "delete":
                    return "Delete handled";
                default:
                    return "Unknown request";
            }
        }
        private String handleUpload(){
    
        }

        private String handleDownload(){
    
        }

        private String handleList(){
    
        }

        private String handleDelete(){
    
        }
    }

    // ================================================================================================================

    // ================================================================================================================

    public static void main(String[] args){
        if(args.length != 4){
            System.out.println("Usage: java Controller <cport> <R> <timeout> <rebalance_period>");
            System.exit(1);
        }
        int cport = Integer.parseInt(args[0]);
        int R = Integer.parseInt(args[1]);
        int timeout = Integer.parseInt(args[2]);
        int rebalance_period = Integer.parseInt(args[3]);
        Controller controller = new Controller(cport, R, timeout, rebalance_period);
        try {
            ControllerLogger.init(Logger.LoggingType.ON_FILE_AND_TERMINAL);
            new Controller(cport, R, timeout, rebalance_period).start();
        } catch (IOException e) {
            System.err.println("Error initializing ControllerLogger: " + e.getMessage());
        }
    }

    public int getCport() {
        return cport;
    }

    public int getR() {
        return R;
    }

    public int getRebalance_period() {
        return rebalance_period;
    }

    public int getTimeout() {
        return timeout;
    }
}
