/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Arrays;

/**
 *
 * @author Elizeu-pc
 */
public class Nodo {

    private int[][] matriz;
    private int heuristica;
    private int custo = 1;
    private int n;
    private Nodo proximo_lista;

    public Nodo(int[][] matriz) {
        this.matriz = matriz; 
        n = matriz[0].length;
    }

    public int[][] getMatriz() {
        return matriz;
    }

    public void setMatriz(int[][] matriz) {
        this.matriz = matriz;
    }

    public int getHeuristica() {
        return heuristica;
    }

    public void setHeuristica(int heuristica) {
        this.heuristica = heuristica;
    }

    public int getCusto() {
        return custo;
    }

    public void setCusto(int custo) {
        this.custo = custo;
    }

    public int getN() {
        return n;
    }
    
    @Override
    public String toString() {
        String m = "";
        
//        for(int i = 0; i < 3; i++){
//            for(int j = 0; j < 3; j++){
//                m += this.matriz[i][j] + " ";
//            }
//            m += '\n';
//        }

        for(int i = 0; i < (n*n); i++){
            int linha = i/n;
            int coluna = (int) i%n;
            
            if(coluna == 0){
                m += '\n';
            }
            
            m += this.matriz[linha][coluna] + " ";
        }
        
        m += '\n';
        m += "--------------";
        
        return m;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }
    
//    public int[] getMovimentos(){
//        
//    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Nodo other = (Nodo) obj;
        if (this.heuristica != other.heuristica) {
            return false;
        }
        if (!Arrays.deepEquals(this.matriz, other.matriz)) {
            return false;
        }
        return true;
    }

    @Override
    public Nodo clone() {
        int [][]m = new int[matriz.length][matriz.length];
        for (int i=0; i< matriz[0].length; i++){
            for (int j=0; j< matriz[i].length; j++){
                m[i][j] = matriz[i][j];
            }
        }
        return new Nodo(m);
    }
    
}
