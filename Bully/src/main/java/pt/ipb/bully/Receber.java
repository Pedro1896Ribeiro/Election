package pt.ipb.bully;

import java.util.List;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;

public class Receber extends ReceiverAdapter {
	
	private JChannel channel;
	private Eleicao eleicao;
	
	private boolean okReceived = false;
	
	public Receber(JChannel new_channel, Eleicao new_eleicao) throws Exception {
		channel = new_channel;
		eleicao = new_eleicao;
		this.start();
	}
	
	public void start() throws Exception {
	    channel.setReceiver(this);
	    channel.connect("Election");
	}
	
	public void receive(Message msg) {
			if (msg.getObject().toString().contains("I Competed"))
				if (msg.getSrc().compareTo(channel.address()) > 0) {
					try {
						Message msg_responde = new Message(msg.getSrc(), "OK!");
						channel.send(msg_responde);
						
						eleicao.comecarEleicao();
					} catch(Exception e) {}
				}
			if (msg.getObject().toString().contains("OK!"))
				okReceived = true;
			if (msg.getObject().toString().contains("I Won")) {
				eleicao.setMasterAtual(msg.getSrc());
				System.out.println(eleicao.getMasterAtual());
			}			
	}
	
	public void viewAccepted(View new_view) {
		if (!new_view.containsMember(eleicao.getMasterAtual())) {
			eleicao.setMasterAtual(null);
		}
		Eleicao.setMembrosConhecidos(new_view.getMembers());
	}
	
	public void loop() {
		while(true) {
			if (eleicao.last_message != 0 && (System.currentTimeMillis() - eleicao.last_message) > 2000 && !okReceived) {
				eleicao.setMasterAtual(channel.getAddress());
			}
		}
	}
}
