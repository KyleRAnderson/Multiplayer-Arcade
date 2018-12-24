package network;

import java.io.IOException;

public class test {

	public static void main(String[] args) throws IOException {
		Server s = new Server();
		s.create();
		s.bind(3333);
		s.accept();
		s.listen();
		
		while (s.getData() == "") {
			System.out.println(s.getData());
		}
		
		s.close();
	}

}
