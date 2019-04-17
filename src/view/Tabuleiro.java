/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 *
 * @author daniel
 */
public class Tabuleiro extends JPanel {

    // Cor das peças 
    private static final Color FOREGROUND_COLOR = new Color(60, 194, 187); // verde água
    // Matriz estado das peças em um array de duas dimensões
    private int[][] matriz;
    // Tamanho de cada peça
    private int tamPeca;
    // Margem reservada para o grid no JFrame
    private int margem;
    
    public Tabuleiro() {
        this(3, 1800, 5);
    }

    public Tabuleiro(int tamanho, int dimensao, int margem) {
        this.margem = margem;

        matriz = new int[tamanho][tamanho];

        // calcula o tamanho do grid para calcular o tamanho das peças
        int tamGrade = (dimensao - 2 * margem);
        tamPeca = tamGrade / (tamanho*10);

        setPreferredSize(new Dimension(dimensao, dimensao + margem));
        setBackground(Color.WHITE);
        setForeground(FOREGROUND_COLOR);
        setFont(new Font("SansSerif", Font.BOLD, 60));

    }

    /**
     * Desenha a representação do tabuleiro (matriz) nesse JPanel
     *
     * @param g objeto para redenrizar formas bidimensionais, texto e imagens.
     */
    private void desenhaGrade(Graphics2D g) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz.length; j++) {
                // transformando em coordenadas para o JPane
                int x = margem + i * tamPeca;
                int y = margem + j * tamPeca;

                // desenhando a peça em branco
                if (matriz[j][i] == 0) {
                    g.setColor(FOREGROUND_COLOR);
                    desenhaNumero(g, "", x, y);
                    continue;
                }
                //outras peças
                g.setColor(getForeground());
                g.fillRoundRect(x, y, tamPeca, tamPeca, 25, 25);
                g.setColor(Color.BLACK);
                g.drawRoundRect(x, y, tamPeca, tamPeca, 25, 25);
                g.setColor(Color.WHITE);

                desenhaNumero(g, String.valueOf(matriz[j][i]), x, y);
            }

        }
    }

    /**
     * Se o atributo isFinal for verdadeiro, desenha a mensagem de sucesso e o
     * número de passos que levou para resolver. Do contrario desenha o string
     * contido no atributo mensagem.
     *
     * @param g objeto para redenrizar formas bidimensionais, texto e imagens.
    
    private void desenhaMensagem(Graphics2D g) {
        if (isFinal) {
            mensagem = "Sucesso! Resolvido em " + passos + " passos.";
        }
        g.setFont(getFont().deriveFont(Font.BOLD, 18));
        g.setColor(FOREGROUND_COLOR);
        g.drawString(mensagem, (getWidth() - g.getFontMetrics().stringWidth(mensagem)) / 2,
                getHeight() - margem);
    } 
    */
    /**
     * Desenha no JPanel o texto que representa um número da matriz .
     *
     * @param g objeto para redenrizar formas bidimensionais, texto e imagens.
     * @param numero texto representando o número na posição x , y
     * @param x posição horizontal x da peça
     * @param y posição vertical y da peça
     */
    private void desenhaNumero(Graphics2D g, String numero, int x, int y) {
        // centraliza a string numero na posição da peça x, y
        FontMetrics fm = g.getFontMetrics();
        int asc = fm.getAscent();
        int desc = fm.getDescent();
        g.drawString(numero, x + (tamPeca - fm.stringWidth(numero)) / 2,
                y + (asc + (tamPeca - (asc + desc)) / 2));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        desenhaGrade(g2D);
    }

    public void setEstado(int[][] matriz) throws InterruptedException {
        this.matriz = matriz;
        repaint();  
    }

    

}
