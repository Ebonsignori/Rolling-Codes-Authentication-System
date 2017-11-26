package GUI.validatetextfields; // Package for custom TextField validations

import javafx.scene.control.TextField;

/*
 * Validator class for Number Field in range of Java Short primitives 
 */
public class ShortNumberField extends TextField {
    
    // Prompt text to show accepted input range
    public ShortNumberField() {
        this.setPromptText("0 through " + Short.toString(Short.MAX_VALUE));
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
    
    /* Checks that value is within range of short integers */
    public void validate() {
        String newValue = this.getText();
        
        if (newValue.matches("[0-9]+")) {
            long value = Long.parseLong(newValue);

            /* If exceeds or goes under range, appropriately set to min or max*/
            if (value < Short.MIN_VALUE) {
                super.setText(Short.toString(Short.MIN_VALUE));
            } else if (value > Short.MAX_VALUE) {
                super.setText(Short.toString(Short.MAX_VALUE));
            }
        }
    }
}
