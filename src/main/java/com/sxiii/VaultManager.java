package com.sxiii;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class VaultManager {
    
    private static final String VAULT_FILE = "vault.dat";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    // Estrutura do cofre em memória
    private Map<String, String> entries = new HashMap<>();
    private String masterPasswordHash;
    
    /**
     * Cria um novo cofre com senha mestra.
     */
    public void createVault(String masterPassword) throws Exception {
        // Hash da senha mestra (para verificar no futuro)
        masterPasswordHash = CryptoUtil.hashMasterPassword(masterPassword);
        
        // Serializar para JSON
        VaultData data = new VaultData();
        data.masterPasswordHash = masterPasswordHash;
        data.encryptedEntries = CryptoUtil.encrypt(gson.toJson(entries), masterPassword);
        
        // Salvar em arquivo
        String json = gson.toJson(data);
        Files.writeString(Path.of(VAULT_FILE), json);
        
        System.out.println("✅ Cofre criado com sucesso!");
    }
    
    /**
     * Abre um cofre existente com a senha mestra.
     */
    public boolean openVault(String masterPassword) throws Exception {
        if (!Files.exists(Path.of(VAULT_FILE))) {
            System.out.println("❌ Nenhum cofre encontrado. Crie um primeiro.");
            return false;
        }
        
        // Ler arquivo
        String json = Files.readString(Path.of(VAULT_FILE));
        VaultData data = gson.fromJson(json, VaultData.class);
        
        // Verificar senha mestra
        if (!CryptoUtil.verifyMasterPassword(masterPassword, data.masterPasswordHash)) {
            System.out.println("❌ Senha mestra incorreta!");
            return false;
        }
        
        // Descriptografar entradas
        String decryptedJson = CryptoUtil.decrypt(data.encryptedEntries, masterPassword);
        entries = gson.fromJson(decryptedJson, Map.class);
        masterPasswordHash = data.masterPasswordHash;
        
        System.out.println("✅ Cofre aberto com sucesso!");
        return true;
    }
    
    /**
     * Salva o cofre criptografado em disco.
     */
    public void saveVault(String masterPassword) throws Exception {
        VaultData data = new VaultData();
        data.masterPasswordHash = masterPasswordHash;
        data.encryptedEntries = CryptoUtil.encrypt(gson.toJson(entries), masterPassword);
        
        String json = gson.toJson(data);
        Files.writeString(Path.of(VAULT_FILE), json);
    }
    
    /**
     * Adiciona uma entrada no cofre.
     */
    public void addEntry(String serviceName, String password, String masterPassword) throws Exception {
        entries.put(serviceName, password);
        saveVault(masterPassword);
        System.out.println("✅ Entrada adicionada: " + serviceName);
    }
    
    /**
     * Busca uma senha no cofre.
     */
    public String getEntry(String serviceName) {
        return entries.get(serviceName);
    }
    
    /**
     * Lista todos os serviços salvos (sem mostrar senhas).
     */
    public void listEntries() {
        if (entries.isEmpty()) {
            System.out.println("📭 Cofre vazio.");
            return;
        }
        
        System.out.println("\n📋 Entradas no cofre:");
        for (String service : entries.keySet()) {
            System.out.println("   • " + service);
        }
    }
    
    /**
     * Remove uma entrada.
     */
    public void removeEntry(String serviceName, String masterPassword) throws Exception {
        if (entries.remove(serviceName) != null) {
            saveVault(masterPassword);
            System.out.println("🗑️  Entrada removida: " + serviceName);
        } else {
            System.out.println("❌ Entrada não encontrada.");
        }
    }
    
    // Classe auxiliar para serializar
    private static class VaultData {
        String masterPasswordHash;
        String encryptedEntries;
    }
}