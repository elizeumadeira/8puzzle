/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import helpers.Helper;
import java.util.Arrays;
import java.util.List;
import model.Nodo;

/**
 *
 * @author Elizeu-pc
 */
public class Jogo {

    private Nodo nodos_abertos;
    private List<Nodo> nodos_fechados;
    private Nodo estado;
    private Nodo objetivo;

    public Jogo(int[][] objetivo, int[][] estado) {
        this.estado = new Nodo(estado);
        this.objetivo = new Nodo(objetivo);
    }

    public Jogo(Nodo objetivo, Nodo estado) {
        this.estado = estado;
        this.objetivo = objetivo;
    }

    public void escreveArray(int[] a) {
        String r = "";
        for (int i = 0; i < a.length; i++) {
            r += a[i] + "|";
        }
        System.out.println(r);
    }

    public int inArray(int[] a, int n) {
        for (int i = 0; i < a.length; i++) {
            if (n == a[i]) {
                return i;
            }
        }
        return -1;
    }

    public int somaHeuristica(int[][] m) {
        int he = 0;

        //começa a percorrer a matriz inserida
        for (int i = 0; i < 9; i++) {
            
            int linha = i / 3;
            int coluna = (int) i % 3;

            int linha_n = 0;
            int coluna_n = 0;
            int n = m[linha][coluna];

            //n devia estar na linha...
            for (int j = 0; j < 3; j++) {
//                this.escreveArray(this.objetivo.getMatriz()[j]);

                if (this.inArray(this.objetivo.getMatriz()[j], n) == -1) {
                    continue;
                }

                linha_n = j;
            }

            //n devia estar na coluna...
            for (int j = 0; j < 3; j++) {
                int[] col = {
                    this.objetivo.getMatriz()[0][j],
                    this.objetivo.getMatriz()[1][j],
                    this.objetivo.getMatriz()[2][j]
                };

//                System.out.println(this.inArray(col, n) + " " + n);
//                System.out.println("-----------");
                if (this.inArray(col, n) == -1) {
                    continue;
                }

                coluna_n = j;
            }

            //compara com a matriz objetivo
            //soma quantas linhas + quantas colunas o numero atual esta fora
//            for (int j = 0; j < 9; j++) {
//                int linha_obj = j / 3;
//                int coluna_obj = (int) j % 3;
//                int n_obj = this.objetivo.getMatriz()[linha_obj][coluna_obj];
//
//                System.out.println(n +" - "+ n_obj +" = "+(n_obj != n ? he+1 : he));
//                if (n_obj == n) {
//                    break;
//                } else {
//                    he++;
//                }
//            }
            //soma a distancia até a posição correta
            //for (int j = 0; j < 9; j++) {
            //}
            //mesma linha e coluna, nada a fazer
            if (linha == linha_n && coluna == coluna_n) {
                he += 0;
            } else //esta ao lado ou na diagonal
            if (Math.abs(linha - linha_n) <= 1 && Math.abs(coluna - coluna_n) <= 1) {
                he += 1;
            } else//esta a duas casas de distancia
            {
                he += 2;
            }
//            System.out.println(n + "-" + linha_n + "-" + coluna_n+" ==> "+Math.abs(linha - linha_n) + " = "+Math.abs(coluna - coluna_n));

        }

        return he;
    }

    boolean isFinal() {
        return nodos_abertos.equals(this.estado);
    }

    public void jogar() {
        System.out.println(this.estado);
        System.out.println("Heuristica " + this.somaHeuristica(this.estado.getMatriz()));
    }

}
