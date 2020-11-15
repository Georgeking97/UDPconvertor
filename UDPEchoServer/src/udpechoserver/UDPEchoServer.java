
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

    //if split length doesn't equal 3
    //try(split values)
    //catch exception set values to default
    private static void run() {
        try {
            String messageIn, messageOut;
            int numMessages = 0;
            int amount = 0;
            String original = "";
            String converted = "";

            do {
                buffer = new byte[256]; //Step 2.
                inPacket = new DatagramPacket(buffer, buffer.length); //Step 3.
                dgramSocket.receive(inPacket); //Step 4.
                InetAddress clientAddress = inPacket.getAddress(); //Step 5.
                int clientPort = inPacket.getPort(); //Step 5. 
                messageIn = new String(inPacket.getData(), 0, inPacket.getLength()); //Step 6.
                //splitting the message based on spaces
                String[] split = messageIn.split("\\s+");
                //if the message is the expected three variables long
                if (split.length == 3) {
                    try {
                        //parse the first position of the array into an int
                        amount = Integer.parseInt(split[0]);
                        //let the second position of the array equal the original currency & change it to all lower case
                        original = split[1].toLowerCase();
                        //let the third position of the array equal the currency we want to convert to & change it to all lower case
                        converted = split[2].toLowerCase();
                    } catch (Exception e) {
                        //if the message is not the expected three variables long set the variables to default values to enter the if statements
                        amount = 0;
                        original = "";
                        converted = "";
                    }
                } else {
                    //else to close to if, same functionality as the catch in the try catch
                    amount = 0;
                    original = "";
                    converted = "";
                }
                //if the original currency is euro and the currency we want to convert to is usd
                if (original.contains("euro") && converted.contains("usd")) {
                    //the first variable is multiplied by the euroUSD variable which is equal to the rate specified in the assignment
                    double convertedUSD = amount * euroUSD;
                    //this is then let equal to the message out with some basic formatting for the front end user
                    messageOut = ("The converted amount is: " + convertedUSD);
                } else if (original.contains("euro") && converted.contains("gbp")) {
                    double convertedGBP = amount * euroGBP;
                    messageOut = ("The converted amount is: " + convertedGBP);
                } else if (converted.contains("usd") && original.contains("euro")) {
                    double convertedEuro = amount * USDEuro;
                    messageOut = ("The converted amount is: " + convertedEuro);
                } else if (original.contains("usd") && converted.contains("cny")) {
                    double convertedCny = amount * USDCny;
                    messageOut = ("The converted amount is: " + convertedCny);
                } else {
                    //else statement that covers situations where the format the user enters doesn't match any of the above if statements
                    messageOut = ("You didn't meet the spec, please ensure you follow the format");
                }
                System.out.println("Message received.");
                outPacket = new DatagramPacket(messageOut.getBytes(), messageOut.length(), clientAddress, clientPort); //Step 7.
                dgramSocket.send(outPacket);
            } while (true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally { //If exception thrown, close connection.
            System.out.println("\n* Closing connection... *");
            dgramSocket.close(); //Step 9.
        }
    }
}
