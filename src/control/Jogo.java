/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import helpers.Helper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.Nodo;

/**
 *
 * @author Elizeu-pc
 */
public class Jogo {

    //private Nodo nodos_abertos;
    private List<Nodo> nodos_abertos;
    private List<Nodo> nodos_fechados;
    private Nodo estado;
    private Nodo objetivo;
        
    public Jogo(int[][] objetivo, int[][] estado) {
        this.estado = new Nodo(estado);
        this.objetivo = new Nodo(objetivo);
        nodos_abertos = new ArrayList<>();
        nodos_fechados = new ArrayList<>();
        this.estado.setHeuristica(this.somaHeuristica(this.estado.getMatriz()));
        nodos_abertos.add(this.estado);
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

    /**
     * Consulta um inteiro em um array de inteiros. Retorna o indice do numero
     * no array ou retorna -1 se não encontrar.
     *
     * @param a
     * @param n
     * @return o indice do numero "n" no array "a" ou retorna -1 se não
     * encontrar
     */
    public int inArray(int[] a, int n) {
        for (int i = 0; i < a.length; i++) {
            if (n == a[i]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Calcula a heuristica para o array estado do nodo atual. Percorre a matriz
     * "m" e para cada posicao do numero, verifica em qual posicao o mesmo
     * numero eh encontrado na matriz do nodo objetivo. Se for na mesma posicao
     * (linha x coluna) heuristica= 0, se nao, calcula a heuristica para o
     * numero, somando o numero de casas de distancia do objetivo o numero está.
     *
     * @param m
     * @return
     */
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
            } else { //esta ao lado ou na diagonal
                //int distancia = Math.abs(linha - linha_n) + Math.abs(coluna - coluna_n);
                //System.out.println("numero: " + n + " linha: " + linha + " linha obj: "+ linha_n + " coluna: " + coluna + " coluna obj: " + coluna_n + " he: " +  distancia );
                he += Math.abs(linha - linha_n) + Math.abs(coluna - coluna_n);

                /*if (Math.abs(linha - linha_n) <= 1 && Math.abs(coluna - coluna_n) <= 1) {
                he += 1;
            } else//esta a duas casas de distancia
            {
                he += 2;*/
            }

        }

        return he;
    }

    public boolean isFinal() {
        return estado.equals(objetivo);
        //return nodos_abertos.equals(this.estado);
    }

    public void jogar() {
        if (temNodoAberto()) {
            System.out.println("### Nodos abertos ###");
            for (Nodo nodo : nodos_abertos){
                System.out.println(nodo);
                 System.out.println("Heuristica " + nodo.getHeuristica());
            }   
            System.out.println("#####################");
            //pega o primeiro nodo da lista e expande e o remove após expandir
            this.estado = nodos_abertos.get(0);
            expandeEstado();
            nodos_fechados.add(this.estado);
            nodos_abertos.remove(this.estado);
        } else {
              
        }
        //estado.setHeuristica(this.somaHeuristica(this.estado.getMatriz()));
        //System.out.println(this.estado);
        //System.out.println("Heuristica " + estado.getHeuristica());

    }

    public boolean temNodoAberto() {
        return !nodos_abertos.isEmpty();
    }
    /**
     * 
     */
    private void expandeEstado() {
        //calcular possibilidades que são os indices alcançaveis a partir da posição 0
        int movimentos = 0;
        int linha = 0;
        int coluna = 0;
        int j = 0;
        while (j < 3) {
            coluna = this.inArray(this.estado.getMatriz()[j], 0);
            if (coluna != -1) {
                linha = j;
                j = 2;
            }
            j++;
        }
       
        if ((linha == coluna && linha == 0) || (linha == 0 && coluna == estado.getN() - 1)
                || (coluna == 0 && linha == estado.getN() - 1) || (linha == coluna && coluna == estado.getN() - 1)) { // o zero está situado em um dos 4 cantos
            movimentos = 2;
        } else if (linha > 0 && coluna > 0 && linha < estado.getN() - 1 && coluna < estado.getN() - 1) { // o zero está no meio
            movimentos = 4;
        } else {
            movimentos = 3;
        }
        switch (movimentos) {
            case 2: //cantos 
                //[0] [0] => [0] [1] ou [1] [0]
                //[0] [2] => [0] [1] ou [1] [2]
                //[2] [0] => [1] [0] ou [2] [1]
                //[2] [2] => [2] [1] ou [1] [2]
                int movY = 0;
                int movX = 0;
                if (linha- (estado.getN()-1) <0){ //testa se está na primeira linha ou na última
                    movY = 1; 
                }else{
                    movY = -1;
                }
                if (coluna-(estado.getN()-1) <0){ //testa se está na primeira coluna ou na última
                    movX = 1; 
                }else{
                    movX = -1;
                }
                int col = 0; //mantêm a mesma coluna e incrementa a linha
                for (int i=1; i<=movimentos ; i++){
                    int n = estado.getMatriz()[linha+movY][coluna+col]; 
                    Nodo novoEstado = estado.clone();
                    novoEstado.getMatriz()[linha+movY][coluna+col] = 0; //troca o numero por zero
                    novoEstado.getMatriz()[linha][coluna] = n;
                    novoEstado.setHeuristica(this.somaHeuristica(novoEstado.getMatriz()) + novoEstado.getCusto());
                    this.insereOrdenado(novoEstado);
                    System.out.println("Nodo " + i);
                    System.out.println(novoEstado);
                    System.out.println("Heuristica " + novoEstado.getHeuristica());
                    movY = 0; //mantêm a mesma linha e incrementa a coluna
                    col = movX;
                }
               //remove da listaordenada o estado pai 
            case 3: // pode subtrair a linha ou a coluna e somar a linha ou coluna coluna +=1; coluna -=1;
                    //[0] [1] => [0] [0] ou [1] [1] ou [0] [2]
                    //[1] [0] => [0] [0] ou [2] [0] ou [1] [1]
                    //[1] [1] => [1] [0] ou [1] [2] ou [0] [1] ou [2] [1]
                    //[1] [2] => [0] [0] ou [2] [0] ou [1] [1]            

            case 4: // meio pode somar e subtrair a linha e a coluna
        }
     

    }
    /** Insere o novo nodo estado na lista ordenada pela sua heurística.
     * 
     * @param estado 
     */
    private void insereOrdenado(Nodo estado) {
        int i = 0;
        while (i<nodos_abertos.size()){
            Nodo n = nodos_abertos.get(i);
            if (estado.getHeuristica()<n.getHeuristica() || i==nodos_abertos.size()-1){
                nodos_abertos.add(i, estado);
                i= nodos_abertos.size();
            }
            i++;
        }
    }

}
