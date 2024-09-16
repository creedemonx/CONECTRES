package doctors;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NotificationService {
    private final Map<String, Set<PrintWriter>> familySubscriptions = new HashMap<>();

    public void subscribeFamily(String patientDNI, PrintWriter writer) {
        synchronized (familySubscriptions) {
            familySubscriptions.computeIfAbsent(patientDNI, k -> new HashSet<>()).add(writer);
        }
    }

    public void notifyFamilies(String patientDNI, String message) {
        synchronized (familySubscriptions) {
            Set<PrintWriter> writers = familySubscriptions.get(patientDNI);
            if (writers != null) {
                for (PrintWriter writer : writers) {
                    writer.println("UPDATE: " + message);
                }
            }
        }
    }
}
