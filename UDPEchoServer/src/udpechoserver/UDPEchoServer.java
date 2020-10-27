
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.StringTokenizer;

public class UDPEchoServer {

    private static final int PORT = 1234;
    private static DatagramSocket dgramSocket;
    private static DatagramPacket inPacket, outPacket;
    private static byte[] buffer;
    static double euroUSD = 1.10;
    static double euroGBP = 0.84;
    static double USDEuro = 0.89;
    static double USDCny = 6.94;

    public static void main(String[] args) {
        System.out.println("Opening port...\n");

        try {
            dgramSocket = new DatagramSocket(PORT);//Step 1.
        } catch (SocketException e) {
            System.out.println("Unable to attach to port!");
            System.exit(1);
        }

        run();
    }

    private static void run() {
        try {
            String messageIn, messageOut;
            int numMessages = 0;
            do {
                buffer = new byte[256]; //Step 2.
                inPacket = new DatagramPacket(buffer, buffer.length); //Step 3.
                dgramSocket.receive(inPacket); //Step 4.
                InetAddress clientAddress = inPacket.getAddress(); //Step 5.
                int clientPort = inPacket.getPort(); //Step 5. 
                messageIn = new String(inPacket.getData(), 0, inPacket.getLength()); //Step 6.

                String[] split = messageIn.split("\\s+");
                int amount = Integer.parseInt(split[0]);
                String original = split[1];
                String converted = split[2];

                if (original.contains("Euro") && converted.contains("USD")) {
                    double convertedUSD = amount * euroUSD;
                    messageOut = ("The converted amount is: " + convertedUSD);
                    outPacket = new DatagramPacket(messageOut.getBytes(), messageOut.length(), clientAddress, clientPort); //Step 7.
                    dgramSocket.send(outPacket);
                } else if (original.contains("Euro") && converted.contains("GBP")) {
                    double convertedGBP = amount * euroGBP;
                    messageOut = ("The converted amount is: " + convertedGBP);
                    outPacket = new DatagramPacket(messageOut.getBytes(), messageOut.length(), clientAddress, clientPort); //Step 7.
                    dgramSocket.send(outPacket);
                } else if (original.contains("USD") && converted.contains("Euro")){
                    double convertedEuro = amount * USDEuro;
                    messageOut = ("The converted amount is: " + convertedEuro);
                    outPacket = new DatagramPacket(messageOut.getBytes(), messageOut.length(), clientAddress, clientPort); //Step 7.
                    dgramSocket.send(outPacket);
                } else if (original.contains("USD") && converted.contains("CNY")){
                    double convertedCny = amount * USDCny;
                    messageOut = ("The converted amount is: " + convertedCny);
                    outPacket = new DatagramPacket(messageOut.getBytes(), messageOut.length(), clientAddress, clientPort); //Step 7.
                    dgramSocket.send(outPacket);
                } else {
                    messageOut = ("You didn't meet the spec");
                    outPacket = new DatagramPacket(messageOut.getBytes(), messageOut.length(), clientAddress, clientPort); //Step 7.
                    dgramSocket.send(outPacket);
                }
                System.out.println("Message received.");
            } while (true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally { //If exception thrown, close connection.
            System.out.println("\n* Closing connection... *");
            dgramSocket.close(); //Step 9.
        }
    }
}
