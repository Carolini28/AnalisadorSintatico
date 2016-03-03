/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analisadorlexico;

/**
 *
 * @author Carolini
 */

/**
 * Classe que serve apenas para auxiliar a identificaçao de alguns caracters ascii
 */
public class IdentificarCaracter {
    
    public static boolean isLetter(char c){
        int code = (int)c;
        if( (code >= 65 && code <= 90) || (code >= 97 && code <= 122) )
            return true;
        else
            return false;
    }

    public static boolean isNumber(char c){
        int code = (int) c;
        if(code >= 48 && code <= 57)
            return true;
        else
            return false;
    }

    public static boolean isTab(char c){
        int code = (int) c;
        if(code == 9){
            return true;
        }
        else
            return false;
    }
    public static boolean isUnderLine(char c){
        int code = (int) c;
        if(code == 95){
            return true;
        }
        else
            return false;
    }
    public static boolean isQuotationMarks(char c){
        int code = (int) c;
        if (code == 39 || code == 34){
            return true;
        }
        else
            return false;
    }
    /**
     * Método para tratar fim de linha/pulo de linha
     */
    public static boolean isLF_CR(char c)
    {
        int code = ((int)c);
        if(code == 10 || code == 13)
            return true;
        else
            return false;
    }

    public static boolean isLF(char c)
    {
        if(((int)c)==10){
            return true;
        }
        else
            return false;
    }
}
