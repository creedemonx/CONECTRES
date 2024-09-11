package pacients_info_and_control_for_familys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class FamilyClient {
	public static void main(String[] args) {
		try (Socket socket = new Socket("localhost", 5000);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

			out.println("FAMILY");

			String updateMessage;
			while ((updateMessage = in.readLine()) != null) {
				System.out.println("Notification: " + updateMessage);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}