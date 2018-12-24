package network;

import java.io.IOException;
import java.net.UnknownHostException;

public class testclient {

	public static void main(String[] args) throws UnknownHostException, IOException {
		Client c = new Client();
		c.connect("127.0.0.1", 3333);
		c.send("hllo");
		c.close();

	}

}
