/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg8puzzle;

import control.Jogo;
import model.Nodo;

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
            {3, 5, 2},
            {0, 6, 4},
            {7, 1, 8}
        
        };
        /*int inicial[][] = {
            {8, 5, 2},
            {7, 6, 4},
            {0, 1, 3}
        };*/

        Jogo j = new Jogo(objetivo, inicial);
//        while (!j.isFinal()){  
        j.jogar();
//        }
        Nodo solucao = j.getEstado();
          System.out.println("Resolvido em " + j.getEstado().getCusto()+ " passos");
        //j.imprimeMaiorFronteira();
        //j.imprimeNodosFechados();
        
      //  while (solucao!=null) {
       //     System.out.println(solucao);
       //     solucao = solucao.getPai();
       // }
      
        
    }

}
