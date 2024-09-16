package family;

import java.io.*;
import java.net.Socket;

public class FamilyClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            out.println("FAMILY"); // Identificar el tipo de cliente

            System.out.println("Enter the DNI of the patient you want to follow:");
            String patientDNI = userInput.readLine();
            out.println("FOLLOW;" + patientDNI);

            Thread userInputThread = new Thread(() -> {
                try {
                    String userCommand;
                    while ((userCommand = userInput.readLine()) != null) {
                        if ("exit".equalsIgnoreCase(userCommand)) {
                            System.out.println("Closing connection...");
                            socket.close();
                            break;
                        } else if ("consult".equalsIgnoreCase(userCommand)) {
                            out.println("CONSULT;" + patientDNI); // Enviar comando de consulta
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            userInputThread.start();

            String updateMessage;
            while ((updateMessage = in.readLine()) != null) {
                System.out.println("Notification: " + updateMessage);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
