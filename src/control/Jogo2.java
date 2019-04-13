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
public class Jogo2 extends Thread {

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
        this.tabuleiro = tabuleiro;
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
            //soma quantas linhas + quantas colunas o numero atual esta fora excetuando o zero
            // System.out.println("N:  " + n +  " he: " + (Math.abs(linha - linha_n) + Math.abs(coluna - coluna_n)) );
            //  System.out.println("N: " + n + " linha: " + linha + " linha n " + linha_n + " coluna " + coluna + " coluna n " + coluna_n );
            {
                if (Math.abs(linha - linha_n) + Math.abs(coluna - coluna_n) == 1) {
                    //procura qual numero esta na posição de N
                    int p = m[linha_n][coluna_n];
                    he += this.movNAteP(m, n, p);
                } else {//move zero ao lado do numero depois soma com a quantidade 
                    // de movimentos necessários para move-lo para o lugar correto
                    he += this.movNAteP(m, 0, n) - 1; //só preciso mover até o lado, não trocar de movimento

                    int p = m[linha_n][coluna_n];
                    he += this.movNAteP(m, n, p); //agora sim calcula a quantidade de movimentos para move-lo parao lugar
                }
            }

        }

        return he;
    }

    public int[] procuraPosicao(int[][] m, int n) {
        int res[] = {0, 0};
        for (int i = 0; i < 9; i++) {
            int linha = i / 3;
            int coluna = (int) i % 3;

            if (m[linha][coluna] == n) {
                res[0] = linha;
                res[1] = coluna;
                break;
            }
        }

        return res;
    }

    //calcula quantos movimentos necessarios para mover uma peça de uma posição até outra
    public int movNAteP(int[][] m, int n, int p) {
        int linhaN = 0;
        int colunaN = 0;
        int linhaP = 0;
        int colunaP = 0;

        //procura P
        int[] posicaoP = this.procuraPosicao(m, 0);
        linhaP = posicaoP[0];
        colunaP = posicaoP[1];
//        for (int i = 0; i < 9; i++) {
//            int linha = i / 3;
//            int coluna = (int) i % 3;
//
//            if (m[linha][coluna] == 0) {
//                linhaP = linha;
//                colunaP = coluna;
//                break;
//            }
//        }

        //procura n
        int[] posicaoN = this.procuraPosicao(m, n);
        linhaN = posicaoN[0];
        colunaN = posicaoN[1];
//        for (int i = 0; i < 9; i++) {
//            int linha = i / 3;
//            int coluna = (int) i % 3;
//
//            if (m[linha][coluna] == n) {
//                linhaN = linha;
//                colunaN = coluna;
//                break;
//            }
//        }

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

    @Override
    public void run() {
        try {
            int ultCusto = 1;
            while (!isFinal()) {
                if (!temNodoAberto()) {
                    break;
                }
//        if (temNodoAberto()) {
                //pega o primeiro nodo da lista e expande e o remove após expandir
//                Nodo estado_temp = new Nodo(nodos_abertos.get(0).getMatriz());

                Nodo estado_temp = nodos_abertos.remove(0);

//                if((ultCusto+1) == estado_temp.getCusto()){
//                    tabuleiro.setEstado(estado_temp.getMatriz(),"Nivel "+ (ultCusto+1));
//                    ultCusto = estado_temp.getCusto();
//                    Thread.sleep(500);
//                }
//            if (this.nodos_fechados.contains(estado_temp)) {
//                continue;
//            }
                this.estado = estado_temp;
                nodos_fechados.add(this.estado);
                //  tabuleiro.setEstado(this.estado.getMatriz(),"");
                expandeEstado();
                System.out.println("Removeu estado: (nodos na fronteira: " + nodos_abertos.size() + ")");
                System.out.println(this.estado);
                System.out.println("Passo: " + this.estado.getCusto());
//            }

            }
            System.out.println(this.estado + " Fim de jogo! Resolvido em " + this.estado.getCusto() + " passos(s)");
            //estado.setHeuristica(this.somaHeuristica(this.estado.getMatriz()));
            //System.out.println(this.estado);
            //System.out.println("Heuristica " + estado.getHeuristica());

            Nodo solucao = this.estado;
            ArrayList<Nodo> arvoreSolucao = new ArrayList<>();
            arvoreSolucao.add(solucao);

            while (solucao != null) {
                solucao = solucao.getPai();
                if (solucao != null) {
                    arvoreSolucao.add(solucao);
                }
            }

            for (int i = arvoreSolucao.size() - 1; i >= 0; i--) {
                solucao = arvoreSolucao.get(i);
                tabuleiro.setEstado(solucao.getMatriz(), "Movimento " + solucao.getMovimento().toString());
                this.sleep(500);

            }
            tabuleiro.setFinal(this.estado.getCusto());
            tabuleiro.setEstado(solucao.getMatriz(), "");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void escreveNodosFechados() {
        int i = 0;
        for (Nodo caminho : nodos_fechados) {
            System.out.println("Nodo nº" + (i + 1));
            System.out.println(caminho);
            System.out.println("============");
            i++;
        }
    }

    public boolean temNodoAberto() {
        return !nodos_abertos.isEmpty();
    }

    /**
     * Método responsável por verificar os movimentos possíveis para o estado
     * atual e calcular a heurística de cada um.
     */
    private void expandeEstado() {

        System.out.println("Expandir o estado:");
        System.out.println(this.estado);
        System.out.println("Heuristica " + this.estado.getHeuristica());
        //incrementa o custo
        this.estado.setCusto(estado.getCusto() + 1);
        int coluna = 0;
        int linha = 0;
        Acao[] acao = {Acao.A_ESQUERDA, Acao.A_DIREITA, Acao.A_BAIXO, Acao.A_CIMA};
        ArrayList<Nodo> filhos = new ArrayList<>();
        while (linha < estado.getN()) {
            coluna = this.inArray(this.estado.getMatriz()[linha], 0);
            if (coluna != -1) { //quando encontra o zero
                int[][] direcoes = {{linha, coluna - 1}, {linha, coluna + 1}, {linha + 1, coluna}, {linha - 1, coluna}}; //Esquerda, Direita, Abaixo, Acima
                //percorre cada um dos movimentos possíveis no array direcoes
                for (int k = 0; k < direcoes.length; k++) {
                    int l = direcoes[k][0];
                    int c = direcoes[k][1];
                    //se na posição do zero for possível executar o movimento, ou seja, o cálculo de l e c estiverem dentro dos limites da matriz
                    if (l >= 0 && c >= 0 && l < estado.getN() && c < estado.getN()) {
                        int n = estado.getMatriz()[l][c];
                        Nodo filho = estado.clone();
                        filho.getMatriz()[l][c] = 0; //troca o numero por zero
                        filho.getMatriz()[linha][coluna] = n;
                        filho.setHeuristica(this.somaHeuristica(filho.getMatriz()) + filho.getCusto());
                        filho.setMovimento(acao[k]);
                        filhos.add(filho);
                        if (this.insereOrdenado(filho)) {
                            System.out.println("+ Nodo " + nodos_abertos.size() + " (movimento a " + filho.getMovimento() + " )");
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
