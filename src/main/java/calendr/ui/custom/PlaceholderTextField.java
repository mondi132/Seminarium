package calendr.ui.custom;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

public class PlaceholderTextField extends JTextField {
	private static final long serialVersionUID = 1L;
	
	String placeholderText;

	public PlaceholderTextField(String text) {
		super(text);
		
		this.placeholderText = text;
		
		this.setForeground(Color.GRAY);
		
		PlaceholderTextField self = this;
		
		this.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				if (self.getRealText().equals(self.placeholderText)) {
					self.setText("");
					self.setForeground(Color.BLACK);
				}
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (self.getRealText().isEmpty()) {
					self.setForeground(Color.GRAY);
					self.setText(self.placeholderText);
		        }
			}
			
		});
	}
	
	public String getRealText() {
		return super.getText();
	}

	@Override
	public String getText() {		
		String realText = this.getRealText();

		if (!realText.equals(placeholderText)) return realText;
		
		return "";
	}
	
	
}
