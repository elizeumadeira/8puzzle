/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import javax.swing.JOptionPane;

/**
 *
 * @author Elizeu
 */
public class PopUps {

    public static char getChar(String msg) {
        return JOptionPane.showInputDialog(msg).charAt(0);
    }

    public static String getString(String msg) {
        return JOptionPane.showInputDialog(msg);
    }

    public static int getInt(String msg) {
        return Integer.parseInt(JOptionPane.showInputDialog(msg));
    }

    public static void showMessage(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    public static void showMessage(String msg, String titulo) {
        JOptionPane.showMessageDialog(null, msg, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirm(String msg, String title) {
        int reply = JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.YES_NO_OPTION);
        return reply == JOptionPane.YES_OPTION;
    }
    
    public static boolean confirm(String msg) {
        return PopUps.confirm(msg, "");
    }

}