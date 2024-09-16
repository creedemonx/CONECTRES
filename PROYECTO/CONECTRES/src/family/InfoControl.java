package family;

import java.util.List;
import java.util.Scanner;

import pacients.Pacient;

public class InfoControl {
    private final List<Pacient> pacients;
    private final InfoChecking infoChecking;
    private final Scanner scanner;

    public InfoControl(List<Pacient> pacients, InfoChecking infoChecking, Scanner scanner) {
        this.pacients = pacients;
        this.infoChecking = infoChecking;
        this.scanner = scanner;
    }
    
    
    public void consultPatient(String patientDNI) {
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