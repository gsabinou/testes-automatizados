#language: pt

  @regressivo
Funcionalidade: Cadastro de entregas
  Como usuário da API
  quero cadastrar uma nova entrega
  para que o registro seja salvo corretamente

  @padrao
  Cenário: Cadastro de entregas bem-sucedido
    Dado que eu tenha os seguintes dados de entrega:
      | campo          | valor        |
      | numeroPedido   | 1            |
      | nomeEntregador | Ana Silva    |
      | statusEntrega  | EM_SEPARACAO |
      | dataEntrega    | 2024-08-22   |
    Quando eu enviar a requisição para o endpoint "/entregas" de cadastro de entregas
    Então o status code da resposta deve ser 201

  Cenário: Cadastro de entregas sem sucesso ao enviar o campo statusEntrega inválido
    Dado que eu tenha os seguintes dados de entrega:
      | campo          | valor      |
      | numeroPedido   | 1          |
      | nomeEntregador | Ana Silva  |
      | statusEntrega  | EM_VOO     |
      | dataEntrega    | 2024-08-22 |
    Quando eu enviar a requisição para o endpoint "/entregas" de cadastro de entregas
    Então o status code da resposta deve ser 400
    E a resposta de mensagem de erro deve ser "Dados fornecidos estão em formato inválido."