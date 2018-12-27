package network;

import java.io.IOException;
import java.net.UnknownHostException;

public class testclient {

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		Thread.sleep(5000);
		Client c = new Client();
		c.connect("127.0.0.1", 3333);
		c.send("hell");
		Thread.sleep(100);
		c.close();
		c.connect("127.0.0.1", 3333);
		c.send("hellss");
		c.close();
	}

}
