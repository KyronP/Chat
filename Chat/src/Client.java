import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Label;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;


public class Client {

	static JFrame guiFrame = new JFrame();

	static Socket socket;
	static DataInputStream in;
	static DataOutputStream out;

	private static JButton messageInputButton;
	private static JTextField messageInputField;
	private static JPanel messageInputPanel;

	

	public static JTextPane messageBox;
	public static Document textDoc;
	public static void main(String[] args) throws UnknownHostException,
			IOException, BadLocationException {

		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrame.setTitle("chat GUI");
		guiFrame.setSize(710, 300);
		 
		FlowLayout flowLayout = new FlowLayout(700);

		guiFrame.setLayout(flowLayout);
		guiFrame.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

	


		messageInputField = new JTextField(20);

		messageInputPanel = new JPanel();
		messageInputPanel.setLayout(new FlowLayout());

		messageInputPanel.add(messageInputField);

		JOptionPane optionPane = new JOptionPane();
		
		
		
		Action sendAction = new AbstractAction("Send") {
			public void actionPerformed(ActionEvent e) {
				try {
		
					
						out.writeUTF(messageInputField.getText());
					
				//	textDoc.insertString(textDoc.getLength(), "", null);
					textDoc.remove(0, textDoc.getLength());
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block

				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		};

		messageInputField.setAction(sendAction);
		messageInputButton = new JButton(sendAction);// Caption start
		messageInputPanel.add(messageInputButton);
		textDoc = Client.messageInputField.getDocument();
		
		messageInputPanel.setBounds(0, 0, 700, 300);

		messageBox = new JTextPane();
		messageBox.setEditable(false);

		JPanel messagePanel = new JPanel();
		JScrollPane scrollPane = new JScrollPane(messageBox);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		scrollPane.setPreferredSize(new Dimension(675, 200));
		scrollPane.setBounds(0, 0, 700, 200);

		messagePanel.add(scrollPane);

		guiFrame.getContentPane().add(messagePanel, BorderLayout.NORTH);

		messageInputPanel.setPreferredSize(new Dimension(700, 50));

		guiFrame.add(messageInputPanel);
		// //////////////////////////////////////////////////////////////////////
		Client.guiFrame.setVisible(true);

		
		
		
		
		System.out
				.println("Client Socket trying to connect to server:7778 ...");

		Document yourDoc = Client.messageBox.getDocument();
		yourDoc.insertString(yourDoc.getLength(),
				"Client Socket trying to connect to server..." + "\n", null);

		messageBox.setCaretPosition(yourDoc.getLength());
		DefaultCaret caret = (DefaultCaret) messageBox.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		socket = new Socket("84.30.122.125", 7778);

		System.out.println("Connection established succesfully");

		yourDoc.insertString(yourDoc.getLength(), "Connection Established..."
				+ "\n", null);
		yourDoc.insertString(yourDoc.getLength(),
				"To change your name, typ /nick [NEWNAME]"
						+ "\n", null);


		in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());


		  JFrame frame = new JFrame("Enter your name");

		    // prompt the user to enter their name
		    String name = JOptionPane.showInputDialog(frame, "What's your name?");

		    if (!name.isEmpty()){
		    out.writeUTF("/nick "+name);
		    }else{
		    	  out.writeUTF("/nick guest");
		    }
		
		


		Input input = new Input(in);
		Thread thread = new Thread(input);
		thread.start();

	}

}

class Input implements Runnable {

	DataInputStream in;

	protected static JLabel instructions;

	public Input(DataInputStream in) {
		this.in = in;
	}

	public void run() {
		while (true) {
			String message;
			try {
				message = in.readUTF();
				Document yourDoc = Client.messageBox.getDocument();
				yourDoc.insertString(yourDoc.getLength(), message + "\n", null);

			} catch (IOException e) {
				e.printStackTrace();
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}



