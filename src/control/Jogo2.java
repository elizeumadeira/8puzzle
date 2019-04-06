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
import view.Tabuleiro;

/**
 *
 * @author Elizeu-pc
 */
public class Jogo2 {

    //private Nodo nodos_abertos;
    private List<Nodo> nodos_abertos;
    private List<Nodo> nodos_fechados;
    private Nodo estado;
    private Nodo objetivo;
    private Tabuleiro tabuleiro;

    public Jogo2(int[][] objetivo, int[][] estado, Tabuleiro tabuleiro) {
        this.estado = new Nodo(estado);
        this.objetivo = new Nodo(objetivo);
//        System.out.println(this.estado.equals(this.objetivo));
        nodos_abertos = new ArrayList<>();
        nodos_fechados = new ArrayList<>();
        nodos_abertos.add(this.estado);
        this.tabuleiro= tabuleiro;
    }

    public Jogo2(Nodo objetivo, Nodo estado) {
        this.estado = estado;
        this.objetivo = objetivo;
    }

    public Nodo getEstado() {
        return this.estado;
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
     * @param m matriz estado nesse estado.
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
            } else //esta ao lado ou na diagonal
                 { //soma quantas linhas + quantas colunas o numero atual esta fora excetuando o zero
                    // System.out.println("N:  " + n +  " he: " + (Math.abs(linha - linha_n) + Math.abs(coluna - coluna_n)) );
                    //  System.out.println("N: " + n + " linha: " + linha + " linha n " + linha_n + " coluna " + coluna + " coluna n " + coluna_n );
                    if(Math.abs(linha - linha_n) + Math.abs(coluna - coluna_n) == 1){
                        //procura qual numero esta na posição de N
                        int p = m[linha_n][coluna_n];
                        he += this.movNAteP(m, n, p);
                    }else{//move zero ao lado do numero depois soma com a quantidade 
                        // de movimentos necessários para move-lo para o lugar correto
                        he += this.movNAteP(m, 0, n) - 1; //só preciso mover até o lado, não trocar de movimento
                        
                        int p = m[linha_n][coluna_n];
                        he += this.movNAteP(m, n, p); //agora sim calcula a quantidade de movimentos para move-lo parao lugar
                    }
                 /*if (Math.abs(linha - linha_n) <= 1 && Math.abs(coluna - coluna_n) <= 1) {
                he += 1;
            } else//esta a duas casas de distancia
            {
                he += 2;
             */
            }

        }

        return he;
    }

    //calcula quantos movimentos necessarios para mover uma peça de uma posição até outra
    public int movNAteP(int[][] m, int n, int p) {
        int linhaN = 0;
        int colunaN = 0;
        int linhaP = 0;
        int colunaP = 0;

        //procura P
        for (int i = 0; i < 9; i++) {
            int linha = i / 3;
            int coluna = (int) i % 3;

            if (m[linha][coluna] == 0) {
                linhaP = linha;
                colunaP = coluna;
                break;
            }
        }

        //procura n
        for (int i = 0; i < 9; i++) {
            int linha = i / 3;
            int coluna = (int) i % 3;

            if (m[linha][coluna] == n) {
                linhaN = linha;
                colunaN = coluna;
                break;
            }
        }

        //diferença entre a linha de n e a linha de 0
        int linhaD = Math.abs(linhaN - linhaP);

        //diferença entre a coluna de n e a coluna de 0
        int colunaD = Math.abs(colunaN - colunaP);

        //n ao lado de 0
        if ((linhaD == 0 && colunaD == 1)
                || (linhaD == 1 && colunaD == 0)
                || (linhaD == 1 && colunaD == 1)) {
            return 4;
        } else if (linhaD == 2 && colunaD == 2) {//zero e N em extremos opostos
            return 10;
        } else if ((linhaD == 2 && colunaD == 1)//zero esta na linha ou coluna do lado + duas casas de distancia
                || (linhaD == 1 && colunaD == 2)) {
            return 9;
        }

        return 0;
    }

    public boolean isFinal() {
        return estado.equals(objetivo);
        //return nodos_abertos.equals(this.estado);
    }

    public void run() {
        while (!isFinal()) {
            if (!temNodoAberto()) {
                break;
            }
//        if (temNodoAberto()) {
            //pega o primeiro nodo da lista e expande e o remove após expandir
//                Nodo estado_temp = new Nodo(nodos_abertos.get(0).getMatriz());
            Nodo estado_temp = nodos_abertos.remove(0);

//            if (this.nodos_fechados.contains(estado_temp)) {
//                continue;
//            }
            this.estado = estado_temp;
            nodos_fechados.add(this.estado);
            tabuleiro.setEstado(this.estado.getMatriz());
            expandeEstado();
            System.out.println("Removeu estado: (nodos na fronteira: " + nodos_abertos.size() + ")");
            //String h ="";
            //for (Nodo nodos : nodos_abertos){
            //    h+= nodos.getHeuristica() + "; ";
            // }
            //System.out.println(h);
            System.out.println(this.estado);
            System.out.println("Passo: " + this.estado.getCusto());
//            }

        }
        System.out.println(this.estado + " Fim de jogo!");
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
        System.out.println("Expandir o estado:");
        System.out.println(estado);
        System.out.println("Heuristica " + this.estado.getHeuristica());
        //incrementa o custo
        this.estado.setCusto(estado.getCusto() + 1);
        int linha = 0;
        int coluna = 0;
        int j = 0;
        int movLinha = 0;
        int movColuna = 0;
        while (j < 3) {
            coluna = this.inArray(this.estado.getMatriz()[j], 0);
            if (coluna != -1) {
                linha = j;
                j = 2;
            }
            j++;
        }
        if (linha == 0) {
            movLinha = 1;
        } else if (linha > 0 && linha < estado.getN() - 1) {
            movLinha = 2;
        } else {
            movLinha = -1;
        }
        if (coluna == 0) {
            movColuna = 1;
        } else if (coluna > 0 && coluna < estado.getN() - 1) {
            movColuna = 2;
        } else {
            movColuna = -1;
        }
        int movY = (movLinha == 2 ? -1 : movLinha);
        int movX = (movColuna == 2 ? -1 : movColuna);
        ArrayList<Nodo> filhos = new ArrayList<>();
        for (int i = 1; i <= Math.abs(movLinha); i++) {
            int n = estado.getMatriz()[linha + movY][coluna];
            Nodo filho = estado.clone();
            filho.getMatriz()[linha + movY][coluna] = 0; //troca o numero por zero
            filho.getMatriz()[linha][coluna] = n;
            filho.setHeuristica(this.somaHeuristica(filho.getMatriz()) + filho.getCusto());
            filhos.add(filho);
            if (this.insereOrdenado(filho)) {
                System.out.println("Nodo " + (nodos_abertos.size()));
                System.out.println(filho);
                System.out.println("Heuristica " + filho.getHeuristica());
            }
            movY *= -1;
        }
        for (j = 1; j <= Math.abs(movColuna); j++) {

            int n = estado.getMatriz()[linha][coluna + movX];
            Nodo filho = estado.clone();
            filho.getMatriz()[linha][coluna + movX] = 0; //troca o numero por zero
            filho.getMatriz()[linha][coluna] = n;
            filho.setHeuristica(this.somaHeuristica(filho.getMatriz()) + filho.getCusto());
            filhos.add(filho);
            if (this.insereOrdenado(filho)) {
                System.out.println("Nodo " + (nodos_abertos.size()));
                System.out.println(filho);
                System.out.println("Heuristica " + filho.getHeuristica());
            }
            movX *= -1;
        }
        estado.setFilhos(filhos);

    }

    /**
     * Insere o novo nodo estado na lista ordenada pela sua heurística.
     *
     * @param estado
     */
    private boolean insereOrdenado(Nodo estado) {
        int i = 0;
        boolean estaFechado = false;
        boolean estaAberto = false;
//        for (Nodo fechado : nodos_fechados) {
//            if (fechado.equals(estado)) {
//                estaFechado = true;
//            }
//        }
        estaFechado = this.nodos_fechados.contains(estado);

        for (Nodo aberto : nodos_abertos) {
            if (aberto.equals(estado)) {
                estaAberto = true;
            }
        }
        if (estaFechado || estaAberto) {
            return false;
        }
        while (i < nodos_abertos.size()) {
            Nodo n = nodos_abertos.get(i);
            if (estado.getHeuristica() < n.getHeuristica()) {
                //insere na frente e retorna
                nodos_abertos.add(i, estado);
                return true;
            }
            i++;
        }
        //se a heurística for a maior dos nodos, insere no final da lista
        nodos_abertos.add(estado);
        return true;
    }

}
