package LL1;

import java.util.Objects;
import java.util.Stack;

/**
 LL1
 E -> TM
 M-> +TM| empty
 T -> FN
 N-> *FN| empty
 F -> (E)| i


 i         +           *        (           )         $
 --------------------------------------------------------------------
 E |  E->TM    |         |         |   E->TM  |          |       |
 --------------------------------------------------------------------
 M|           |  M->+TM|         |           |   M->   |  M-> |
 --------------------------------------------------------------------
 T |  T->FN   |          |         |   T->FN  |          |       |
 --------------------------------------------------------------------
 N|          |  N->     | N->*FN|           |  N->       | N->  |
 --------------------------------------------------------------------
 F |   F->i  |           |         |  F->(E)   |          |      |


 */

public class LL1 {
    StringBuilder input = new StringBuilder("i*i+i$"); //输入串
    int currindex = 0; //index of input
    String currchar = ""; //character of input
    Stack<String> stack = new Stack<>(); //stack to store rule
    boolean flag = true; //flag of the input can be analyzed
    String top = ""; //stack.peek()

    String[][] analysisTable = new String[][] {
            {"TM",   null,    null,  "TM", null, null},
            { null, "+TM",    null,   null,  "ε",  "ε"},
            {"FN",   null,    null,  "FN", null, null},
            { "",    "ε",  "*FN",   null,  "ε",  "ε"},
            { "i",   null,   null,  "(E)", null, null}
    }; //预测分析表
    String[] nonTer = new String[]{"E", "M", "T", "N", "F"}; //nonterminal
    String[] Ter = new String[]{"i", "+", "*", "(", ")", "$"}; //terminal



    private void initial(){
        stack.push("$");
        stack.push("E");
        System.out.printf("%-14s %-20s \n", "stack ","        input ");
        currChar();
        System.out.printf("%-22s %-38s  \n",stack.toString(),input.substring(currindex, input.length()));
    }


    private String currChar(){
        currchar = String.valueOf(input.charAt(currindex));
        return currchar;
    }

    private String stackpeek(){
        top = stack.peek();
        return top;
    }

    /*
    get corresponding rule of nonterminal
     */
    private String getRule(){
        int row = 0, col = 0;
        for (int i = 0; i < nonTer.length; i++){ //tell stack.peek() and nonterminal to get the row of the rule
            if(nonTer[i].equals(top)){
                row = i;
            }
        }

        for (int j = 0; j < Ter.length; j++){ //tell characterof input and terminal to get the column of the rule
            if (Ter[j].equals(currchar)){
                col = j;
            }
        }

        if (analysisTable[row][col] == null){
            error();
        }

        return analysisTable[row][col];

    }



    /*
    stack to store the rule
     */
    private void pushStack(){
        String previoustop = stack.peek();
        for (int i = getRule().length() - 1; i >= 0; i--){
            String ch = String.valueOf(getRule().charAt(i));
            stack.push(ch);
        }
        System.out.printf("%-20s %6s %-1s->%-12s\n",stack.toString(),input.substring(currindex, input.length()),top,getRule());
    }

    /*
    tell if it is terminal
     */
    private boolean isTerminal(String str){
        for (int i = 0; i < Ter.length - 1; i++){
            if (Ter[i].equals(str)){
                return true;
            }
        }

        return false;
    }



    private void control(){
        while (flag) {
            if (isTerminal(stackpeek())) { //if stack.peek() is terminal
                if (stackpeek().equals("$")) { //left=right=$
                    if (stackpeek().equals(currChar())) {
                        flag = false;
                        System.out.println("success");
                    } else {  //left is done but not for right
                        error();
                    }
                } else if (stackpeek().equals(currChar())) { //stack.peek()=currchar，stack pop, index of input++
                    currindex++;
                    stack.pop();
                    System.out.printf("%-20s %6s \n", stack.toString(), input.substring(currindex, input.length()));
                } else {
                    error();
                }
            } else { //if it is nonterminal, get the corresponding expression to put it in the stack
                if (currChar().equals("$")) { //input is done but stack is not, check if it is ε
                    if (getRule().equals("ε")) {
                        stack.pop();
                        System.out.printf("%-20s %6s %-1s->%-12s\n", stack.toString(), input.substring(currindex, input.length()), top, getRule());
                    }
                } else if (getRule().equals("ε")){
                    currindex++;
                    stack.pop();
                    System.out.printf("%-20s %6s %-1s->%-12s\n", stack.toString(), input.substring(currindex, input.length()), top, getRule());
                }
                else {
                    stack.pop();
                    pushStack();
                }
            }
        }
    }

    /*
    error, cannot analyze
     */

    private void error(){
        System.out.println("cannot analyze");
        System.exit(0);
    }




    public static void main(String[] args){
        LL1 parser = new LL1();
        parser.initial();
        parser.control();

    }

}
