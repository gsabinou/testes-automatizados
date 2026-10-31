#language: pt

  @regressivo
Funcionalidade: Validar contrato de cadastro de entrega

  Cenário: Validar o contrato ao realizar um cadastro de entrega bem sucedido
    Dado que eu tenha os seguintes dados de entrega:
      | campo          | valor        |
      | numeroPedido   | 1            |
      | nomeEntregador | Ana Silva    |
      | statusEntrega  | EM_SEPARACAO |
      | dataEntrega    | 2024-08-22   |
    Quando eu enviar a requisição para o endpoint "/entregas" de cadastro de entregas
    Então o status code da resposta deve ser 201
    E que o arquivo de contrato esperado é o "Cadastro bem-sucedido de entrega"
    Então a resposta da requisição deve estar em conformidade com o contrato selecionado