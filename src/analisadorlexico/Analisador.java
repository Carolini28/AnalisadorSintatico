/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analisadorlexico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Carolini
 */
class Analisador {

    private Tabela table; //Tabela de palavras reservadas
    private StringBuffer word; //variavel utilizada para montar um token que eh composto por uma cadeia de caracters
    private Tipo element; //objeto que guarda o resultado da analise corrente
    private boolean end; //variavel que guarda se o arquivo que esta sendo processado chegou ao final ou nao
    private BufferedReader bufferIn; //buffer de leitura do arquivo
    private final int STATE_FINAL = 999;  //a máquina toda tem apenas um estado final
    private int line; //linha do arquivo que esta sendo processada
    private Erros erro; //objeto que guarda informaçoes sobre o erro
    private boolean flagErro; //flag que indica se houve erro ou não

    Analisador(List<String> list, File file) throws Exception {
        
                this.bufferIn = new BufferedReader(new FileReader(file));
                this.setTable(new Tabela(list));
                this.word = new StringBuffer();
                this.setEnd(false);
                line = 1;
                flagErro = false;
                erro = null;
    }


    public Tipo analise() throws Exception {
       int state = 0;
       char charLido;
       this.word.delete(0, this.word.length());
       flagErro = false;
       this.element = null;
       
        if (!isTerminate()){
                        while(bufferIn.ready()){  // fazendo leitura direto do disco
                             System.out.println("teste3");
                             bufferIn.mark(2); // marca o buffer para poder retroceder

                             int representacaoEmIntDoCharLido = bufferIn.read();
                             
                             charLido = (char) representacaoEmIntDoCharLido;
                             state = stateMachine(charLido,state);

                             if(state == STATE_FINAL){ // caso a máquina tenha alcançado seu estado final então saia do laço
                                 System.out.println("break");      
                                 break;
                             }
                        }

                        if (!bufferIn.ready()){ // caso o arquivo tenha chegado ao fim
                        
                            System.out.println("bufferIn");  
                                if(state == 27)  //caso o arquivo tenha terminado antes de chegar do estado final da parte de comentarios
                                {                                    
                                    flagErro = true;
                                    this.addToElement("{... EOF", "erro");
                                    erro = new Erros("comentario nao fechado","{... EOF",this.line,"Léxico");
                                }
                                else
                                {
                                    bufferIn.mark(0);
                                        if(state == 0)   // caso o arquivo tenha chegado no final e a maquina esteja no estado 0, ou seja, o metodo foi invocado mas o arquivo nao tinha nada de util
                                        {
                                            element = null;
                                        }
                                        else  //caso o arquivo tenha chegado ao final e a maquina de estados esteja ativa, mas nao em um estado final
                                          state = stateMachine('ª',state); //insere um caracter invalido para forçar a maquina entrar no estado final
                                }
                        }
               
                }
           return (this.element);
    }

      public boolean isTerminate() {
        try{
                if(!bufferIn.ready()){
                     bufferIn.close(); // fecha o buffer de leitura do arquivo
                     this.setEnd(true);
                }
        }catch(IOException e){
                e.printStackTrace();
        }
        return end;
    };
     
    /**
     * Método que monta o elemento que sera a resposta da analise lexica
     * @param token token resultante da analise
     * @param code codigo do token resultante da analise
     */
    private void addToElement(String token, String code)
    {
        element = new Tipo(token, code, line);
    }
    
    /**
     * Método que retrocede em uma posição o ponteiro de leitura do arquivo
     */
    private void retroceder(){
            try{
                bufferIn.reset();
            }
            catch(IOException e){

            }
    }
    
    public int getLine()
    {
        return this.line;
    }
    
    boolean hasErro() {
        return flagErro;
    }

    public Erros getErro() {
           if(hasErro())
               return erro;
           else
               return null;
    }

    /**
     * Método que vai "montando" o token que é composto por mais de um caracter
     * @param letter caracter a ser adicionado no token
     */
    private void appendWord(char letter)
    {
        this.word.append(letter);
    }
    
    private void setTable(Tabela tabela) {
         this.table = tabela;
    }

    private void setEnd(boolean end) {
        this.end = end;
    }
 
     /**
     * Método que contém a máquina de estados
     * @param input caracter de entrada
     * @param stateCurrent estado atual da máquina
     * @return retorna o estado em que a máquina ficou após a entrada de input
     */
    
    private int stateMachine (char input, int stateCurrent){

            switch(stateCurrent)
            {
                case 0:
                    switch(input)
                    {
                        case ' ':
                            stateCurrent = 0;
                            break;
                        case '>':
                            stateCurrent = 1;
                            break;
                        case '<':
                            stateCurrent = 2;
                            break;
                        case '=':
                            //stateCurrentCurrent = 8;
                            this.addToElement("=", "op_igual");
                            stateCurrent = STATE_FINAL;
                            break;
                        case '+':
                            //stateCurrent = 11;
                            this.addToElement("+", "+");
                            stateCurrent = STATE_FINAL;
                            break;
                        case '-':
                            //stateCurrent = 12;
                            this.addToElement("-", "-");
                            stateCurrent = STATE_FINAL;
                            break;
                        case '*':
                            //stateCurrent = 25;
                            this.addToElement("*", "op_mult");
                            stateCurrent = STATE_FINAL;
                            break;
                        case '/':
                            //stateCurrent = 26;
                            this.addToElement("/", "op_div");
                            stateCurrent = STATE_FINAL;
                            break;
                        case '{':
                            stateCurrent = 27;
                            break;
                        case ':':
                            stateCurrent = 35;
                            break;
                        case '(':
                            //stateCurrent = 38;
                            this.addToElement("(", "(");
                            stateCurrent = STATE_FINAL;
                            break;
                        case ')':
                            //stateCurrent = 39;
                            this.addToElement(")", ")");
                            stateCurrent = STATE_FINAL;
                            break;
                        case '.':
                            //stateCurrent = 40;
                            this.addToElement(".", ".");
                            stateCurrent = STATE_FINAL;
                            break;
                        case ';':
                            //stateCurrent = 41;
                            this.addToElement(";", ";");
                            stateCurrent = STATE_FINAL;
                            break;
                        case ',':
                            //stateCurrent = 42;
                            this.addToElement(",", ",");
                            stateCurrent = STATE_FINAL;
                            break;
                        
                        default: //senao nao for nenhum acima...
                            if (IdentificarCaracter.isLetter(input)){                
                                stateCurrent = 9;
                                appendWord(input);
                            }
                            else{
                                if (IdentificarCaracter.isNumber(input)){                        
                                    stateCurrent = 13;
                                    appendWord(input);
                                }
                                else
                                {
                                    if(IdentificarCaracter.isLF_CR(input))
                                    {
                                        stateCurrent = 0;
                                    }
                                    else
                                    {
                                        if(IdentificarCaracter.isTab(input))
                                        {
                                            stateCurrent = 0;
                                        }
                                        else
                                        {
                                            this.addToElement(String.valueOf(input),"erro");
                                            flagErro = true;
                                            erro = new Erros("Caractere inválido",String.valueOf(input),this.line,"Léxico");
                                            stateCurrent = STATE_FINAL;
                                        }
                                    }
                                }
                            }
                       break;
                   } //switch c
                   break; //estado 0
                   
                case 1:
                    switch (input)
                    {
                        case '=':
                            //stateCurrent = 3;
                            this.addToElement(">=", "op_maior_igual");
                            stateCurrent = STATE_FINAL;
                            break;
                        default:
                            //stateCurrent = 4;
                            this.addToElement(">", "op_maior");
                            retroceder();
                            stateCurrent = STATE_FINAL;
                            break;
                    }
                    break;
                case 2:
                    switch(input)
                    {
                        case '>':
                            //stateCurrent = 5
                            this.addToElement("<>", "op_diferente" );
                            stateCurrent = STATE_FINAL;
                            break;
                        case '=':
                            //stateCurrent = 6
                            this.addToElement("<=", "op_menor_igual");
                            stateCurrent = STATE_FINAL;
                            break;
                        default:
                            this.addToElement("<", "op_menor");
                            retroceder();
                            stateCurrent = STATE_FINAL;
                            break;
                    } //switch c
                    break;

                case 9:
                    if(IdentificarCaracter.isLetter(input)||IdentificarCaracter.isNumber(input))
                    {
                        //stateCurrent = 9;
                        appendWord(input);
                    }
                    else{
                        if(this.table.hasWord(word.toString())) // trata palavras reservadas
                        {
                            this.addToElement(word.toString(), word.toString());
                            stateCurrent = STATE_FINAL;
                        }
                        else
                        {
                            //stateCurrent = 10
                            this.addToElement(word.toString(), "ID");
                            stateCurrent = STATE_FINAL;
                        }
                        if(!IdentificarCaracter.isLF(input)){
                            retroceder();
                        }
                    }
                    break;

                case 11:
                    break;

                case 13:
                    switch(input)
                    {
                        case '.':
                            stateCurrent = 14;
                            appendWord(input);
                            break;
                        default:
                            if(IdentificarCaracter.isNumber(input))
                            {
                                //stateCurrent = 13;
                                appendWord(input);
                            }
                            else
                            {
                                if(IdentificarCaracter.isLetter(input))
                                {
                                   stateCurrent = 20;
                                   appendWord(input);
                                }
                                else
                                {
                                    //stateCurrent=16;
                                    this.addToElement(word.toString(), "integer");
                                    stateCurrent = STATE_FINAL;
                                    retroceder();
                                }
                            }
                            break;

                    }//switch c
                    break;
                case 14:
                    if(IdentificarCaracter.isNumber(input))
                    {
                        stateCurrent = 15;
                        appendWord(input);
                    }
                    else
                    {
                        if(IdentificarCaracter.isLetter(input))
                        {
                           stateCurrent = 20;
                           appendWord(input);
                        }
                        else
                        {
                            //stateCurrent = 17;
                            this.addToElement(word.toString(), "erro");
                            flagErro = true;
                            erro = new Erros("Número real mal formado",word.toString(),this.line,"Léxico");
                            stateCurrent = STATE_FINAL;
                            retroceder();
                        }
                    }
                    break;
                case 15:
                    if(IdentificarCaracter.isNumber(input))
                    {
                        //stateCurrent = 15;
                        appendWord(input);
                    }
                    else
                    {
                        if(IdentificarCaracter.isLetter(input))
                        {
                           stateCurrent = 20;
                           appendWord(input);
                        }
                        else
                        {
                            //stateCurrent = 19;
                            this.addToElement(word.toString(), "real");
                            stateCurrent = STATE_FINAL;
                            retroceder();
                        }
                    }
                    break;
                case 20:
                    if(IdentificarCaracter.isLetter(input) || IdentificarCaracter.isNumber(input))
                    {
                       stateCurrent = 20;
                       appendWord(input);
                    }
                    else
                    {
                        //stateCurrent = 21;
                        this.addToElement(word.toString(), "erro");
                        flagErro = true;
                        erro = new Erros("Número real mal formado",word.toString(),this.line,"Léxico");
                        stateCurrent = STATE_FINAL;
                        retroceder();
                    }
                    break;
                case 27:
                    if(input == '}')
                    {
                        stateCurrent = 0;
                    }
                    else
                    {
                        //stateCurrent = 27;
                    }
                    break;
                    
                case 35:
                    if(input == '=')
                    {
                        //stateCurrent = 36;
                        this.addToElement(":=", "atribuição");
                        stateCurrent = STATE_FINAL;
                    }
                    else
                    {
                        //stateCurrent = 37;
                        this.addToElement(":", ":");
                        stateCurrent = STATE_FINAL;
                        retroceder();
                    }
                    break;
            }//switch


            //para contar as linhas do codigo
            if(IdentificarCaracter.isLF(input))
            {                
                  this.line++;
            }

       return stateCurrent;
    }
    
}
