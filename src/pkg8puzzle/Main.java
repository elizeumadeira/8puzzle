/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg8puzzle;

import control.Jogo;

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

        int inicial[][] = {
            {8, 5, 2},
            {7, 6, 4},
            {3, 1, 0}
        };

        Jogo j = new Jogo(objetivo, inicial);
        j.jogar();
    }

}
