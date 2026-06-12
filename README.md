# Best Financeiro

Sistema de gestao financeira pessoal desenvolvido em **Java**, rodando 100% via terminal. Permite cadastro e login de usuarios, registro de receitas e despesas, saldo, relatorios, exportacao em CSV, projecao de gastos do mes e sugestoes de economia.

> Quer entender em detalhe tudo que o sistema faz (com exemplos de tela)? Le o [APRESENTACAO.md](APRESENTACAO.md).

---

## 1. Pre-requisitos e instalacao

### Versoes necessarias
- **Java JDK 17 ou superior** (o projeto foi desenvolvido e testado no **JDK 21**)
- **Git** (so pra clonar o repositorio)
- **sqlite3** (opcional, apenas se quiser aplicar o dump do banco na mao)

### Dependencias
Nao precisa instalar banco de dados. O driver do **SQLite** ja vem incluso na pasta `lib/`:
- `sqlite-jdbc.jar` - driver JDBC do SQLite
- `slf4j-api.jar` e `slf4j-nop.jar` - dependencias do driver

### Passo a passo da instalacao
```bash
# 1. Clonar o repositorio
git clone https://github.com/Gabrieldiog/best-financeiro.git

# 2. Entrar na pasta
cd best-financeiro

# 3. Conferir a versao do Java (precisa ser 17+)
java -version

# 4. Pronto! Rode o sistema (ver secao 3)
./run.sh
```

Nao ha configuracao de ambiente extra: o banco `financeiro.db` e criado automaticamente na primeira execucao.

---

## 2. Banco de dados (dump inicial)

O sistema usa duas tabelas: `usuarios` e `movimentacoes`.

O arquivo [database.sql](database.sql) contem o **dump inicial**: os comandos `CREATE TABLE` para criar as tabelas e os `INSERT` para popular com dados de teste.

### Aplicar o dump (opcional)
Voce **nao precisa** rodar isso pra usar o sistema (o app cria as tabelas sozinho). Use apenas se quiser ja comecar com dados de teste via SQL:
```bash
sqlite3 financeiro.db < database.sql
```

Depois de aplicar, ja existe um usuario pronto:
- **Usuario:** `teste`
- **Senha:** `Teste$1`

> A senha no dump esta com hash (SHA-256 + salt), igual o sistema salva. O hash do dump corresponde a senha `Teste$1`.

### Estrutura das tabelas

**usuarios**
| Campo | Tipo | Descricao |
|-------|------|-----------|
| id | INTEGER PK | Identificador do usuario |
| username | TEXT unico | Nome de login |
| senha | TEXT | Hash da senha (formato `salt:hash`) |

**movimentacoes**
| Campo | Tipo | Descricao |
|-------|------|-----------|
| id | INTEGER PK | Identificador da movimentacao |
| id_usuario | INTEGER FK | Dono da movimentacao |
| tipo | TEXT | RECEITA ou DESPESA |
| valor | REAL | Valor de uma ocorrencia |
| descricao | TEXT | Descricao livre |
| data_movimentacao | TEXT | Data (aaaa-mm-dd) |
| categoria | TEXT | Categoria |
| frequencia | INTEGER | Quantas vezes por mes acontece (padrao 1) |

---

## 3. Como rodar o projeto

### Linux / Mac
```bash
./run.sh
```

### Windows
```bash
RunInWindows.bat
```

Os scripts compilam e ja executam o sistema.

### Quer testar com dados prontos?
O script abaixo recria o banco com um usuario de teste e varias movimentacoes (receitas, despesas e gastos recorrentes):
```bash
./seed.sh
```
Depois rode `./run.sh` e logue com `teste` / `Teste$1`.

### Compilar e rodar na mao
```bash
javac -cp "lib/*" -d out $(find src/main/java -name "*.java")
java -cp "out:lib/*" App
```

---

## 4. Como testar cada funcao

O sistema e um CLI (terminal): voce navega digitando o **numero** da opcao e apertando Enter. Nao ha argumentos de linha de comando; tudo e interativo.

### Tela inicial
| Digite | Funcao | O que faz / o que informar |
|--------|--------|----------------------------|
| `1` | Cadastrar | Cria conta. Pede username (unico) e senha forte: min 6 caracteres, 1 maiuscula, 1 especial `) ( / & % $`, nao pode ser `123456` |
| `2` | Login | Pede username e senha. Se corretos, abre o menu do usuario |
| `3` | Sair | Encerra o sistema |

### Menu do usuario (apos login)
| Digite | Funcao | O que faz / o que informar |
|--------|--------|----------------------------|
| `1` | Registrar movimentacao | Pede tipo (1 receita / 2 despesa), valor, quantas vezes por mes (Enter = 1), categoria (lista ou criar nova), descricao e data (Enter = hoje) |
| `2` | Listar movimentacoes | Mostra todas as movimentacoes do usuario com id, data, tipo, categoria, valor e descricao |
| `3` | Editar movimentacao | Pede o id e abre um menu pra mudar so o campo que quiser (valor, frequencia, tipo, categoria, descricao ou data) |
| `4` | Excluir movimentacao | Pede o id e confirmacao (s/n) antes de apagar |
| `5` | Ver saldo | Mostra total de receitas, total de despesas e o saldo |
| `6` | Relatorios | Mostra os totais e o gasto por categoria com percentual |
| `7` | Projecao de gastos do mes | Calcula o gasto mensal previsto (valor x quantas vezes acontece) e compara com a renda |
| `8` | Sugestao de economia | Diagnostico financeiro + onde economizar, com calculo mensal e anual |
| `9` | Exportar CSV | Gera um arquivo CSV na pasta `excel/` com todas as movimentacoes |
| `10` | Logout | Volta pra tela inicial |

> **Dica:** durante o registro ou a edicao, digitar `0` em qualquer campo **cancela** a operacao e volta pro menu sem salvar.

### Roteiro rapido de teste
```
2          (login)
teste      (usuario de teste)
Teste$1    (senha)
2          (listar - ve as movimentacoes)
5          (saldo)
6          (relatorios)
7          (projecao do mes)
8          (sugestao de economia)
9          (exportar CSV)
10         (logout)
3          (sair)
```

---

## 5. Organizacao de pastas e arquivos

```
best-financeiro/
├── README.md                   # este arquivo
├── APRESENTACAO.md             # guia detalhado de todas as funcoes
├── database.sql                # dump inicial do banco (tabelas + dados de teste)
├── run.sh                      # compila e roda (Linux/Mac)
├── RunInWindows.bat            # compila e roda (Windows)
├── seed.sh                     # recria o banco com dados de teste
├── financeiro.db               # banco SQLite (gerado em runtime, nao versionado)
├── lib/                        # dependencias (driver JDBC do SQLite + slf4j)
├── docs/                       # documentacao em HTML
├── excel/                      # CSVs exportados (gerado em runtime)
└── src/main/java/
    ├── App.java                # ponto de entrada: menus e leitura do terminal
    ├── model/                  # entidades (os dados)
    │   ├── Usuario.java
    │   ├── Read.java           # uma movimentacao
    │   ├── Projecao.java
    │   ├── AnaliseFinanceira.java
    │   └── Sugestao.java
    ├── DAO/                    # acesso ao banco (Data Access Object)
    │   ├── UsuarioDAO.java     # cadastro, login
    │   ├── CreateDAO.java      # inserir movimentacao
    │   ├── ReadDAO.java        # listar e buscar
    │   ├── UpdateDAO.java      # editar
    │   ├── DeleteDAO.java      # excluir
    │   ├── RelatorioDAO.java   # saldo, totais, gastos por categoria
    │   └── GastosMensaisDAO.java # somas do mes (projecao/analise)
    ├── service/               # regras de negocio
    │   └── Financas.java       # projecao de gastos e sugestao de economia
    └── utils/                 # ferramentas de apoio
        ├── Conexao.java        # conexao JDBC e criacao das tabelas
        ├── Hash.java           # hash de senha (SHA-256 + salt)
        ├── Moeda.java          # formatacao em Real (R$)
        ├── DataUtil.java       # conversao de datas
        ├── ExportadorCSV.java  # exportacao em CSV
        └── CancelamentoException.java # cancelar operacao no meio
```

### Camadas (responsabilidades)
- **App** - interface de terminal (menus, leitura e validacao de entrada)
- **model** - as entidades de dados
- **DAO** - operacoes de banco (CRUD e consultas)
- **service** - regras de negocio (projecao e analise)
- **utils** - apoio (conexao, hash, moeda, datas, CSV)

---

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
