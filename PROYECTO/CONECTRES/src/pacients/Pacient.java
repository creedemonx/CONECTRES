package pacients;

import java.util.ArrayList;
import java.util.List;

public class Pacient {
    protected String name;
    protected String DNI;
    protected List<String> patologies;
    protected int roomNumber;
    protected boolean roomChanged = false;

    public Pacient() {
        this.patologies = new ArrayList<>();
    }

    public Pacient(String name, String DNI, int roomNumber) {
        this.name = name;
        this.DNI = DNI;
        this.roomNumber = roomNumber;
        this.patologies = new ArrayList<>();
    }

    public Pacient(String name, String DNI, List<String> patologies, int roomNumber, boolean roomChanged) {
        this.name = name;
        this.DNI = DNI;
        this.patologies = new ArrayList<>(patologies);
        this.roomNumber = roomNumber;
        this.roomChanged = roomChanged;
    }

    // Getters and setters
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
        return new ArrayList<>(patologies);
    }

    public void setPatologies(List<String> patologies) {
        this.patologies = new ArrayList<>(patologies);
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

    public void addPatology(String patology) {
        this.patologies.add(patology);
    }

    public void removePatology(String patology) {
        this.patologies.remove(patology);
    }

    public boolean hasPatology(String patology) {
        return this.patologies.contains(patology);
    }
}
