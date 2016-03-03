/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analisadorlexico;

import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Carolini
 */

/**
 *Classe que serve para guardar e manipular as palavras reservadas da linguagem
 */
class Tabela {
    private final HashSet<String> hashset;
    
    /**
     * Método construtor, apenas recebe uma lista e guarda em uma HashSet, para uma manipulaçao
     * mais eficiente.
     * A lista é um conjunto, elementos não se repetem.
     * A busca na hashset terá tempo constante: O(1).
     * @param list uma lista com as palavras reservadas da linguagem
     */
    Tabela(List<String> list) {
        this.hashset = new HashSet<String> (list);
    }
    /**
     * Método que verifica se a palavra reservada word esta na lista de palavras reservadas
     * @param word palavra que será procurada
     * @return verdadeiro caso a word exista na lista, falso caso contrario
     */
    public boolean  hasWord(String word)
    {
        //esta comparação tem complexidade O(1).
        return( this.hashset.contains(word) );
    }
}
