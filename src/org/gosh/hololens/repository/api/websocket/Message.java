package org.gosh.hololens.repository.api.websocket;

public class Message {

	private boolean direct;
	private String sender;
	private String mesh;
	private double x,y,z;
	
	
	
	public Message() {
		super();
	}

	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getContent() {
		return mesh;
	}
	public void setContent(String content) {
		this.mesh = content;
	}


	public double[] getPosition() {
		return new double[]{x,y,z};
	}

	public void setPosition(double x,double y,double z) {
		this.x=x;
		this.y=y;
		this.z=z;
	}

	public boolean isDirect() {
		return direct;
	}

	public void setDirect(boolean direct) {
		this.direct = direct;
	}
}
