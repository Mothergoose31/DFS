package master;

java.io.IOException;
java.net.ServerSocket;
java.net.Socket;


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



    public void startAndListen() {
        
        try {
            logger.info("initializing server");
            serverSocket = new ServerSocket(cport);
            logger.info("Starting controller on port " + cport);
            while(

            )
        }
    }


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
            // Start the controller
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