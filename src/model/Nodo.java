/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Elizeu-pc
 */
public class Nodo {

    private int[][] matriz;
    private int heuristica;
    private int custo = 1;
    private Nodo pai;
    private ArrayList<Nodo> filhos;
    //private Nodo proximo_lista;

    public Nodo(int[][] matriz) {
        this.matriz = matriz; 
        pai = null;
        filhos = null;
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
        return matriz[0].length;
    }

    public Nodo getPai() {
        return pai;
    }

    public void setPai(Nodo pai) {
        this.pai = pai;
    }

    public ArrayList<Nodo> getFilhos() {
        return filhos;
    }

    public void setFilhos(ArrayList<Nodo> filhos) {
        this.filhos = filhos;
        if (filhos!= null){
            for (Nodo f: filhos){
                f.setPai(this);
            }
        }
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

        for(int i = 0; i < (getN()*getN()); i++){
            int linha = i/getN();
            int coluna = (int) i%getN();
            
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
      //  if (this.heuristica != other.heuristica) {
       //     return false;
        //}
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
        Nodo novo = new Nodo(m);
        novo.setCusto(custo);
        novo.setHeuristica(heuristica);
        return novo;
    }
    
}
