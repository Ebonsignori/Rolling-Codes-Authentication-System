package GUI.validatetextfields; // Package for custom TextField validations

import javafx.scene.control.TextField;

/*
 * Validator class for Number Field in range of Java Int primitives 
 */
public class IntNumberField extends TextField {
    
    // Prompt text to show max input value
    public IntNumberField() {
        this.setPromptText("+" + Integer.toString(Integer.MAX_VALUE));
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
    
    /* Checks that input is within range of int values */
    public void validate() {
        String newValue = this.getText();
        
        if (newValue.matches("[0-9]+")) {
            long value = Long.parseLong(newValue);

            /* If exceeds or goes under range, appropriately set to min or max*/
            if (value < Integer.MIN_VALUE) {
                super.setText(Integer.toString(Integer.MIN_VALUE));
            } else if (value > Integer.MAX_VALUE) {
                super.setText(Integer.toString(Integer.MAX_VALUE));
            }
        }
    }
}
