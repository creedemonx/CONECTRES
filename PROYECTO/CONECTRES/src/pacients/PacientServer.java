package pacients;

import java.io.*;
import java.net.*;
import java.util.*;

import family.InfoControl;

public class PacientServer {
	private static List<Socket> familyConnections = new ArrayList<>(); // Lista de conexiones de familiares
	private static List<Pacient> pacients = Collections.synchronizedList(new ArrayList<>()); // Lista de pacientes
	// compartida

	// Mapa para almacenar a qu� pacientes est� suscrito cada cliente
	private static Map<Socket, Set<String>> familySubscriptions = Collections.synchronizedMap(new HashMap<>());

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(5000, 50, InetAddress.getByName("0.0.0.0"));
		System.out.println("Server started and listening on port 5000...");

		while (true) {
			Socket clientSocket = serverSocket.accept(); // Acepta la conexi�n
			new ClientHandler(clientSocket).start(); // Maneja las conexiones en hilos separados
		}
	}

	// Clase interna para manejar clientes (familiares y m�dicos)
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
				if ("DOCTOR".equals(clientType)) {
					handleDoctorActions();
				} else if ("FAMILY".equals(clientType)) {
					handleFamilyConnection();
					handleFamilyActions(); // Manejar las acciones de los familiares
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

		// Maneja las acciones que env�a un doctor
		private void handleDoctorActions() throws IOException {
			String action;
			while ((action = in.readLine()) != null) {
				String[] parts = action.split(";");
				String command = parts[0];

				if ("add".equals(command)) {
					// Agregar nuevo paciente
					String name = parts[1];
					String dni = parts[2];
					int roomNumber = Integer.parseInt(parts[3]);
					Pacient newPacient = new Pacient(name, dni, new ArrayList<>(), roomNumber, false);
					synchronized (pacients) {
						pacients.add(newPacient);
					}
					notifyFamilies(dni, "New patient added: " + name + " with DNI: " + dni + " in room: " + roomNumber);
				} else if ("updateRoom".equals(command)) {
					// Actualizar habitaci�n del paciente
					String dni = parts[1];
					int newRoomNumber = Integer.parseInt(parts[2]);
					synchronized (pacients) {
						for (Pacient pacient : pacients) {
							if (pacient.getDNI().equals(dni)) {
								pacient.setRoomNumber(newRoomNumber);
								notifyFamilies(dni,
										"Patient " + pacient.getName() + " moved to room: " + newRoomNumber);
								break; // Salir del bucle una vez que se encuentra el paciente
							}
						}
					}
				} else if ("addPatology".equals(command)) {
					// Agregar patolog�a a un paciente
					String dni = parts[1];
					String newPatology = parts[2];
					synchronized (pacients) {
						for (Pacient pacient : pacients) {
							if (pacient.getDNI().equals(dni)) {
								pacient.addPatology(newPatology);
								notifyFamilies(dni, "Patient " + pacient.getName() + " diagnosed with: " + newPatology);
								break; // Salir del bucle una vez que se encuentra el paciente
							}
						}
					}
				}
			}
		}

		// A�adir al socket de un familiar a la lista de conexiones
		private void handleFamilyConnection() {
			familyConnections.add(clientSocket);
			familySubscriptions.put(clientSocket, new HashSet<>());
			System.out.println("Family connected.");
		}

		private void consultPatient(String patientDNI) {
			synchronized (pacients) {
				for (Pacient pacient : pacients) {
					if (pacient.getDNI().equals(patientDNI)) {
						try {
							PrintWriter familyOut = new PrintWriter(clientSocket.getOutputStream(), true);
							familyOut.println("Patient found: " + pacient.getName());
							familyOut.println("Patient pathology: " + pacient.getPatologies());
							familyOut.println("Patient room: " + pacient.getRoomNumber());
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;
					}
				}
				try {
					PrintWriter familyOut = new PrintWriter(clientSocket.getOutputStream(), true);
					familyOut.println("No patient found with that DNI.");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// Manejar las acciones que env�a un familiar
		private void handleFamilyActions() throws IOException {
			String action;
			while ((action = in.readLine()) != null) {
				String[] parts = action.split(";");
				String command = parts[0];

				if ("FOLLOW".equals(command)) {
					String patientDNI = parts[1];
					Set<String> subscriptions = familySubscriptions.get(clientSocket);
					if (subscriptions != null) {
						subscriptions.add(patientDNI);
						System.out.println("Family client subscribed to patient with DNI: " + patientDNI);
					}
				} else if ("CONSULT".equals(command)) {
					String patientDNI = parts[1];
					consultPatient(patientDNI); // M�todo que implementa la consulta
				}
			}
		}

		// Notificar a todos los familiares conectados
		private void notifyFamilies(String patientDNI, String updateMessage) throws IOException {
			synchronized (familySubscriptions) {
				for (Map.Entry<Socket, Set<String>> entry : familySubscriptions.entrySet()) {
					Socket familySocket = entry.getKey();
					Set<String> subscriptions = entry.getValue();
					if (subscriptions.contains(patientDNI)) {
						PrintWriter familyOut = new PrintWriter(familySocket.getOutputStream(), true);
						familyOut.println("UPDATE: " + updateMessage);
					}
				}
			}
		}
	}
}
