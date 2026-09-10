package com.sxiii;

import java.util.Scanner;

public class Main {
    private static ConfigTerminal configTerminal = new ConfigTerminal();
    private static Scanner scanner = new Scanner(System.in);
    private static VaultManager vault = new VaultManager();
    private static String currentMasterPassword = null;
    
    public static void main(String[] args) throws Exception {
        configTerminal.terminalConfig(args);
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║       🔐 VAULT - SEU COFRE         ║");
        System.out.println("╚════════════════════════════════════╝\n");
        
        // Verificar se já existe cofre
        if (new java.io.File("vault.dat").exists()) {
            System.out.print("Digite a senha mestra: ");
            String password = scanner.nextLine();
            
            if (vault.openVault(password)) {
                currentMasterPassword = password;
                showMenu();
            }
        } else {
            System.out.println("🆕 Nenhum cofre encontrado. Vamos criar um!\n");
            System.out.print("Escolha uma senha mestra forte: ");
            String password = scanner.nextLine();
            
            System.out.print("Confirme a senha mestra: ");
            String confirm = scanner.nextLine();
            
            if (!password.equals(confirm)) {
                System.out.println("❌ As senhas não conferem. Tente novamente.");
                return;
            }
            
            vault.createVault(password);
            currentMasterPassword = password;
            showMenu();
        }
    }
    
    private static void showMenu() throws Exception {
        boolean running = true;
        
        while (running) {
            System.out.println("\n═══════════════════════════════════");
            System.out.println("1. ➕ Adicionar senha");
            System.out.println("2. 🔍 Buscar senha");
            System.out.println("3. 📋 Listar todas (sem senhas)");
            System.out.println("4. 🗑️  Remover senha");
            System.out.println("5. 🚪 Sair");
            System.out.println("═══════════════════════════════════");
            System.out.print("Escolha: ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1": addPassword(); break;
                case "2": searchPassword(); break;
                case "3": vault.listEntries(); break;
                case "4": removePassword(); break;
                case "5": 
                    running = false;
                    System.out.println("🔒 Cofre fechado. Até logo!");
                    break;
                default: System.out.println("❌ Opção inválida.");
            }
        }
    }
    
    private static void addPassword() throws Exception {
        System.out.print("\nNome do serviço (ex: Gmail, Netflix): ");
        String service = scanner.nextLine();
        
        System.out.print("Senha: ");
        String password = scanner.nextLine();
        
        vault.addEntry(service, password, currentMasterPassword);
    }
    
    private static void searchPassword() {
        System.out.print("\nNome do serviço: ");
        String service = scanner.nextLine();
        
        String password = vault.getEntry(service);
        if (password != null) {
            System.out.println("🔑 Senha de " + service + ": " + password);
        } else {
            System.out.println("❌ Serviço não encontrado.");
        }
    }
    
    private static void removePassword() throws Exception {
        System.out.print("\nNome do serviço para remover: ");
        String service = scanner.nextLine();
        
        vault.removeEntry(service, currentMasterPassword);
    }
}