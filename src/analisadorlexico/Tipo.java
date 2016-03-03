/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analisadorlexico;

/**
 *
 * @author Carolini
 */
class Tipo {
    private String token;// o token lido
    private String code; // o codigo do token
    private int line;
    
    
    Tipo(String token, String code, int line) {
        this.setCode(code);
        this.setToken(token);
        this.setLine(line);
    }
    
    /**
     * Metodo que retorna o token
     */
    public String getToken() {
        return token;
    }

    /**
     * Metodo que retorna o codigo do token
     */
    public String getCode() {
        return code;
    }
    
    public void setToken(String token) {
        this.token = token;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    @Override
    public String toString()
    {
        return (token+"  -  "+code+"\n");
    }
}
