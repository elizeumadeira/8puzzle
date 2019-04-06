/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.util.ArrayList;
import java.util.List;
import model.Acao;
import model.Nodo;
import view.Tabuleiro;

/**
 *
 * @author Elizeu-pc
 */
public class Jogo extends Thread {

    private List<Nodo> nodos_abertos;
    private List<Nodo> nodos_fechados;
    private Nodo estado;
    private Nodo objetivo;
    private Tabuleiro tabuleiro;

    public Jogo(int[][] objetivo, int[][] estado, Tabuleiro tabuleiro) {
        this.estado = new Nodo(estado);
        this.objetivo = new Nodo(objetivo);
        nodos_abertos = new ArrayList<>();
        nodos_fechados = new ArrayList<>();
        nodos_abertos.add(this.estado);
        this.tabuleiro= tabuleiro;
    }

    public Jogo(Nodo objetivo, Nodo estado) {
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
            if (n > 0) { //soma quantas linhas e colunas o numero atual esta fora do lugar (excetuando o zero)
                //mesma linha e coluna, nada a fazer
            if (linha == linha_n && coluna == coluna_n) {
                he += 0;
            } else //esta ao lado ou na diagonal
                  //   System.out.println("N:  " + n +  " he: " + (Math.abs(linha - linha_n) + Math.abs(coluna - coluna_n)) );
                  //    System.out.println("N: " + n + " linha: " + linha + " linha n " + linha_n + " coluna " + coluna + " coluna n " + coluna_n );
                    he += Math.abs(linha - linha_n) + Math.abs(coluna - coluna_n);
                    //calcula quantos movimentos são necessários para colocar o número no lugar dele

            }
            
        }

        return he;
    }

    public boolean isFinal() {
        return estado.equals(objetivo);
        //return nodos_abertos.equals(this.estado);
    }

    public void jogar() {
       this.start();
    }
    
    @Override
    public void run() {
        try {
            int passo = 0;
            while (!isFinal()) {
                if (!temNodoAberto()) {
                    break;
                }
                passo++;
                //pega o primeiro nodo da lista de filhos, se for estado S pega o primeiro de nodos abertos
                if (this.estado.getPai()==null) // nodo S (não tem um pai)
                    this.estado = nodos_abertos.remove(0);
                else{
                    System.out.println("Filhos: " + estado.getFilhos().size());
                    this.estado = this.estado.getFilhos().get(0); //não precisa remover aqui
                    
                }
                
                
                
//            if (this.nodos_fechados.contains(estado_temp)) {
//                continue;
//            }           
                expandeEstado();
                nodos_fechados.add(this.estado);
                tabuleiro.setEstado(this.estado.getMatriz());
                System.out.println("removeu: " + nodos_abertos.remove(this.estado));  //remove o estado filho com melhor heuristica da fronteira
                //for (Nodo aberto: nodos_abertos){
                    
                //}
                //String h ="";
                //for (Nodo nodos : nodos_abertos){
                //    h+= nodos.getHeuristica() + "; ";
                // }
                //System.out.println(h);

//            }
                   this.sleep(500); 
            }
            System.out.println(this.estado + " Fim de jogo! Resolvido em " + passo + " passos(s)");
            tabuleiro.setFinal(passo);
            //implentar: O total de nodos visitados; O maior tamanho da fronteira durante a busca
            //O tamanho do caminho
            System.out.println("Nodos visitados: " + (nodos_abertos.size() + nodos_fechados.size()));
            ///String caminho = "";
            /*for (Nodo fechado : nodos_fechados){
            //caminho += fechado.getMovimento() + " -> ";
            System.out.println(fechado.getMovimento());
            System.out.println(fechado);
            
        }*/
            // System.out.println(caminho);
            System.out.println("Tamanho do caminho: " + nodos_fechados.size());
            //j.imprimeMaiorTamFronteira();
            //j.imprimeNodosFechados();

            /*Nodo solucao = this.estado;
        while (solucao!=null) {
            System.out.println(solucao);
            solucao = solucao.getPai();
        }*/
        } catch (Exception ex) {
        }
    }

    public boolean temNodoAberto() {
        return !nodos_abertos.isEmpty();
    }

    /** Método responsável por verificar os movimentos possíveis 
     *  para o estado atual e calcular a heurística de cada um.
     */
    private void expandeEstado() {
        
        System.out.println("Expandir o estado:");
        System.out.println(estado);
        System.out.println("Heuristica " + this.estado.getHeuristica());
        //incrementa o custo
        this.estado.setCusto(estado.getCusto() + 1);
        int coluna = 0;
        int linha = 0;
        Acao [] acao = {Acao.A_ESQUERDA, Acao.A_DIREITA, Acao.A_BAIXO, Acao.A_CIMA}; 
        ArrayList<Nodo> filhos = new ArrayList<>();
        while (linha < estado.getN()) {
            coluna = this.inArray(this.estado.getMatriz()[linha], 0);
            if (coluna != -1) { //quando encontra o zero
                int [][] direcoes = {{linha, coluna-1}, {linha, coluna+1}, {linha+1, coluna},  {linha-1, coluna}}; //Esquerda, Direita, Abaixo, Acima
                //percorre cada um dos movimentos possíveis no array direcoes
                for (int k = 0 ; k< direcoes.length ; k++){
                    int l = direcoes [k][0];
                    int c = direcoes [k][1];
                    //se na posição do zero for possível executar o movimento, ou seja, o cálculo de l e c estiverem dentro dos limites da matriz
                    if (l >= 0 && c >= 0 && l < estado.getN() && c < estado.getN()) {
                        int n = estado.getMatriz()[l][c];
                        Nodo filho = estado.clone();
                        filho.getMatriz()[l][c] = 0; //troca o numero por zero
                        filho.getMatriz()[linha][coluna] = n;
                        filho.setHeuristica(this.somaHeuristica(filho.getMatriz()) + filho.getCusto());
                        filho.setMovimento(acao[k]);                      
                        if (this.insereOrdenado(filhos, filho)) {
                            System.out.println("+ Nodo " + nodos_abertos.size()  + " (movimento a " + filho.getMovimento() + " )");
                            System.out.println(filho);
                            System.out.println("Heuristica " + filho.getHeuristica());
                        }
                    }
                }
                break;
            }
            linha++;
        }
        this.estado.setFilhos(filhos);
    }

    /**
     * Insere o novo nodo estado filho na lista ordenada pela sua heurística.
     *
     * @param filho
     */
    private boolean insereOrdenado(ArrayList<Nodo> filhos, Nodo filho) {
        int i = 0;
        boolean estaFechado = false;
        boolean estaAberto = false;
        for (Nodo fechado : nodos_fechados) {
            if (fechado.equals(filho)) {
                estaFechado = true;
            }
        }
         for (Nodo aberto : nodos_abertos) {
            if (aberto.equals(filho)) {
                estaAberto = true;
            }
        }
       //boolean estaFechado = this.nodos_fechados.contains(filho);
//       boolean estaAberto = this.nodos_abertos.contains(filho);
        
        if (estaFechado || estaAberto) {
            return false;
        }
        while (i < filhos.size()) {
            Nodo n = filhos.get(i);
            if (filho.getHeuristica() < n.getHeuristica()) {
                //insere na frente e retorna
                filhos.add(i, filho);
                return true;
            }
            i++;
        }
        //se a heurística for a maior dos nodos, insere no final da lista
        filhos.add(filho);
        nodos_abertos.add(filho);
        return true;
    }
    
    

}