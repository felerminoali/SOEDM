package wsweka;

import java.util.Scanner;

import javax.xml.ws.Endpoint;

public class Publisher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean published = false;

		Endpoint ep = null;
		while (true) {
			
			if (!published) {
				ep = Endpoint.publish("http://localhost:9876/elearningwekaws",
						new ElearningWekaWS());
				System.out
						.println("http://localhost:9876/elearningwekaws?wdsdl published successfuly");
				published = true;
			}
			System.out.println("Press U to unpublish this service: ");
			Scanner scanner = new Scanner(System.in);
			String cmd = scanner.nextLine();

			if (cmd.equals("u")) {
				ep.stop();
				break;
			}
	

		}

	}
}
