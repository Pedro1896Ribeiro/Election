package pt.ipb.bully;

import java.util.LinkedList;
import java.util.List;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;

public class Eleicao {
	
	private JChannel channel;
	
	private Address Master_Atual;
	
	public static long last_message;
	
	private static List<Address> Membros_conhecidos;

	public Eleicao(JChannel new_channel) {
		channel = new_channel;
		Master_Atual = null;
		last_message = 0;
	}
	
	public void comecarEleicao() {
		List<Address> maiores = this.checkWhoIsBigger();
		try {
			if (maiores != null) {
				for (Address address : maiores) {
					Message msg = new Message(address, "I Competed");
					channel.send(msg);
					last_message = System.currentTimeMillis();
				}
			}
			else {
				Message msg = new Message(null, "I Won");
				channel.send(msg);
			}
		} catch(Exception e) {}
	}
	
	public Address getMasterAtual() {
		return Master_Atual;
	}

	public void setMasterAtual(Address master_Atual) {
		Master_Atual = master_Atual;
	}

	public static List<Address> getMembrosConhecidos() {
		return Membros_conhecidos;
	}

	public static void setMembrosConhecidos(List<Address> membros_conhecidos) {
		Membros_conhecidos = membros_conhecidos;
	}
	
	public boolean MasterOnline() {
		if (Master_Atual == null)
			return false;
		else {
			return channel.getView().containsMember(Master_Atual);
		}
	}
	
	public List<Address> checkWhoIsBigger() {
		List<Address> maiores = new LinkedList<Address>();
		for(int i=0; i<Membros_conhecidos.size() ;i++) {
			if (channel.getAddress().compareTo(Membros_conhecidos.get(i)) > 0)
				maiores.add(Membros_conhecidos.get(i));
		}
		if (maiores.size() == 0)
			maiores = null;
		return maiores;
	}
}
