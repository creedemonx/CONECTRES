package pacients_info_and_control_for_familys;

import java.util.List;
import java.util.Scanner;

import pacients_info_and_control.Pacient;

public class InfoControl {
    private final List<Pacient> pacients;
    private final InfoChecking infoChecking;
    private final Scanner scanner;

    public InfoControl(List<Pacient> pacients, InfoChecking infoChecking, Scanner scanner) {
        this.pacients = pacients;
        this.infoChecking = infoChecking;
        this.scanner = scanner;
    }

}