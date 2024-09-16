package doctors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import pacients.Pacient;

public class DoctorClient {
	   private static final String SERVER_ADDRESS = "localhost";
	    private static final int SERVER_PORT = 5000;

	    public static void main(String[] args) {
	        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
	             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	             Scanner scanner = new Scanner(System.in)) {

	            // Identificarse como doctor
	            out.println("DOCTOR");

	            String command;
	            while (true) {
	                System.out.println("Enter command: add, updateRoom, addPatology, exit");
	                command = scanner.nextLine();

	                if ("exit".equalsIgnoreCase(command)) {
	                    break;
	                }

	                switch (command.toLowerCase()) {
	                    case "add":
	                        // Agregar nuevo paciente
	                        System.out.println("Enter patient name:");
	                        String name = scanner.nextLine();
	                        System.out.println("Enter patient DNI:");
	                        String dni = scanner.nextLine();
	                        System.out.println("Enter patient room number:");
	                        int roomNumber = Integer.parseInt(scanner.nextLine());
	                        out.println("add;" + name + ";" + dni + ";" + roomNumber);
	                        break;
	                    case "updateroom":
	                        // Actualizar habitaci�n del paciente
	                        System.out.println("Enter patient DNI:");
	                        dni = scanner.nextLine();
	                        System.out.println("Enter new room number:");
	                        roomNumber = Integer.parseInt(scanner.nextLine());
	                        out.println("updateRoom;" + dni + ";" + roomNumber);
	                        break;
	                    case "addpatology":
	                        // Agregar patolog�a
	                        System.out.println("Enter patient DNI:");
	                        dni = scanner.nextLine();
	                        System.out.println("Enter new patology:");
	                        String patology = scanner.nextLine();
	                        out.println("addPatology;" + dni + ";" + patology);
	                        break;
	                    default:
	                        System.out.println("Invalid command. Please try again.");
	                        break;
	                }
	            }
	            // Cerrar el socket de manera ordenada
	            socket.close();

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
}