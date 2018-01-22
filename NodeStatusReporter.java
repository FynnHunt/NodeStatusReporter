/*
 * NodeStatusReporter reads file input for node messages 
 * and outputs each nodes last known status.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class NodeStatusReporter {
	public static void main(String[] args) {
		String filename = args[0];
		File file = new File(filename);
		Scanner sc = null;
		ArrayList<Message> messageList = new ArrayList<Message>();
		HashMap<String, String> nodeMap = new HashMap<String, String>();
		
		long prevSent = 0;
		String prevNotification = null;
		String prevNode2 = null;
		String prevNode1 = null;
		
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		if (sc != null) {
			messageList = ReadFile(sc);
			//Sorting the list of messages by their sent time.
			Collections.sort(messageList);
		}
		sc.close();
		
		/*
		 * Iterating through node hashmap and adding each
		 * node to the map plus their current status
		 */
		for(Message msg : messageList) {
			/* HashMap overwrites duplicates so most recent message
			 * containing each node will be contained in the map for
			 * each node.
			 */
			String status = " ALIVE ";
			/* Checking whether this message was sent within 50ms of previous message and
			 * whether the notifications counter each other. If so setting status to UNKNOWN
			 */
			if (msg.getNode1().equals(prevNode2)) {
				if ((prevNotification.equals("LOST")) && (msg.getNotification().equals("HELLO"))) {
					if ((msg.getSentTime() - prevSent) < 50) {
						status = " UNKNOWN ";
					}
				}
			}			
			
			nodeMap.put(msg.getNode1(), msg.getNode1() + status + 
					msg.getReceivedTime() + " " + msg.getNode1() + " " + 
						msg.getNotification());
			
			//If there is a second node in the message perform the following second node checks
			if (msg.getNode2() != null) {
				status = " ALIVE ";
				//Checking whether this message was sent within 50ms of previous message and
				//whether the notifications counter each other. If so setting status to UNKNOWN
				if (msg.getNode1().equals(prevNode2)) {
					if ((prevNotification.equals("LOST")) && (msg.getNotification().equals("HELLO"))) {
						if ((msg.getSentTime() - prevSent) < 50) {
							status = " UNKNOWN ";
						}
					}
				}	
				
				nodeMap.put(msg.getNode1(), msg.getNode1() + status + 
						msg.getReceivedTime() + " " + msg.getNode1() + " " + 
							msg.getNotification() + " " + msg.getNode2());
				
				if (msg.getNotification().equals("FOUND")) {
					status = " ALIVE ";
					//Checking whether this message was sent within 50ms of previous
					if (msg.getNode2().equals(prevNode2)) {
						if (!msg.getNotification().equals(prevNotification)) {
							if ((msg.getSentTime() - prevSent) < 50) {
								//If within 50ms of previous node and statuses do not match, output
								//is UNKNOWN
								status = " UNKNOWN ";
							}
						}
					}
					
					nodeMap.put(msg.getNode2(), msg.getNode2() + status + 
							msg.getReceivedTime() + " " + msg.getNode1() + " " + 
								msg.getNotification() + " " + msg.getNode2());
				} else if (msg.getNotification().equals("LOST")) {
					status = " DEAD ";
					if (msg.getNode2().equals(prevNode2)) {
						if (!msg.getNotification().equals(prevNotification)) {
							if ((msg.getSentTime() - prevSent) < 50) {
								status = " UNKNOWN ";
							}
						}
					}
					if (msg.getNode2().equals(prevNode1)) {
						if (prevNotification.equals("HELLO")) {
							if ((msg.getSentTime() - prevSent) < 50) {
								status = " UNKNOWN ";
							}
						}
					}
					
					nodeMap.put(msg.getNode2(), msg.getNode2() + status + 
							msg.getReceivedTime() + " " + msg.getNode1() + " " + 
								msg.getNotification() + " " + msg.getNode2());
				}
			}
			/*
			 * Setting required current message data to previous data variables
			 * to compare with next message
			 */
			prevSent = msg.getSentTime();
			prevNode1 = msg.getNode1();
			prevNode2 = msg.getNode2();
			prevNotification = msg.getNotification();
		}
		
		for(Map.Entry<String, String> entry : nodeMap.entrySet()) {
			String msg = entry.getValue();			
			System.out.println(msg);
		}
		
	}
	
	/*
	 * Adds each line in file to a message object and returns
	 * array list of message objects
	 */
	private static ArrayList<Message> ReadFile(Scanner sc) {	
		ArrayList<Message> messageList = new ArrayList<Message>();
		
		/* Iterating over each string in file and creating
		 * Message object with file contents
		*/
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			Scanner lineScanner = new Scanner(line);
			while (lineScanner.hasNext()) {
				try {
					String received = lineScanner.next(); 
					String sent = lineScanner.next();
					String node1 = lineScanner.next();
					String notification = lineScanner.next();
					String node2 = null;
					if (lineScanner.hasNext()) {
						node2 = lineScanner.next();
					}
					
					long receivedVal = Long.parseLong(received);
					long sentVal = Long.parseLong(sent);
					Message message = new Message(receivedVal, sentVal, node1,
							notification, node2);
					
					messageList.add(message);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			lineScanner.close();
		}
		return messageList;
	}
	
}
