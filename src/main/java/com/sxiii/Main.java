package com.sxiii;
import org.mindrot.jbcrypt.BCrypt;
import java.util.HashMap;
import java.util.Map;

public class Main {

    // Simula nosso banco de dados (na vida real, seria MySQL, PostgreSQL, etc.)
    private static Map<String, String> usuariosDB = new HashMap<>();

    public static void main(String[] args) {
        Emoji emoji = new Emoji();
        emoji.Emoji(args);
        System.out.println("=== SISTEMA DE CADASTRO E LOGIN ===\n");

        // 1. CADASTRAR USUÁRIOS
        cadastrarUsuario("ana", "senha123");
        cadastrarUsuario("bruno", "123456");
        cadastrarUsuario("carla", "minhasenha");

        System.out.println("Usuários cadastrados!\n");

        // 2. TENTAR LOGIN
        testarLogin("ana", "senha123");   // ✅ Deu certo
        testarLogin("ana", "senhaerrada"); // ❌ Deu errado
        testarLogin("bruno", "123456");    // ✅ Deu certo
        testarLogin("carla", "carla");     // ❌ Deu errado
    }

    // Cadastro: recebe a senha e salva o hash
    public static void cadastrarUsuario(String usuario, String senhaPlana) {
        // Gera um hash BCrypt com custo 12 (pode ser 10, 11, 13, 14...)
        String hash = BCrypt.hashpw(senhaPlana, BCrypt.gensalt(12));
        
        // Salva no "banco de dados"
        usuariosDB.put(usuario, hash);
        
        System.out.println("✅ Usuário '" + usuario + "' cadastrado com sucesso!");
        System.out.println("   Hash salvo: " + hash.substring(0, 30) + "...");
    }

    // Login: verifica se a senha digitada bate com o hash salvo
    public static void testarLogin(String usuario, String senhaDigitada) {
        // Pega o hash salvo para esse usuário
        String hashSalvo = usuariosDB.get(usuario);

        if (hashSalvo == null) {
            System.out.println("❌ Usuário '" + usuario + "' não encontrado!");
            return;
        }

        // BCrypt verifica se a senha digitada + sal embutido = hash salvo
        boolean loginSucesso = BCrypt.checkpw(senhaDigitada, hashSalvo);

        if (loginSucesso) {
            System.out.println("✅ LOGIN SUCESSO: " + usuario);
        } else {
            System.out.println("❌ LOGIN FALHOU: senha incorreta para " + usuario);
        }
    }
}