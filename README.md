A aplicação foi implementada na versão 8 do java.

A importação do arquivo csv será feita via API, 
basta chamar a api (POST) "/api/movie/import" (Postman) enviando o arquivo na requisição.
Caso o arquivo não seja enviado na requisição, a aplicação tentará importar à partir de um arquivo do disco, 
o caminho deste arquivo está no método "loadContentFromFile()" da classe MovieService.

Na importação do arquivo, os dados das tabelas do banco de dados serão deletados e preenchidos novamente.

A API que retorna os intervalos das premiações conforme a solicitação é a (GET) "/api/movie/getProducersIntervals"

APIs implementadas:

(CRUD)
/api/producer
    /list - GET (Retorna a listagem completa de Produtores.)
    /{id} - GET (Consulta um Produtore por id.)
    /insert - POST (Cria um novo Produtore.)
    /update - PUT (Atualiza o Produtore.)
    /delete/{id} - DELETE (Deleta o Produtore.)

(CRUD)
/api/movie
    /list - GET (Retorna a listagem completa de Filmes.)
    /{id} - GET (Consulta um Filme por id.)
    /insert - POST (Cria um novo Filme.)
    /update - PUT (Atualiza o Filme.)
    /delete/{id} - DELETE (Deleta o Filme.)

(adicionais)
    /import - POST (Importa arquivo csv com a listagem de Filmes)
              Enviar arqiuvo .csv na requisição da API (MultipartFile).
    /getProducersIntervals - GET (Retorna o Produtor com maior intervalo de premiação.)
    
