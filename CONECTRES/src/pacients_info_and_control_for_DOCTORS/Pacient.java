package pacients_info_and_control_for_DOCTORS;

import java.util.ArrayList;
import java.util.List;

public class Pacient {
	
	  protected String name;
	    protected String DNI;
	    protected List<String> patologies;  // Cambiado a una lista de Strings
	    protected int roomNumber;
	    protected boolean roomChanged = false;

	    // Constructor vacío
	    public Pacient() {
	        this.patologies = new ArrayList<>();  // Inicializamos la lista
	    }

	    // Constructor con nombre, DNI y número de habitación
	    public Pacient(String name, String DNI, int roomNumber) {
	        this.name = name;
	        this.DNI = DNI;
	        this.roomNumber = roomNumber;
	        this.patologies = new ArrayList<>();  // Inicializamos la lista
	    }

	    // Constructor con múltiples atributos, incluyendo patologías
	    public Pacient(String name, String DNI, List<String> patologies, int roomNumber, boolean roomChanged) {
	        this.name = name;
	        this.DNI = DNI;
	        this.patologies = new ArrayList<>(patologies);  // Copiamos la lista
	        this.roomNumber = roomNumber;
	        this.roomChanged = roomChanged;
	    }

	    // Getters y setters
	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getDNI() {
	        return DNI;
	    }

	    public void setDNI(String DNI) {
	        this.DNI = DNI;
	    }

	    public List<String> getPatologies() {
	        return new ArrayList<>(patologies);  // Devolvemos una copia para evitar modificaciones externas
	    }

	    public void setPatologies(List<String> patologies) {
	        this.patologies = new ArrayList<>(patologies);  // Asignamos una copia de la lista
	    }

	    public int getRoomNumber() {
	        return roomNumber;
	    }

	    public void setRoomNumber(int roomNumber) {
	        this.roomNumber = roomNumber;
	    }

	    public boolean isRoomChanged() {
	        return roomChanged;
	    }

	    public void setRoomChanged(boolean roomChanged) {
	        this.roomChanged = roomChanged;
	    }

	    // Métodos para manipular las patologías

	    // Añadir una patología a la lista
	    public void addPatology(String patology) {
	        this.patologies.add(patology);
	    }

	    // Eliminar una patología de la lista
	    public void removePatology(String patology) {
	        this.patologies.remove(patology);
	    }

	    // Comprobar si el paciente tiene una patología específica
	    public boolean hasPatology(String patology) {
	        return this.patologies.contains(patology);
	    }
}
