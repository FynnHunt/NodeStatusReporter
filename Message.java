
public class Message implements Comparable<Message> {
	
	private long receivedTime;
	private long sentTime;
	private String node1;
	private String notification;
	private String node2 = null;
	
	public Message(long receivedTime, long sentTime, String node1,
			String notification, String node2) {
		this.receivedTime = receivedTime;
		this.sentTime = sentTime;
		this.node1 = node1;
		this.notification = notification;
		this.node2 = node2;
	}
	
	//Overriding compareTo to allow sorting of message objects by sent time
	@Override
	public int compareTo(Message msg) {
		long otherSentTime = msg.getSentTime();
		
		if (sentTime < otherSentTime) {
			return -1;
		} else if (sentTime > otherSentTime) {
			return 1;
		}
		return 0;
	}
	
	public long getReceivedTime() {
		return receivedTime;
	}
	
	public long getSentTime() {
		return sentTime;
	}
	
	public String getNode1() {
		return node1;
	}
	
	public String getNotification() {
		return notification;
	}
	
	public String getNode2() {
		return node2;
	}

}
