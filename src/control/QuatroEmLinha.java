/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

/**
 *
 * @author Elizeu
 */
public class QuatroEmLinha {

    private int[][] tabuleiro;

    public QuatroEmLinha() {
        int inicial[][] = {
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0},
            {0, 0, 0, 1, 0, 0, 2},
            {0, 0, 1, 0, 0, 0, 2},
            {0, 1, 0, 0, 0, 0, 2},
            {0, 0, 1, 1, 1, 1, 2}
        };
        this.tabuleiro = inicial;
    }

    protected int[] verificaVencedor(int[][] m) {
        int vitJogadores[] = {0, 0};
        for (int i = 0; i < 42; i++) {
            int linha = i / 7;
            int coluna = (int) i % 7;
            int n = m[linha][coluna];

            //não verifica se for vazio
            if (n == 0) {
                continue;
            }

            if ((linha + 4) <= 6 && (coluna + 4) <= 7) {//evitar ArrayOutOfBound
                //diagonal principal
                if (m[linha + 1][coluna + 1] == n
                        && m[linha + 2][coluna + 2] == n
                        && m[linha + 3][coluna + 3] == n) {
                    vitJogadores[n - 1]++;
                }
            }

            if ((linha + 4) <= 6) {
                //vertical
                if (m[linha + 1][coluna] == n
                        && m[linha + 2][coluna] == n
                        && m[linha + 3][coluna] == n) {
                    vitJogadores[n - 1]++;
                }
            }

            if ((coluna + 4) <= 7) {
                //horizontal
                if (m[linha][coluna + 1] == n
                        && m[linha][coluna + 2] == n
                        && m[linha][coluna + 3] == n) {
                    vitJogadores[n - 1]++;
                }
            }

            if ((linha - 4) >= 0 && (coluna + 4) <= 7) {
                //diagonal secundaria
                if (m[linha - 1][coluna + 1] == n
                        && m[linha - 2][coluna + 2] == n
                        && m[linha - 3][coluna + 3] == n) {
                    vitJogadores[n - 1]++;
                }
            }
        }

        return vitJogadores;
    }

    public int verificaVencedor() {
        int[] vencedor = this.verificaVencedor(this.tabuleiro);
        System.out.println(vencedor[0] + " == " + vencedor[1]);
        return 0;
    }

    public int escreveTabuleiro() {
        String m = "";

        for (int i = 0; i < 42; i++) {
            int linha = i / 7;
            int coluna = (int) i % 7;

            if (coluna == 0) {
                m += '\n';
            }
            m += this.tabuleiro[linha][coluna];
        }

        m += '\n';
        m += "--------------";

        System.out.println(m);

        return 0;
    }

}
