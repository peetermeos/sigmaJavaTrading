/**
 * 
 */
package sigma.gui;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

/**
 * Output stream redirect to Swing UI text area
 * 
 * @author Peeter Meos
 * @version 0.1
 *
 */
public class TextAreaOutputStream extends OutputStream{
	private JTextArea myTextArea;

	/* 
	 * Simple constructor for text area output stream
	 * 
	 * @param textArea target for the output stream to go to
	 */
	public TextAreaOutputStream(JTextArea textArea) {
		this.myTextArea = textArea;
	}
	
	/**
	 * Overriden write method
	 */
	@Override
	public void write(int b) throws IOException {
		// Append the line
		myTextArea.append(String.valueOf((char) b));
		// Scroll to the bottom
		myTextArea.setCaretPosition(myTextArea.getDocument().getLength());
	}
}
