/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analisadorlexico;

/**
 *
 * @author Carolini
 */
public class Erros {
    private String description; //descriçao do erro
    private String id;  //item que originou o erro
    private int line; // linha que ocorreu o erro
    private String type; //tipo do erro (sintatico,lexico,...)

    public String getType() {
        return type;
    }

    public void trocaTipo()
    {
        this.type = "Lexico";
    }

    /*
     *
     */
    public Erros(String description, String id, int line,String type)
    {
        this.description = description;
        this.id = id;
        this.line = line;
        this.type = type;
    }

    /**
     * Método que retorna a descriçao do erro
     * @return String string contendo a descriçao do erro
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Método que retorna a id do erro
     * @return String string contendo a id causadora do erro
     */
    public String getId()
    {
        return id;
    }

    /**
     * Método que retorna a linha do erro
     * @return int inteiro contendo o número da linha em que ocorreu o erro
     */
    public int getLine()
    {
        return line;
    }

    @Override
    /**
     * Método que retorna as informações sobre o erro
     * @return String string contendo informações gerais sobre o erro
     */
    public String toString()
    {
      return (description+" na linha "+line+":  "+id+'\n');
    }
    public String toString2()
    {
        return ("Erro "+type+": "+description+" na linha "+line+".\n");
    }
    public String toString3()
    {
        return ("Erro "+type+": "+description+" \""+ id+"\" na linha "+line+".\n");
    }
}
