package network;

import java.io.IOException;


public class test {

	public static void main(String[] args) throws IOException, InterruptedException {
		Server s = new Server();
		s.create(3333);
		s.accept();
		System.out.println(s.listenForData());
		s.close();
	}
}
