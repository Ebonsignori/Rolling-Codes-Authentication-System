package GUI.validatetextfields; // Package for custom TextField validations

import java.math.BigInteger;
import javafx.scene.control.TextField;

/*
 * Validator class for Number Field in range of Java Long primitives 
 */
public class LongNumberField extends TextField {
    
    // Prompt text to show max input value
    public LongNumberField() {
        this.setPromptText(Long.toString(Long.MAX_VALUE));
    }
    
    /* replaceText and replaceSelection are necessary for functionality */
    @Override
    public void replaceText(int i, int i1, String string) {
        if (string.matches("[0-9]") || string.isEmpty()) {
            super.replaceText(i, i1, string);
        } 
        this.validate();
    }
        
    @Override
    public void replaceSelection(String string) {
        super.replaceSelection(string);
    }
    
    /* Checks that input is within range of long values */
    public void validate() {
        String newValue = this.getText();
        
        if (newValue.matches("[0-9]+")) {
            // BigInteger used to parse value that is greater than a long value
            BigInteger value = new BigInteger(newValue);

            /* If exceeds or goes under range, appropriately set to min or max*/
            if (value.compareTo(new BigInteger(Long.toString(Long.MIN_VALUE))) == -1) {
                super.setText(Long.toString(Long.MIN_VALUE));
            } else if (value.compareTo(new BigInteger(Long.toString(Long.MAX_VALUE))) == 1) {
                super.setText(Long.toString(Long.MAX_VALUE));
            }
        }
    }
}
