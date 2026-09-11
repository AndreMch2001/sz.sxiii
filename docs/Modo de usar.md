# Como iniciar o Vault

Este guia explica como abrir o cofre de senhas no Windows usando o arquivo `iniciar.bat`. Você não precisa saber Java: basta clicar no arquivo certo.

---

## O que você precisa ter instalado

Antes da primeira vez, o computador precisa de duas ferramentas:

1. **Java 21** (ou mais novo) — roda o programa  
2. **Maven** — monta o projeto na primeira execução  

Para conferir, abra o **Prompt de Comando** e digite:

```bat
java -version
mvn -version
```

Se aparecer um número de versão, está pronto. Se aparecer “não é reconhecido como um comando”, instale o que faltar e **abra uma janela nova** do CMD depois da instalação.

---

## Como abrir o programa

1. Abra a pasta do projeto (a mesma pasta onde estão `iniciar.bat` e `pom.xml`).
2. Dê **dois cliques** em `iniciar.bat`.
3. Uma janela preta (CMD) abre sozinha.

Na **primeira vez**, pode aparecer `Compilando o projeto...` e demorar um pouco. Nas próximas, o cofre sobe mais rápido.

4. Use o programa nessa janela: digite as respostas e pressione **Enter**.
5. Para sair, escolha a opção **5** no menu.
6. Quando terminar, pressione qualquer tecla para fechar a janela (`Pressione qualquer tecla para continuar...`).

Não feche a janela no **X** enquanto estiver usando o cofre. Saia pelo menu.

---

## O que acontece na tela

### Primeira execução (ainda não existe cofre)

O programa pede uma **senha mestra** duas vezes. Essa senha abre o cofre nas próximas vezes. Anote-a em um lugar seguro: **não há recuperação** se você esquecer.

Depois disso aparece o menu:

```text
1. Adicionar senha
2. Buscar senha
3. Listar todas (sem senhas)
4. Remover senha
5. Sair
```

Digite o número da opção e pressione **Enter**.

### Próximas execuções

O programa pede a senha mestra. Se estiver correta, o menu aparece de novo.

O arquivo do cofre se chama `vault.dat` e fica **na mesma pasta** do `iniciar.bat`. Não apague esse arquivo se quiser manter as senhas salvas.

---

## Atalho na área de trabalho (opcional)

1. Clique com o botão direito em `iniciar.bat`.
2. Escolha **Enviar para → Área de trabalho (criar atalho)**.
3. Use esse atalho para abrir o cofre sem procurar a pasta.

---

## Se algo der errado

| O que aparece | O que fazer |
|---|---|
| `java` não é reconhecido | Instale o Java 21 e abra o `iniciar.bat` de novo |
| `mvn` não é reconhecido | Instale o Maven (só é necessário na primeira compilação) |
| A janela abre e fecha na hora | Rode o `iniciar.bat` de novo; no final ela espera uma tecla e mostra o erro |
| Emojis aparecem como `?` | Use o `iniciar.bat` (ele já configura UTF-8). Evite rodar o `.java` direto no CMD antigo |
| Senha mestra incorreta | Tente de novo. Sem a senha certa o cofre não abre |
| As senhas “sumiram” | Confira se você abriu o `iniciar.bat` **desta** pasta. O `vault.dat` fica onde o programa foi iniciado |

---

## Resumo

1. Instale Java 21 e Maven.  
2. Dê dois cliques em `iniciar.bat`.  
3. Crie ou digite a senha mestra.  
4. Use o menu.  
5. Saia pela opção **5**.
