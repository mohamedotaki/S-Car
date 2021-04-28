import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SMSUsers extends Thread implements Observer{

	private final Subject subject;
	private String desc;
	private final SocketChannel socketChannel;
	private final String userInfo;
	private final Observer observer;

	
	public SMSUsers(Subject subject, String userInfo, SocketChannel socketChannel){

		if(subject==null){
			throw new IllegalArgumentException("No Publisher found.");
		}
		this.socketChannel = socketChannel;
		this.subject = subject;
		this.userInfo = userInfo;
		observer = SMSUsers.this;
		observer.subscribe();
		observer.update("Welcome to live Soccer match to unSubscribe Please Type 1 or unSubscribe");
	}

	public void run(){
		while(true){
		if(receiveMessage(socketChannel).equalsIgnoreCase("1")){
			observer.update("Unsubscribed successfully. To subscribed type 2 or subscribed");
			observer.unSubscribe();
		}if(receiveMessage(socketChannel).equalsIgnoreCase("2")){
			observer.update("subscribed successfully. To Unsubscribed type 1 or Unsubscribed");
			observer.subscribe();
			}
		}
	}


	@Override
	public void update(String desc) {
		this.desc = desc;
		display();
	}
	
	
	private void display()   {
		try {
			sendMessage(socketChannel,userInfo+"\t"+desc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void subscribe() {
		System.out.println("Subscribing "+userInfo+" to "+subject.subjectDetails()+" ...");
		this.subject.subscribeObserver(this);
		System.out.println("Subscribed successfully.");
	}

	@Override
	public void unSubscribe() {
		System.out.println("Unsubscribing "+userInfo+" to "+subject.subjectDetails()+" ...");
		this.subject.unSubscribeObserver(this);
		System.out.println("Unsubscribed successfully.");
	}
	private void sendMessage(SocketChannel socketChannel, String message)throws IOException{
		ByteBuffer buffer = ByteBuffer.allocate(16384);
		buffer.put(message.getBytes());
		buffer.put((byte) 0x00);
		buffer.flip();
		while (buffer.hasRemaining()) {
			socketChannel.write(buffer);
		}
		buffer.clear();
	}

	private String receiveMessage(SocketChannel sChannel){
		String message = "";
		try {
			ByteBuffer byteBuffer = ByteBuffer.allocate(16384);
			while (sChannel.read(byteBuffer) > 0) {
				byteBuffer.flip();
				char byteRead = 0x00;
				while (byteBuffer.hasRemaining()) {
					byteRead = (char) byteBuffer.get();
					if (byteRead == 0x00) break;
					message += byteRead;
				}
				if (byteRead == 0x00) break;
				byteBuffer.clear();
			}

			byteBuffer.clear();
		}catch (IOException e){}
		return message;
	}
}
