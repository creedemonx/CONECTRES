package pacients_info_and_control_for_DOCTORS;
import java.util.List;
import java.util.Scanner;

public class InfoControl {
	 private final List<Pacient> pacients;
	    private final InfoChecking infoChecking;
	    private final Scanner scanner;

	    public InfoControl(List<Pacient> pacients, InfoChecking infoChecking, Scanner scanner) {
	        this.pacients = pacients;
	        this.infoChecking = infoChecking;
	        this.scanner = scanner;
	    }

	  
	    public void addNewUsers() {
	        System.out.println("WELCOME, LET'S ASK A FEW QUESTIONS TO CREATE YOUR ACCOUNT");

	        System.out.println("WHAT IS YOUR NAME?");
	        String name = scanner.nextLine();

	        System.out.println("WHAT IS YOUR LASTNAME?");
	        String lastName = scanner.nextLine();
	        System.out.println("WHAT IS YOUR DNI?");
	        String DNI = scanner.nextLine();

	        if (infoChecking.checkUser(DNI)) {
	            System.out.println("THIS USER ALREADY EXISTS. DO YOU WANT TO LOGIN?");
	          
	        } else {
	            System.out.println("SET YOUR PASSWORD");
	            String password = scanner.nextLine(); 

	            Pacient newPacient = new Pacient(name, DNI, 0); 
	            pacients.add(newPacient);

	           
	            addPatologiesToPacient(newPacient);

	            System.out.println("ACCOUNT CREATED");
	            System.out.println("THE PATIENT IS IN ROOM: " + newPacient.getRoomNumber());
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
}
