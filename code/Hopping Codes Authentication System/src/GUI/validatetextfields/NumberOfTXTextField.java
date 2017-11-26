package GUI.validatetextfields; // Package for custom TextField validations

import javafx.scene.control.TextField;

/* Validator class for Number of TX option textfield */
public class NumberOfTXTextField extends TextField {
    
    // Default Text is 5
    public NumberOfTXTextField() {
        this.setText("5");
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
    
    /* Checks that value is an integer between 1 and 10 */
    public void validate() {
        String newValue = this.getText();
        
        if (newValue.matches("[0-9]+")) {
            int value = Integer.parseInt(newValue);

            /* If exceeds or goes under range, appropriately set to min or max*/
            if (value < 1) {
                super.setText("1");
            } else if (value > 10) {
                super.setText("10");
            }
        }
    }
}
