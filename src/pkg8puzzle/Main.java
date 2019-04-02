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
              {1, 2, 3},
            {4, 0, 6},
            {7, 8, 5}
        };

        Jogo j = new Jogo(objetivo, inicial);
        while (!j.isFinal()){  
            j.jogar();
        }
        System.out.println("Resolvido em " + j.getPassos() + " passos");
        //j.imprimeMaiorFronteira();
        //j.imprimeNodosFechados();
        
        
    }

}
