/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userinterface;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Frame;
/**
 *
 * @author Jon
 */
public class UserInterface extends Frame{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new UserInterface();
    }
    public UserInterface(){
        JFrame userInterface = new JFrame();
        userInterface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        userInterface.setTitle("SeeFood AI UserInterface");
        userInterface.setSize(800, 750);
        userInterface.setLocationRelativeTo(null);
        JButton userButton = new JButton("Add Image to Client");
        userButton.addActionListener(new ActionListener(){
           @Override 
            public void actionPerformed(ActionEvent event) {
                
            }
        });
        userInterface.add(userButton,BorderLayout.SOUTH);
        userInterface.setVisible(true);
    }
}
