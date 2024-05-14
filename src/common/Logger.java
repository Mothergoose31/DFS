import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class Logger {

    public enum LoggingType {
        NO_LOG,
        ON_TERMINAL_ONLY, 
        ON_FILE_ONLY,
        ON_FILE_AND_TERMINAL 
    }

    public enum LogLevel {
        INFO,
        DEBUG,
        ERROR
    }

    protected final LoggingType loggingType;
    protected PrintStream ps;
    protected final BlockingQueue<String> logQueue;
    protected volatile boolean running;

    protected Logger(LoggingType loggingType) {
        this.loggingType = loggingType;
        this.logQueue = new LinkedBlockingQueue<>();
        this.running = true;
        startLogThread();
    }

    protected abstract String getLogFileSuffix();

    protected synchronized PrintStream getPrintStream() throws IOException {
        if (ps == null) {
            ps = new PrintStream(new FileOutputStream(getLogFileSuffix() + "_" + System.currentTimeMillis() + ".log", true));
        }
        return ps;
    }

    protected boolean logToFile() {
        return loggingType == LoggingType.ON_FILE_ONLY || loggingType == LoggingType.ON_FILE_AND_TERMINAL;
    }

    protected boolean logToTerminal() {
        return loggingType == LoggingType.ON_TERMINAL_ONLY || loggingType == LoggingType.ON_FILE_AND_TERMINAL;
    }

    protected void log(String message, LogLevel level) {
        String timestampedMessage = "[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "] [" + level + "] " + message;
        logQueue.offer(timestampedMessage);
    }

    public void messageSent(Socket socket, String message) {
        log("[" + socket.getLocalPort() + "->" + socket.getPort() + "] " + message, LogLevel.INFO);
    }

    public void messageReceived(Socket socket, String message) {
        log("[" + socket.getLocalPort() + "<-" + socket.getPort() + "] " + message, LogLevel.INFO);
    }

    public void info(String message) {
        log(message, LogLevel.INFO);
    }

    public void debug(String message) {
        log(message, LogLevel.DEBUG);
    }

    public void error(String message) {
        log(message, LogLevel.ERROR);
    }

    private void startLogThread() {
        Thread logThread = new Thread(() -> {
            while (running || !logQueue.isEmpty()) {
                try {
                    String logMessage = logQueue.poll();
                    if (logMessage != null) {
                        if (logToFile()) {
                            try {
                                getPrintStream().println(logMessage);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (logToTerminal()) {
                            System.out.println(logMessage);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        logThread.setDaemon(true);
        logThread.start();
    }

    public void close() {
        running = false;
        if (ps != null) {
            ps.close();
        }
    }
}