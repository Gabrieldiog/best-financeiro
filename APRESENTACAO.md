# Best Financeiro - Guia Completo do Sistema

Esse documento explica TUDO que o sistema faz, pra quem for montar o slide nao ficar com duvida.
Le do comeco ao fim que da pra entender o projeto inteiro.

---

## 1. O que e o sistema

O **Best Financeiro** e um sistema de controle financeiro pessoal feito em **Java**, que roda **so no terminal** (linha de comando). A pessoa cria uma conta, faz login, e a partir dai registra suas receitas (dinheiro que entra) e despesas (dinheiro que sai). Com base nisso o sistema mostra saldo, relatorios, faz projecao de gastos e ate sugere onde economizar.

Cada usuario so enxerga os proprios dados (e separado por login).

**Resumo em uma frase:** um app de financas pessoais de terminal, com login seguro, CRUD de movimentacoes e analises inteligentes de gasto.

---

## 2. Tecnologias usadas

| Tecnologia | Pra que serve |
|------------|----------------|
| **Java 21** | Linguagem do sistema todo |
| **SQLite** | Banco de dados relacional (arquivo `financeiro.db`) |
| **JDBC** | Ponte entre o Java e o banco |
| **SHA-256 + salt** | Criptografia (hash) das senhas |
| **Scanner** | Leitura do que o usuario digita no terminal |
| **Git** | Controle de versao do codigo |

> Nao precisa instalar banco nenhum: o SQLite e um arquivo, e o driver ja vem na pasta `lib/`. O banco e criado sozinho na primeira execucao.

---

## 3. Como rodar

### No Linux / Mac
```bash
./run.sh
```

### No Windows
```bash
RunInWindows.bat
```

Esses scripts **compilam e ja executam** o sistema.

### Pra testar com dados prontos
Tem um script que cria um usuario de teste ja com varias movimentacoes:
```bash
./seed.sh
```
Depois e so rodar `./run.sh` e logar com:
- **Usuario:** `teste`
- **Senha:** `Teste$1`

---

## 4. Os requisitos (e onde cada um foi atendido)

### Requisitos Funcionais (o que o sistema FAZ)

| Codigo | Requisito | Onde esta |
|--------|-----------|-----------|
| RF01 | Cadastro e login de usuarios | `App` + `UsuarioDAO` + `Hash` |
| RF02 | Registrar receitas e despesas (valor, data, descricao, categoria) | `CreateDAO` |
| RF03 | Editar e excluir transacoes | `UpdateDAO` + `DeleteDAO` |
| RF04 | Listar todas as movimentacoes | `ReadDAO` |
| RF05 | Categorizar as transacoes | `App` (categorias por tipo + criar novas) |
| RF06 | Mostrar o saldo atual | `RelatorioDAO` |
| RF07 | Exportar em CSV | `ExportadorCSV` |
| RF08 | Relatorios por categoria e total | `RelatorioDAO` |
| RF09 | Projecao de gastos do mes | `Financas` + `GastosMensaisDAO` |
| RF10 | Sugerir onde economizar | `Financas` |

### Requisitos Nao Funcionais (COMO o sistema e)

| Codigo | Requisito | Como foi atendido |
|--------|-----------|-------------------|
| RNF01 | Desenvolvido em Java | Sim, Java 21 |
| RNF02 | Banco de dados relacional | SQLite |
| RNF03 | Documentacao de todas as partes | `docs/` + este guia |
| RNF04 | Senhas com hash | SHA-256 + salt (nunca em texto plano) |
| RNF05 | Operar em Real (R$) | Classe `Moeda` formata tudo em R$ |
| RNF06 | Responder em tempo habil | Consultas simples e indexadas |
| RNF07 | Funcionar so via terminal | Sim, tudo no terminal |
| RNF08 | Adicionar usuarios sem perder desempenho | `username` unico e indexado |

---

## 5. Arquitetura (como o codigo e organizado)

O sistema segue uma separacao em **camadas**, cada uma com sua responsabilidade. Isso deixa o codigo organizado e facil de manter.

```
App (terminal/menus)
        |
        v
DAO (acesso ao banco)  +  Service (regras de negocio)
        |
        v
Conexao (JDBC)
        |
        v
SQLite (financeiro.db)
```

### O que cada pasta faz

| Camada | Responsabilidade |
|--------|------------------|
| `App.java` | Mostra os menus, le e valida o que o usuario digita |
| `model/` | As "entidades" (os dados): usuario, movimentacao, etc |
| `DAO/` | "Data Access Object": conversa com o banco (salvar, buscar, atualizar, apagar) |
| `service/` | Regras de negocio mais espertas (projecao e analise) |
| `utils/` | Ferramentas de apoio (conexao, hash, moeda, datas, CSV) |

### As classes (arquivos)

**model/**
- `Usuario` - dados do usuario (id, username, senha)
- `Read` - dados de uma movimentacao (tipo, valor, frequencia, categoria, descricao, data)
- `Projecao` - resultado da projecao de gastos
- `AnaliseFinanceira` - resultado da analise (renda, gastos, diagnostico, sugestoes)
- `Sugestao` - uma dica de economia (categoria, quanto cortar, quanto economiza)

**DAO/** (cada operacao do CRUD tem sua classe)
- `UsuarioDAO` - cadastro, login e checar se username existe
- `CreateDAO` - **C**reate: inserir movimentacao
- `ReadDAO` - **R**ead: listar e buscar movimentacoes
- `UpdateDAO` - **U**pdate: editar movimentacao
- `DeleteDAO` - **D**elete: excluir movimentacao
- `RelatorioDAO` - saldo, totais e gastos por categoria
- `GastosMensaisDAO` - somas do mes (pra projecao e analise)

**service/**
- `Financas` - faz a projecao de gastos e a analise/sugestao de economia

**utils/**
- `Conexao` - conecta no banco e cria as tabelas
- `Hash` - gera e confere o hash das senhas
- `Moeda` - formata os valores em Real (R$ 1.234,56)
- `DataUtil` - converte datas (dd/mm/aaaa para o formato do banco e vice-versa)
- `ExportadorCSV` - gera o arquivo CSV
- `CancelamentoException` - usada pra cancelar uma operacao no meio

---

## 6. O banco de dados

Sao duas tabelas:

### Tabela `usuarios`
| Campo | Tipo | Descricao |
|-------|------|-----------|
| id | INTEGER | Identificador unico |
| username | TEXT (unico) | Nome de login |
| senha | TEXT | Hash da senha (formato `salt:hash`) |

### Tabela `movimentacoes`
| Campo | Tipo | Descricao |
|-------|------|-----------|
| id | INTEGER | Identificador da movimentacao |
| id_usuario | INTEGER | De quem e a movimentacao (liga com usuarios) |
| tipo | TEXT | RECEITA ou DESPESA |
| valor | REAL | Valor de uma ocorrencia |
| descricao | TEXT | Descricao livre |
| data_movimentacao | TEXT | Data (formato aaaa-mm-dd) |
| categoria | TEXT | Categoria da movimentacao |
| frequencia | INTEGER | Quantas vezes por mes acontece (padrao 1) |

---

## 7. Funcionalidades em detalhe

### 7.1 Cadastro (RF01)
Pede um username (tem que ser unico) e uma senha. A senha precisa ser **forte**:
- minimo 6 caracteres
- pelo menos 1 letra maiuscula
- pelo menos 1 caractere especial entre `) ( / & % $`
- nao pode ser `123456`
- tem que confirmar a senha digitando de novo

A senha **nunca** e salva como texto. Ela vira um **hash** (com SHA-256 + um "salt" aleatorio). Mesmo quem abrir o banco nao consegue ler a senha real.

### 7.2 Login (RF01)
Confere o username e o hash da senha. Se bater, entra no menu do usuario.

### 7.3 Menu do usuario
Depois de logar, aparecem estas opcoes:
```
1 - Registrar movimentacao
2 - Listar movimentacoes
3 - Editar movimentacao
4 - Excluir movimentacao
5 - Ver saldo
6 - Relatorios
7 - Projecao de gastos do mes
8 - Sugestao de economia
9 - Exportar CSV
10 - Logout
```

### 7.4 Registrar movimentacao (RF02, RF05)
Pergunta na ordem:
1. Tipo (Receita ou Despesa)
2. Valor de uma vez (ex: R$ 20)
3. **Quantas vezes por mes isso acontece** (ex: 22) - se for Enter, assume 1
4. Categoria (escolhe da lista ou cria uma nova na hora)
5. Descricao
6. Data (ou Enter pra hoje)

**Categorias que ja vem prontas:**
- Despesa: Alimentacao, Transporte, Moradia, Saude, Educacao, Lazer, Contas, Outros
- Receita: Salario, Investimento, Freelance, Presente, Outros

E da pra **criar categoria nova** na hora (ex: "Assinaturas", "Pet"). Depois de criada, ela aparece na lista automaticamente nas proximas vezes.

> Em qualquer campo, digitar `0` **cancela** a operacao e volta pro menu sem salvar nada.

### 7.5 Listar movimentacoes (RF04)
Mostra todas as movimentacoes do usuario, das mais recentes pras mais antigas:
```
#5 | 02/06/2026 | DESPESA | Alimentacao | R$ 20,00 x22 = R$ 440,00/mes | Almoco no trabalho
#3 | 01/06/2026 | DESPESA | Moradia | R$ 1.200,00 | Aluguel
#1 | 01/06/2026 | RECEITA | Salario | R$ 3.500,00 | Salario do mes
```
Quando a frequencia e maior que 1, ele ja mostra o total no mes (valor x vezes).

### 7.6 Editar movimentacao (RF03)
Escolhe o id e aparece um **menu de campos**. Mostra os dados atuais e voce muda so o que quiser (valor, frequencia, tipo, categoria, descricao ou data). So salva quando voce mandar salvar.
```
Editando a movimentacao #4:
  Valor:      R$ 600,00
  Vezes/mes:  1 (total R$ 600,00/mes)
  ...
O que voce quer mudar?
1 - Valor
2 - Quantas vezes por mes
...
```

### 7.7 Excluir movimentacao (RF03)
Escolhe o id, o sistema pede confirmacao (s/n) antes de apagar.

### 7.8 Ver saldo (RF06)
Mostra o total de receitas, o total de despesas e o saldo (receitas - despesas), **com base no que voce realmente registrou**.
```
Total de receitas: R$ 4.300,00
Total de despesas: R$ 1.593,00
Saldo atual: R$ 2.707,00
```

### 7.9 Relatorios (RF08)
Mostra os totais e o gasto **por categoria**, com o percentual de cada uma:
```
Gastos por categoria:
- Moradia: R$ 1.200,00 (75,3%)
- Contas: R$ 200,00 (12,6%)
- Saude: R$ 100,00 (6,3%)
```

### 7.10 Projecao de gastos do mes (RF09) - DIFERENCIAL
Aqui entra a sacada da **frequencia**. Em vez de adivinhar, o sistema calcula o gasto mensal exato: **valor x quantas vezes acontece no mes**. Despesa "1 vez" (tipo aluguel) nao e multiplicada; despesa recorrente (tipo almoco) sim.
```
Suas despesas do mes (valor x quantas vezes acontece no mes):
- Aluguel (Moradia): R$ 1.200,00 x 1 = R$ 1.200,00/mes
- Almoco no trabalho (Alimentacao): R$ 20,00 x 22 = R$ 440,00/mes
- Cafezinho (Alimentacao): R$ 8,00 x 30 = R$ 240,00/mes

>> Gasto mensal previsto: R$ 2.530,00
>> Sua renda mensal: R$ 4.300,00
>> No fim do mes deve sobrar R$ 1.770,00. Mandou bem!
```

### 7.11 Sugestao de economia / Analise (RF10) - DIFERENCIAL
O sistema faz um diagnostico financeiro completo:
- mostra renda, gastos e sobra do mes
- calcula a **taxa de comprometimento** (quanto % da renda foi gasto) e a **taxa de poupanca**
- da um **diagnostico** (otimo / saudavel / atencao / risco / no vermelho)
- lista **onde da pra economizar**, por categoria, com quanto da pra cortar e quanto isso economiza **por mes e por ano**

As despesas sao classificadas em:
- **essenciais** (Moradia, Saude, Educacao, Contas) - sugere cortar 10%
- **flexiveis** (o resto) - sugere cortar 20% (e mais facil de cortar)

```
Renda do mes:   R$ 4.300,00
Gastos do mes:  R$ 2.530,00
Sobra do mes:   R$ 1.770,00
Voce gasta 58,8% da sua renda e poupa 41,2%.

Diagnostico: Saudavel. Voce usou 58,8% da sua renda. Da pra apertar um pouco e poupar mais.

Onde da pra economizar:
1) Moradia (essencial) - hoje R$ 1.200,00
   Cortando 10% voce economiza R$ 120,00/mes (R$ 1.440,00/ano)
2) Alimentacao (flexivel) - hoje R$ 680,00
   Cortando 20% voce economiza R$ 136,00/mes (R$ 1.632,00/ano)

>> Potencial total de economia: R$ 336,00 por mes, ou R$ 4.032,00 no ano!
```

### 7.12 Exportar CSV (RF07)
Gera um arquivo CSV (que abre no Excel/Google Sheets) com todas as movimentacoes. Salva na pasta `excel/` com o nome do usuario e a data:
```
excel/extrato_teste_05-06-2026.csv
```

---

## 8. Diferenciais (o que faz o projeto se destacar)

1. **Senha com hash de verdade** (SHA-256 + salt) - nem quem abre o banco le a senha.
2. **Frequencia mensal** - o usuario diz quantas vezes por mes acontece cada gasto, e o sistema calcula o gasto mensal exato.
3. **Projecao inteligente** - separa gasto que acontece 1 vez de gasto recorrente, entao a previsao faz sentido.
4. **Analise financeira com diagnostico** - nao so mostra numeros, fala onde economizar com calculo de quanto da pra guardar no ano.
5. **Categorias personalizadas** - alem das prontas, da pra criar as suas, e elas viram recorrentes.
6. **Cancelar de qualquer lugar** - digitando `0` em qualquer pergunta a operacao e cancelada com seguranca.
7. **Tudo em Real (R$)** - valores formatados no padrao brasileiro.
8. **Migracao automatica do banco** - se o banco for de uma versao antiga, o sistema se ajusta sozinho.
9. **Separacao por usuario** - cada um so ve as proprias movimentacoes.

---

## 9. Roteiro sugerido pra demonstrar no slide

Uma sequencia que mostra o sistema todo, do simples ao impressionante:

1. **Login** - entra com `teste` / `Teste$1` (ja tem dados).
2. **Listar (opcao 2)** - mostra as movimentacoes, repara nas que tem "x22 = R$ 440/mes".
3. **Saldo (opcao 5)** - mostra o saldo real.
4. **Relatorios (opcao 6)** - mostra os gastos por categoria com %.
5. **Projecao (opcao 7)** - AQUI brilha: o gasto mensal previsto com a conta de cada despesa.
6. **Analise (opcao 8)** - o diagnostico e as dicas de economia com valor por ano.
7. **Registrar (opcao 1)** - cadastra um gasto novo e mostra a pergunta "quantas vezes por mes?".
8. **Exportar CSV (opcao 9)** - mostra o arquivo gerado na pasta `excel/`.
9. **Seguranca** - abre o `financeiro.db` e mostra que a senha esta embaralhada (hash), nao em texto.

---

## 10. Estrutura de pastas (resumo visual)

```
best-financeiro/
├── run.sh / RunInWindows.bat   # rodar o sistema
├── seed.sh                     # popular com dados de teste
├── financeiro.db               # banco (gerado sozinho)
├── lib/                        # driver do SQLite
├── docs/                       # documentacao em HTML
├── excel/                      # CSVs exportados (gerado)
└── src/main/java/
    ├── App.java
    ├── model/      (Usuario, Read, Projecao, AnaliseFinanceira, Sugestao)
    ├── DAO/        (UsuarioDAO, CreateDAO, ReadDAO, UpdateDAO, DeleteDAO, RelatorioDAO, GastosMensaisDAO)
    ├── service/    (Financas)
    └── utils/      (Conexao, Hash, Moeda, DataUtil, ExportadorCSV, CancelamentoException)
```
