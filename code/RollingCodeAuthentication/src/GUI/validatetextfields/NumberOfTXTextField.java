/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.validatetextfields;

import javafx.scene.control.TextField;

public class NumberOfTXTextField extends TextField {
    
    public NumberOfTXTextField() {
        this.setText("5");
    }
    
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
    
    public void validate() {
        String newValue = this.getText();

        int value = Integer.parseInt(newValue);

        // Number of TX must be a digit and between 1 and 10
        if (value < 1) {
            super.setText("1");
        } else if (value > 10) {
            super.setText("10");
        }
    }
}
