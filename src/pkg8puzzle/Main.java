/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg8puzzle;

import control.Jogo;
import control.Jogo2;
import helpers.GFG;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import model.Nodo;
import view.Tabuleiro;

/**
 *
 * @author Elizeu-pc
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int objetivo[][] = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}
        };
//        int inicial[][] = {
//            {3, 5, 2},
//            {0, 6, 4},
//            {7, 1, 8}
//
//        };
        int inicial[][] = {
            {8, 5, 2},
            {7, 6, 4},
            {3, 1, 0}
        };
//        if (GFG.isSolvable(inicial)) {
            //cria a GUI para o tabuleiro
            Tabuleiro tabuleiro = new Tabuleiro(3, 550, 30);
            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setTitle("Jogo 8-puzzle");
                frame.setResizable(false);
                frame.add(tabuleiro, BorderLayout.CENTER);
                frame.pack();
                // centralizar a tela
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            });
            if (1 == 2) {
                Jogo j = new Jogo(objetivo, inicial, tabuleiro);
                j.run();
            } else {
                Jogo2 j = new Jogo2(objetivo, inicial, tabuleiro);
                j.run();
            }
//       } else {
//            System.out.println("O estado inicial não é solucionável!");
//        }

    }

}
