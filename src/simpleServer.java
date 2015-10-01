import java.io.*;
import java.net.*;
import java.lang.Runnable;

/*
class RunnableThread implements Runnable {

    Thread runner;
    BufferedReader data_in;

    public RunnableThread() {
    }

    public RunnableThread(String threadName, BufferedReader reader) {
        data_in = reader;
        runner = new Thread(this, threadName); // (1) Create a new thread.
        System.out.println(runner.getName());
        runner.start(); // (2) Start the thread.
    }

    public void run() {
        //Display info about this particular thread
        System.out.println(Thread.currentThread());
        while (true) {
            String msg = null;
            try {
                msg = data_in.readLine();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            if (msg != null) {
                System.out.println(msg);
            } 
        }
    }
}
 */

public class simpleServer {

    //private static RunnableThread reader;
    private static BufferedReader data_in;
    private static PrintWriter data_out;
    // Message terminator
    private static final char EOF = (char) 0x00;

    public static void log(String message, int status) {
        switch (status) {
            case 0:
                System.out.println("Received: " + message);
                break;
            case 1:
                System.out.println("Status: " + message);
                break;
            case 2:
                System.out.println("Connection: " + message);
                break;
        }
    }

    public static void send(String message) {
        data_out.write(message + 0x00);
        data_out.flush();
    }

    public static void main(String args[]) {

        //while (true) {
        try {
            // create a serverSocket connection on port 3333
            ServerSocket s = new ServerSocket(3333);


            System.out.println("Server started. Waiting for connections...");
            // wait for incoming connections

            Socket serverSocket = s.accept();
            log("Client connection from "+serverSocket.getRemoteSocketAddress(),2);
            data_in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            data_out = new PrintWriter(serverSocket.getOutputStream());
            //reader = new RunnableThread("Reader", data_in);

            boolean quit = false;

            while (!quit) {
                String msg = data_in.readLine();

                if (msg != null) {
                    if (msg.equals("<policy-file-request/>\0")) {
                        log(msg, 0);
                        send("<?xml version=\"1.0\"?><cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"*\" /></cross-domain-policy>");
                        log("print-policy",1);
                        send("<?xml version=\"1.0\"?><p>message</p>");
                        log("print-message",1);
                    }
                }

            }
        }
        catch (Exception e) {
            System.err.println("Connection lost");
        }
        //}

    }
}