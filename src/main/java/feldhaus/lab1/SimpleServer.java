package feldhaus.lab1;

import java.net.*;
import java.io.*;
import java.util.Objects;

public class SimpleServer { //Serieller Server, bedient alle Clientanfrangen aneinander

    public static void main(String[] args) {
        loop();
    }

    private static void loop() {
        try
        {
            ServerSocket listenSocket = new ServerSocket(4711);
            while (true) // server loop
            {
                serveClient(listenSocket);
            }
        }
        catch (IOException e) {e.printStackTrace();}
    }

    private static void serveClient(ServerSocket listenSocket) throws IOException {
        Socket talkSocket;
        BufferedReader fromClient ;
        OutputStreamWriter toClient;
        String stringToConvert;
        Float convertCost = 1.5f;
        Float currentCost;
        Boolean lower;

        lower = false;
        System.out.println("Connection request from address 193.175.197.111 port 2465");
        talkSocket = listenSocket.accept(); // blocking wait
        // incoming messages are char based (text)
        fromClient = new BufferedReader(new InputStreamReader(
                talkSocket.getInputStream(), "Cp1252"));
        // outgoing messages are char based (text)
        toClient = new OutputStreamWriter(
                talkSocket.getOutputStream(), "Cp1252");
        stringToConvert = fromClient.readLine();
        if(stringToConvert.equals("#LOWER#")){
            lower = true;
            stringToConvert = fromClient.readLine();
        }
        while(!Objects.equals(stringToConvert, null)){
            //System.out.println("Vor dem equals. Der string lautet: " + stringToConvert);
            if(stringToConvert.equals("ENDE")){
                //System.out.println("Springt rein in stringToConvert");
                currentCost = stringToConvert.length() * convertCost;
                toClient.write("Server shutdown "+ currentCost +"Ct" +'\n');
                toClient.close();   // close writer
                fromClient.close(); // close reader
                talkSocket.close();
                listenSocket.close();
                return;
            }
            currentCost = stringToConvert.length() * convertCost;
            if(lower){
                toClient.write(stringToConvert.toLowerCase() + " " + currentCost + "Ct" + '\n');
            } else {
                toClient.write(stringToConvert.toUpperCase() + " " + currentCost + "Ct" + '\n');
            }
            stringToConvert = fromClient.readLine();
        }
        toClient.close();   // close writer
        fromClient.close(); // close reader
        talkSocket.close(); // close talksocket
        // not listensocket !!!
    }
}
