#language: pt

Funcionalidade: Deletar uma entrega

#  O contexto roda antes do cenário sempre
  Contexto: Cadastro de entregas bem-sucedido
    Dado que eu tenha os seguintes dados de entrega:
      | campo          | valor        |
      | numeroPedido   | 1            |
      | nomeEntregador | Ana Silva    |
      | statusEntrega  | EM_SEPARACAO |
      | dataEntrega    | 2024-08-22   |
    Quando eu enviar a requisição para o endpoint "/entregas" de cadastro de entregas
    Então o status code da resposta deve ser 201

  Cenário: Deve ser possível deletar uma entrega
    Dado que eu recupere o ID criado no contexto
    Quando eu enviar o ID na requisição para o endpoint "/entregas" de exclusão de entregas
    Entao o status code da resposta deve ser 400