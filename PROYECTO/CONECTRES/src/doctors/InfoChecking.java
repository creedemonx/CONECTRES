package doctors;

import java.util.List;

import pacients.Pacient;



public class InfoChecking {
	  private final List<Pacient> pacients;
	  public InfoChecking() {
		this.pacients = null;
			// TODO Auto-generated constructor stub
		}

	    public InfoChecking(List<Pacient> pacients) {
	        this.pacients = pacients;
	    }

	   
	    

		public boolean checkDNI(String DNI) {
	        String lettersDNI = "TRWAGMYFPDXBNJZSQVHLCKE";

	        if (DNI.length() != 9) {
	            return false;
	        }

	        String numbers = DNI.substring(0, 8);
	        char assignedLetter = DNI.charAt(8);
	        try {
	            int numberOfDNI = Integer.parseInt(numbers);
	            int module = numberOfDNI % 23;
	            char correctLetter = lettersDNI.charAt(module);
	            return assignedLetter == correctLetter;
	        } catch (NumberFormatException e) {
	            return false;
	        }
	    }

	    
	    public boolean checkUser(String DNI) {
	        for (Pacient pacient : pacients) {
	            if (pacient.getDNI().equals(DNI)) {
	                return true;
	            }
	        }
	        return false;
	    }

	    public boolean checkRoomNumberChangings (int roomNumber) {
	    	boolean roomChanged = false; 
	    	for(Pacient pacient : pacients) {
	    		if (pacient.getRoomNumber()!=roomNumber) {
	    			pacient.setRoomChanged(true);
	    			roomChanged = true;
	    		}else {
	    			pacient.setRoomChanged(false);
	    		}
	    	}
	    	
	    	
	    	return roomChanged; 
	    }
	   
	    public boolean checkPacientPatology(Pacient pacient, String patology) {
	        return pacient.hasPatology(patology); 
	    }
}
