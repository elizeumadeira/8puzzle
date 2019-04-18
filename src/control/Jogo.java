/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.util.ArrayList;
import java.util.List;
import model.Acao;
import model.Algoritmo;
import model.Nodo;
import view.Tabuleiro;
import view.TelaInicial;

/**
 * Universidade Federal de Santa Catarina
 * Curso de Bacharelado em Sistemas de Informacao
 * INE5633-07238-Sistemas Inteligentes
 * Daniel Antonio Tell - 18200625
 * Elizeu Santos Madeira - 10101181
 * Florianopolis, abril de 2019
 */
public class Jogo extends Thread {

    //private Nodo nodos_abertos;
    private List<Nodo> nodos_abertos;
    private List<Nodo> nodos_fechados;
    private Nodo estado;
    private Nodo objetivo;  
    private Algoritmo algoritmo;
    private TelaInicial tela;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public boolean imprimeSaida = true;

    public Jogo(int[][] objetivo, int[][] estado, TelaInicial tela, boolean imprimeSaida) {
        this.estado = new Nodo(estado);
        this.objetivo = new Nodo(objetivo);

        nodos_abertos = new ArrayList<>();
        nodos_fechados = new ArrayList<>();
        nodos_abertos.add(this.estado);      
        this.tela = tela;
        this.algoritmo = Algoritmo.CUSTO_UNIFORME;
        this.imprimeSaida = imprimeSaida;
    }

    public void setAlgoritmo(Algoritmo algoritmo) {
        this.algoritmo = algoritmo;
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
        escreve(r);
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
     * Calcula a heuristica simples para o array estado do nodo atual. Percorre a matriz
     * "m" e para cada posicao da matriz, verifica se o mesmo número eh encontrado na 
     * mesma posição da matriz do nodo objetivo. .
     *
     * @param filho eh um objeto da classe Nodo que representa o estado a verificar.
     * @return valor da heurística calculada + valor do custo
     */
    public int somaHeuristicaSimples(Nodo filho) {
        int he = 0;
        int [][]m = filho.getMatriz();
        int custo = filho.getCusto();
        //começa a percorrer a matriz inserida
        for (int i = 0; i < 9; i++) {
            int linha = i / 3;
            int coluna = (int) i % 3;
            //para cada n fora do lugar somar 1 a heuristica (excetuando o zero)
            if (m[linha][coluna] != 0){
                if (m[linha][coluna] != this.objetivo.getMatriz()[linha][coluna]){     
                    he++;
                }
            }
            
        }
        //retorna a soma da heurística com o custo
        return he + custo;
    }
    /**
     * Calcula a heuristica mais precisa para o array estado do nodo atual. Percorre a matriz
     * "m" e para cada posicao do numero, verifica em qual posicao o mesmo
     * numero eh encontrado na matriz do nodo objetivo. Se for na mesma posicao
     * (linha x coluna) heuristica= 0, se nao, calcula a heuristica para o
     * numero, somando o numero de casas de distancia do objetivo o numero está.
     *
     * @param filho eh um objeto da classe Nodo que representa o estado a verificar.
     * @return valor da heurística calculada + valor do custo
     */
    public int somaHeuristicaPrecisa(Nodo filho) {
        int he = 0; // valor de heurística
        int [][]m = filho.getMatriz();
        int custo = filho.getCusto();
        //começa a percorrer a matriz inserida
        for (int i = 0; i < 9; i++) {
            int linha = i / 3;
            int coluna = (int) i % 3;

            int linha_n = 0;
            int coluna_n = 0;
            int n = m[linha][coluna];
            //n devia estar na linha...
            for (int j = 0; j < 3; j++) {
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
                if (this.inArray(col, n) == -1) {
                    continue;
                }
               coluna_n = j;
            
            }//soma quantas linhas e colunas o número atual esta fora do lugar (excetuando o zero)
            if (n > 0) { 
            //mesma linha e coluna, nada a fazer
            if (linha == linha_n && coluna == coluna_n) {
                he += 0;
            } else 
                he += Math.abs(linha - linha_n) + Math.abs(coluna - coluna_n);
            }
            
        }

        return he + custo;

    }
    

    public boolean isFinal() {
        return estado.equals(objetivo);
        //return nodos_abertos.equals(this.estado);
    }

    @Override
    public void run() {
        try {
            int maiorFront = 0;
            Tabuleiro tabuleiro = tela.getTabuleiro();
            tabuleiro.setVisible(true);
            tela.setMensagem("");          
            while (!isFinal()) {
                if (!temNodoAberto()) {
                    break;
                }
                if (nodos_abertos.size()> maiorFront){
                    maiorFront = nodos_abertos.size();
                }
                this.estado = nodos_abertos.remove(0);              
                nodos_fechados.add(this.estado); 
                expandeEstado();
                escreve(ANSI_RED + "- Estado removido: (nodos na fronteira: " + nodos_abertos.size() + ")" + ANSI_RESET);
                escreve(this.estado);
                escreve("Heuristica " + this.estado.getHeuristica());

            }
            escreve(this.estado + " Fim de jogo! Resolvido em " + this.estado.getCusto() + " passos(s)");
            
            Nodo solucao = this.estado;
            ArrayList<Nodo> arvoreSolucao = new ArrayList<>();
            arvoreSolucao.add(solucao
            );          
            while (solucao != null) {
                solucao = solucao.getPai();
                if (solucao != null) {
                    arvoreSolucao.add(solucao);
                }
            }
            int passo = 0;
            for (int i = arvoreSolucao.size() - 1; i >= 0; i--) {
                passo++;
                solucao = arvoreSolucao.get(i);
                tabuleiro.setEstado(solucao.getMatriz());
                tela.setMensagem("Passo " + passo + " --> movimento " + solucao.getMovimento().toString() );
                tela.repaint();
                this.sleep(500);
            }
            tela.setMensagem(" Fim de jogo! Resolvido em " + this.estado.getCusto() + " passos(s)" + ". Nodos visitados: " + nodos_fechados.size() + ". Maior fronteira: " + maiorFront);
            
            tabuleiro.setEstado(solucao.getMatriz());
            tela.fimDeJogo8Puzzle();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void escreveNodosFechados() {
        int i = 0;
        for (Nodo caminho : nodos_fechados) {
            escreve("Nodo nº" + (i + 1));
            escreve(caminho);
            escreve("============");
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
        escreve("Estado expandido:");
        escreve(this.estado);
        escreve("Heuristica " + this.estado.getHeuristica());
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
                        switch (this.algoritmo){
                            case CUSTO_UNIFORME:
                                filho.setHeuristica(this.estado.getCusto());
                                break;
                            case A_ESTRELA_SIMPLES:
                                filho.setHeuristica(this.somaHeuristicaSimples(filho)); 
                                break;
                            case A_ESTRELA_PRECISA:
                                filho.setHeuristica(this.somaHeuristicaPrecisa(filho));
                        }
                        filho.setMovimento(acao[k]);
                        filhos.add(filho);
                        if (this.insereOrdenado(filho)) {
                            escreve(ANSI_GREEN + "+ Nodo " + nodos_abertos.size() + " (movimento a " + filho.getMovimento() + " )" + ANSI_RESET);
                            escreve(filho);
                            escreve("Heuristica " + filho.getHeuristica());
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

    private void escreve(Object saida) {
        
        if (imprimeSaida)
            System.out.println(saida);
    }

}
