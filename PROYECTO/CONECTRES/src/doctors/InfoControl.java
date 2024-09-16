package doctors;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import pacients.Pacient;

public class InfoControl {
	private final List<Pacient> pacients;
	private final InfoChecking infoChecking;
	private final Scanner scanner;
	private final NotificationService notificationService; // Cambio para gestionar notificaciones

	public InfoControl(List<Pacient> pacients, InfoChecking infoChecking, Scanner scanner,
			NotificationService notificationService) {
		this.pacients = pacients;
		this.infoChecking = infoChecking;
		this.scanner = scanner;
		this.notificationService = notificationService; // Inyecta el servicio de notificaci�n
	}

	public void addNewUsers() {
		System.out.println("WELCOME");

		System.out.println("What is the patient name?");
		String name = scanner.nextLine();

		System.out.println("What is the patient name?");
		String lastName = scanner.nextLine();
		System.out.println("What is the patient DNI?");
		String DNI = scanner.nextLine();

		if (infoChecking.checkUser(DNI)) {
			System.out.println("This patient already exists");

		} else {
			System.out.println("In wich room will the patient be");
			Scanner sc = new Scanner(System.in);
			int room = sc.nextInt();

			Pacient newPacient = new Pacient(name, DNI, room);
			pacients.add(newPacient);

			addPatologiesToPacient(newPacient);

			System.out.println("PATIENT ADDED");
			System.out.println("THE PATIENT IS IN ROOM: " + newPacient.getRoomNumber());
			String notify = "New pacient added: " + name + " with DNI: " + DNI + " in room: ";
			String notify_2 = "New pacient added: " + newPacient.getRoomNumber();
			// Notificar a todas las familias sobre el nuevo paciente
			notificationService.notifyFamilies(notify, notify_2);
		}
	}

	private void addPatologiesToPacient(Pacient pacient) {
		System.out.println("DOES THE PATIENT HAVE ANY PATHOLOGIES? (Y/N)");
		String hasPatologies = scanner.nextLine();

		while (hasPatologies.equalsIgnoreCase("Y")) {
			System.out.println("ENTER A PATHOLOGY:");
			String patology = scanner.nextLine();
			pacient.addPatology(patology);

			System.out.println("ADD ANOTHER PATHOLOGY? (Y/N)");
			hasPatologies = scanner.nextLine();
		}
	}

	public void consultPatient(String patientDNI) throws IOException {
	    synchronized (pacients) {
	        for (Pacient pacient : pacients) {
	            if (pacient.getDNI().equals(patientDNI)) {
	                System.out.println("Patient found: " + pacient.getName());
	                System.out.println("Patient pathology: " + pacient.getPatologies());
	                System.out.println("Patient room: " + pacient.getRoomNumber());
	                return;
	            }
	        }
	        System.out.println("No patient found with that DNI.");
	    }
	}

	public void checkPacientPatology() {
		System.out.println("Enter the DNI of the patient to consult:");
		String DNI = scanner.nextLine();
		System.out.println("Searching the pacient...");
		Pacient pacient = pacients.stream().filter(p -> p.getDNI().equals(DNI)).findFirst().orElse(null);

		if (pacient != null) {
			System.out.println("Enter the pathology to check:");
			String patology = scanner.nextLine();

			if (infoChecking.checkPacientPatology(pacient, patology)) {
				System.out.println("The patient has the pathology: " + patology);
				System.out.println();
				System.out.println("Do you want to add a new pathology (Y/N)? ");
				String answer = scanner.nextLine();
				if (answer.equalsIgnoreCase("Y")) {
					addPatologiesToPacient(pacient);
					// Notificar a todas las familias sobre la nueva patolog�a
					notificationService.notifyFamilies(pacient.getDNI(),
							"Patient " + pacient.getName() + " has been updated with a new pathology: " + patology);
				} else {
					System.out.println("No new pathology added");
				}

			} else {
				System.out.println("The patient does not have that pathology");
			}
		} else {
			System.out.println("Patient not found ");
		}
	}

	public void changePacientRoom() {
		System.out.println("Enter the DNI of the patient to consult:");
		String DNI = scanner.nextLine();
		System.out.println("Searching for the patient...");

		Pacient pacient = pacients.stream().filter(p -> p.getDNI().equals(DNI)).findFirst().orElse(null);

		if (pacient != null) {
			System.out.println("Enter the new room number for the patient:");

			int newRoomNumber;
			while (true) {
				try {
					newRoomNumber = Integer.parseInt(scanner.nextLine());
					if (newRoomNumber < 0) {
						System.out.println("Room number cannot be negative. Please enter a valid room number:");
					} else {
						break;
					}
				} catch (NumberFormatException e) {
					System.out.println("Invalid input. Please enter a valid room number:");
				}
			}

			pacient.setRoomNumber(newRoomNumber);
			System.out.println(
					"The room number for patient " + pacient.getName() + " has been updated to " + newRoomNumber + ".");

			// Notificar a todas las familias sobre el cambio de habitaci�n
			notificationService.notifyFamilies(pacient.getDNI(),
					"Patient " + pacient.getName() + " has been moved to room: " + newRoomNumber);
		} else {
			System.out.println("No patient found with DNI " + DNI + ".");
		}
	}
}
