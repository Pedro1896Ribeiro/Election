package pt.ipb.bully;

import java.awt.EventQueue;

import javax.swing.JFrame;

import org.jgroups.JChannel;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import java.awt.Font;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Application {

	private JFrame frame;
	private static JChannel channel;
	private static Eleicao eleicao;
	private static Receber receber;
	private static JLabel lblNewLabel;
	private Timer timer;
	

	/**
	 * Launch the application.
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		channel = new JChannel();
		eleicao = new Eleicao(channel);
		receber = new Receber(channel, eleicao);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Application window = new Application();
					window.frame.setVisible(true);
					updateCoordenador();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Application() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 250);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		lblNewLabel = new JLabel("New label");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 49, 414, 34);
		frame.getContentPane().add(lblNewLabel);		
		
		JButton btnNewButton = new JButton("Start Election");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				eleicao.comecarEleicao();
			}
		});
		btnNewButton.setBounds(10, 160, 414, 45);
		frame.getContentPane().add(btnNewButton);
	}
	
	public static void updateCoordenador() {
		ActionListener action = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (eleicao.getMasterAtual() != null) {
					lblNewLabel.setText(eleicao.getMasterAtual().toString());
				}
				else {
					lblNewLabel.setText("Without Coordinator.");
				}
			}
		};
		Timer t = new Timer(10,action);
		t.start();
	}
}
