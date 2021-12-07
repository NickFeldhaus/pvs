package feldhaus.lab1;
import java.net.*;
import java.io.*;
import java.util.Objects;

public class SimpleClient { //Blockierender Client, wartet auf Konvertierungsergebnis


    public static void main(String[] args) {
        String s="Das Pferd frisst Gurkensalat\n";
        String finish="ENDE\n";
        String upper="#UPPER#\n";
        String lower="#LOWER#\n";
        try
        {
            Socket talkSocket = new Socket("localhost", 4711);
            BufferedReader fromServer = new BufferedReader(
                    new InputStreamReader (
                            talkSocket.getInputStream(),"Cp1252"));
            OutputStreamWriter toServer =
                    new OutputStreamWriter(
                            talkSocket.getOutputStream(), "Cp1252");
            System.out.println("Send: "+lower);
            toServer.write(lower);
            System.out.println("Send: "+s);
            toServer.write(s);
            System.out.println("Send: "+finish);
            toServer.write(finish);
            //System.out.println("Send: "+s+finish+s);
            //toServer.write(s+finish+s);

            toServer.flush(); // force message to be sent
            String result = fromServer.readLine(); // blocking read
            while(!Objects.equals(result, null)){
                System.out.println("Receive: "+result);
                result = fromServer.readLine();
            }
            toServer.close(); // close writer
            fromServer.close(); // close reader
            talkSocket.close(); // close socket (necessary??)
        } catch (Exception e) { e.printStackTrace(); }

    }
}
