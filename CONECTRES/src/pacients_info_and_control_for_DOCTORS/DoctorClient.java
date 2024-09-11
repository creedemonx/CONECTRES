package pacients_info_and_control_for_DOCTORS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class DoctorClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            // Identificarse como doctor
            out.println("DOCTOR");

            // Simular acciones del doctor
            String action;
            System.out.println("Enter doctor action (e.g., 'add;John Doe;12345678A;0' or 'updateRoom;12345678A;101' or 'addPatology;12345678A;Diabetes'): ");
            while ((action = userInput.readLine()) != null) {
                out.println(action); // Enviar acción al servidor
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}