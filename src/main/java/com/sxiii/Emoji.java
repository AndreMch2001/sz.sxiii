package com.sxiii;

public class Emoji {

    public void Emoji(String[] args){
        try {
            new ProcessBuilder("cmd","/c","chcp", "65001").inheritIO().start().waitFor();
        }catch (Exception e) {
            System.out.println("Erro ao alterar a codificação para UTF-8: " + e.getMessage());
        }
    }
}