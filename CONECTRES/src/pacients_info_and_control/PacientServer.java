package pacients_info_and_control;

import java.io.*;
import java.net.*;
import java.util.*;

import pacients_info_and_control_for_DOCTORS.Pacient;

public class PacientServer {
 private static List<Socket> familyConnections = new ArrayList<>(); // Lista de conexiones de familiares
 private static List<Pacient> pacients = Collections.synchronizedList(new ArrayList<>()); // Lista de pacientes compartida

 public static void main(String[] args) throws IOException {
     ServerSocket serverSocket = new ServerSocket(5000); // Puerto donde escucha el servidor
     System.out.println("Server started and listening on port 5000...");

     while (true) {
         Socket clientSocket = serverSocket.accept(); // Acepta la conexión
         new ClientHandler(clientSocket).start(); // Maneja las conexiones en hilos separados
     }
 }

 // Clase interna para manejar clientes (familiares y médicos)
 static class ClientHandler extends Thread {
     private Socket clientSocket;
     private PrintWriter out;
     private BufferedReader in;

     public ClientHandler(Socket socket) {
         this.clientSocket = socket;
     }

     public void run() {
         try {
             in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             out = new PrintWriter(clientSocket.getOutputStream(), true);

             // Leer el tipo de cliente (doctor o familiar)
             String clientType = in.readLine();
             if (clientType.equals("DOCTOR")) {
                 handleDoctorActions();
             } else if (clientType.equals("FAMILY")) {
                 handleFamilyConnection();
             }

         } catch (IOException e) {
             e.printStackTrace();
         } finally {
             try {
                 in.close();
                 out.close();
                 clientSocket.close();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
     }

     // Maneja las acciones que envía un doctor
     private void handleDoctorActions() throws IOException {
         String action;
         while ((action = in.readLine()) != null) {
             // Ejemplo de acción: add;John Doe;12345678A;0
             // Separar los campos de la acción
             String[] parts = action.split(";"); //las acciones se agregarán en el orden del constructor : String name, String DNI, int roomNumber
             String command = parts[0];

             if (command.equals("add")) {
                 // Agregar nuevo paciente
                 String name = parts[1];
                 String dni = parts[2];
                 int roomNumber = Integer.parseInt(parts[3]);
                 Pacient newPacient = new Pacient(name, dni, new ArrayList<>(), roomNumber, false); // Ejemplo
                 synchronized (pacients) {
                     pacients.add(newPacient); // Agregar paciente a la lista
                 }
                 notifyFamilies("New pacient added: " + name + " with DNI: " + dni);
             } else if (command.equals("updateRoom")) {
                 // Actualizar habitación del paciente
                 String dni = parts[1];
                 int newRoomNumber = Integer.parseInt(parts[2]);
                 synchronized (pacients) {
                     for (Pacient pacient : pacients) {
                         if (pacient.getDNI().equals(dni)) {
                             pacient.setRoomNumber(newRoomNumber);
                             notifyFamilies("Pacient " + pacient.getName() + " moved to room: " + newRoomNumber);
                         }
                     }
                 }
             } else if (command.equals("addPatology")) {
                 // Agregar patología a un paciente
                 String dni = parts[1];
                 String newPatology = parts[2];
                 synchronized (pacients) {
                     for (Pacient pacient : pacients) {
                         if (pacient.getDNI().equals(dni)) {
                             pacient.addPatology(newPatology);
                             notifyFamilies("Pacient " + pacient.getName() + " diagnosed with: " + newPatology);
                         }
                     }
                 }
             }
         }
     }

     // Añadir al socket de un familiar a la lista de conexiones
     private void handleFamilyConnection() {
         familyConnections.add(clientSocket);
         System.out.println("Family connected.");
     }

     // Notificar a todos los familiares conectados
     private void notifyFamilies(String updateMessage) throws IOException {
         synchronized (familyConnections) {
             for (Socket familySocket : familyConnections) {
                 PrintWriter familyOut = new PrintWriter(familySocket.getOutputStream(), true);
                 familyOut.println("UPDATE: " + updateMessage);
             }
         }
     }
 }
}
