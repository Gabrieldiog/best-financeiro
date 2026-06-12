DROP TABLE IF EXISTS movimentacoes;
DROP TABLE IF EXISTS usuarios;

CREATE TABLE usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    senha TEXT NOT NULL
);

CREATE TABLE movimentacoes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_usuario INTEGER NOT NULL,
    tipo TEXT NOT NULL,
    valor REAL NOT NULL,
    descricao TEXT,
    data_movimentacao TEXT NOT NULL,
    categoria TEXT NOT NULL,
    frequencia INTEGER NOT NULL DEFAULT 1,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

INSERT INTO usuarios (id, username, senha) VALUES
(1, 'teste', 'Pv11sgje6ca7t+sTdPi+ug==:nG5ibd+ZYv91YZ+LV53CGDgoBUeDBbDpWQ+4lSV3rA0=');

INSERT INTO movimentacoes (id, id_usuario, tipo, valor, descricao, data_movimentacao, categoria, frequencia) VALUES
(1, 1, 'RECEITA', 3500.0, 'Salario do mes', '2026-06-01', 'Salario', 1),
(2, 1, 'RECEITA', 800.0, 'Projeto freela', '2026-06-03', 'Freelance', 1),
(3, 1, 'DESPESA', 1200.0, 'Aluguel', '2026-06-01', 'Moradia', 1),
(4, 1, 'DESPESA', 200.0, 'Conta de luz', '2026-06-01', 'Contas', 1),
(5, 1, 'DESPESA', 20.0, 'Almoco no trabalho', '2026-06-02', 'Alimentacao', 22),
(6, 1, 'DESPESA', 15.0, 'Corridas de app', '2026-06-03', 'Transporte', 20),
(7, 1, 'DESPESA', 8.0, 'Cafezinho', '2026-06-04', 'Alimentacao', 30),
(8, 1, 'DESPESA', 100.0, 'Academia', '2026-06-02', 'Saude', 1),
(9, 1, 'DESPESA', 50.0, 'Netflix', '2026-06-04', 'Assinaturas', 1);
