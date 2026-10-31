package steps;

import com.networknt.schema.ValidationMessage;
import io.cucumber.java.PendingException;
import io.cucumber.java.pt.*;
import model.ErrorMessageModel;
import org.junit.Assert;
import services.CadastroEntregasService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CadastroEntregasSteps {

    CadastroEntregasService service =  new CadastroEntregasService();

    @Dado("que eu tenha os seguintes dados de entrega:")
    public void queEuTenhaOsSeguintesDadosDeEntrega(List<Map<String, String>> rows) {
        for (Map<String, String> columns : rows) {
            service.setFieldsDelivery(columns.get("campo"), columns.get("valor"));
        }
    }

    @Quando("eu enviar a requisição para o endpoint {string} de cadastro de entregas")
    public void euEnviarARequisiçãoParaOEndpointDeCadastroDeEntregas(String endpoint) {
        service.createDelivery(endpoint);
    }

    @Então("o status code da resposta deve ser {int}")
    public void oStatusCodeDaRespostaDeveSer(int statusCode) {
        Assert.assertEquals(statusCode, service.response.statusCode());
    }

    @E("a resposta de mensagem de erro deve ser {string}")
    public void aRespostaDeMensagemDeErroDeveSer(String mensagem) {
        ErrorMessageModel errorMessage = service.gson.fromJson(
                service.response.jsonPath().prettify(),  ErrorMessageModel.class);

        Assert.assertEquals(mensagem, errorMessage.getMessage());
    }

    @Dado("que eu recupere o ID criado no contexto")
    public void queEuRecupereOIDCriadoNoContexto() {
        service.retrieveId();
    }

    @Quando("eu enviar o ID na requisição para o endpoint {string} de exclusão de entregas")
    public void euEnviarOIDNaRequisiçãoParaOEndpointDeExclusãoDeEntregas(String endpoint) {
        service.deleteDelivery(endpoint);
    }


    @E("que o arquivo de contrato esperado é o {string}")
    public void queOArquivoDeContratoEsperadoÉO(String contrato) throws IOException {
        service.setContrato(contrato);
    }

    @Então("a resposta da requisição deve estar em conformidade com o contrato selecionado")
    public void aRespostaDaRequisiçãoDeveEstarEmConformidadeComOContratoSelecionado() throws IOException {
        // verifica se o contrato está com algum erro
        Set<ValidationMessage> validateResponse = service.validateResponseAgainstSchema();
        // faz o assert validando se o contrato está de acordo com o esperado
        Assert.assertTrue("O contrato está errado. Erros encontrados: " + validateResponse, validateResponse.isEmpty());
    }
}
