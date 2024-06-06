package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/* APUNTES GENERALES:

   Esta clase se emplea justo después de haber presionado una casilla del tablero.
   Su objetivo es que el usuario introduza la palabra que desea colocar, así como
   la dirección en la que quiere que vaya.
*/

public class ChooseWordDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	private static final String[] directions = {"V", "H" };
	
	private Component parent;
	
	private DefaultComboBoxModel<String> directionsModel;
	private JComboBox<String> directionsCombo;
	
	private JTextField wordField;
	
	private JLabel position;
	
	private int status;
	
	ChooseWordDialog(Component parent) {
		this.parent = parent;
		this.setModal(true);
		initGUI();
	}
	
	private void initGUI() {
		
		status = 0;
	
		setTitle("Write a Word");
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		mainPanel.add(northPanel, BorderLayout.NORTH);
		northPanel.setPreferredSize(new Dimension(400, 60));
		
		JLabel label1 = new JLabel("Elige una palabra para escribir en la posición ");
		this.position = new JLabel();
		JLabel label2 = new JLabel("y elige una dirección.");
		northPanel.add(label1);
		northPanel.add(this.position);
		northPanel.add(label2);
		
		JPanel writingAWordOption = new JPanel(new FlowLayout(FlowLayout.CENTER));
		mainPanel.add(writingAWordOption, BorderLayout.CENTER);
		
		JPanel wordOption = new JPanel();
		wordOption.setLayout(new BoxLayout(wordOption, BoxLayout.X_AXIS));
		writingAWordOption.add(wordOption);
		
		wordOption.add(new JLabel("Palabra: "));
		this.wordField = new JTextField();
		this.wordField.setPreferredSize(new Dimension(60, 30));
		wordOption.add(this.wordField);
		
		JPanel directionsOption = new JPanel();
		directionsOption.setLayout(new BoxLayout(directionsOption, BoxLayout.X_AXIS));
		writingAWordOption.add(directionsOption);
		
		directionsOption.add(new JLabel("Dirección: "));
		this.directionsModel = new DefaultComboBoxModel<String>();
		this.directionsCombo = new JComboBox<String>(this.directionsModel);
		directionsOption.add(this.directionsCombo);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
		
		JButton cancel = new JButton();
		cancel.setText("Cancelar");
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				status = 0;
				setVisible(false);
			}
		});
		buttonsPanel.add(cancel);
		
		JButton ok = new JButton();
		ok.setText("OK");
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(wordField.getText() != null && directionsCombo.getSelectedItem() != null) 
					status = 1;
				else  {
					status = 0;
					JOptionPane.showMessageDialog(ChooseWordDialog.this, "Los valores escogidos no son válidos", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
				
				setVisible(false);
			}
		});
		buttonsPanel.add(ok);
		
		setContentPane(mainPanel);
		setMinimumSize(new Dimension(350, 200));
		setPreferredSize(new Dimension(400, 205));
		setVisible(false);
	}
	
	public int open(int posX, int posY) {
		
		status = 0;
		
		this.directionsModel.removeAllElements();
		for(String x : directions)
			this.directionsModel.addElement(x);
		
		this.directionsCombo.setSelectedItem(null);
		
		this.wordField.setText(null);
		
		this.position.setText(String.format("(%s, %s)", posX, posY));
		
		setLocationRelativeTo(parent);
		pack();
		setVisible(true);
		
		return status;
	}

	String getSelectedWord() {
		return this.wordField.getText();
	}

	String getSelectedDirection() {
		return (String) this.directionsCombo.getSelectedItem();
	}
}
