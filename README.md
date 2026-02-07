# Best Financeiro

Sistema de gestao financeira desenvolvido em Java. Um CRUD para controle de receitas e despesas, utilizando banco de dados e operacoes via terminal.

## Como usar o Git no projeto

### 1. Antes de tudo, atualize seu repositorio local

Sempre antes de comecar a trabalhar, puxe as ultimas alteracoes:

```bash
git pull origin main
```

### 2. Crie sua branch

Nunca trabalhe direto na `main`. Crie uma branch com um nome descritivo:

```bash
git checkout -b nome-da-sua-branch
```

Exemplos:
```bash
git checkout -b feature/cadastro-transacao
git checkout -b fix/corrigir-saldo
```

### 3. Faca suas alteracoes e adicione ao git

Depois de modificar os arquivos, adicione tudo:

```bash
git add .
```

### 4. Faca o commit

```bash
git commit -m "descricao do que voce fez"
```

### 5. Envie sua branch para o GitHub

```bash
git push origin nome-da-sua-branch
```

### Resumo rapido

```bash
git pull origin main
git checkout -b minha-branch
# faca suas alteracoes...
git add .
git commit -m "minha alteracao"
git push origin minha-branch
```
