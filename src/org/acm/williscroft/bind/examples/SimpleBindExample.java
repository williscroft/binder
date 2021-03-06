package org.acm.williscroft.bind.examples;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.acm.williscroft.bind.Binder;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.EventListener;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
/**
 * An example of using Binder.
 * Largely machine generated by Eclipse WindowBuilder.
 * @author tim
 *
 */
public class SimpleBindExample extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -109045207924842964L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * This is the Observable string that links the fields together
	 */
	private ObservableString ourString = new ObservableString();
	private final JPanel panel_2 = new JPanel();
	private JTextField textField_eventLinked;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SimpleBindExample frame = new SimpleBindExample();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SimpleBindExample() {
		setTitle("Binder example");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		
		JLabel lblEnterTextHere = new JLabel("Enter text here (A)");
		panel.add(lblEnterTextHere);
		
		textField = new JTextField();
		panel.add(textField);
		textField.setColumns(10);
	
		/**
		 * Bind the text field to the ObservableString	
		 * This is a contrived example to simulate a model object updating.
		 * Binders can observe swing document models.
		 * ()->textField.getText() provides human edited text.
		 * (s)->ourString.setText(s) applies that text to the ObservableString.
		 * the textFeild's Document is listened to for changes.
		 */
		new Binder<String>(()->textField.getText(),(s)->ourString.setText(s),textField.getDocument());
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1);
		
		JLabel lblOrHere = new JLabel("(A) Ends up here");
		panel_1.add(lblOrHere);
		
		textField_1 = new JTextField();
		panel_1.add(textField_1);
		textField_1.setColumns(10);
		contentPane.add(panel_2);
		
		JLabel lblPressEnterHere = new JLabel("Press enter in this field to pass the string to (A)");
		panel_2.add(lblPressEnterHere);
		
		textField_eventLinked = new JTextField();
		panel_2.add(textField_eventLinked);
		textField_eventLinked.setColumns(10);
		
		/**
		 * Binders can be ActionListners for text fields, menu items and Buttons.
		 */
		ActionListener l = new Binder<String>(()->textField_eventLinked.getText(),(text)->ourString.setText(text));
		
		textField_eventLinked.addActionListener(l);
		
		JButton btnOrPressThis = new JButton("Or press this button");
		panel_2.add(btnOrPressThis);

		btnOrPressThis.addActionListener(l);
		/**
		 * Here is the Binder being used
		 * Bind ourString to the textField.
		 * ourString->getText() provides a String
		 * textField.setText(text) consumes a String.
		 * ourString is the observable ( to trigger the Binder on data changes. )
		 */
		new Binder<String>(()->ourString.getText(),(text)->textField_1.setText(text),ourString);
	}

}
