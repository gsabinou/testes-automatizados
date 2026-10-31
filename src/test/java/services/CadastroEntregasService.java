package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.EntregaModel;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

import static io.restassured.RestAssured.given;

public class CadastroEntregasService {

    EntregaModel entregaModel = new EntregaModel();

    public Gson gson  = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public Response response;

    String baseUrl = "https://api-entregas-e08b1747db7d.herokuapp.com/";

    String idDelivery;

    String schemasPath = "src/test/resources/schemas/";
    JSONObject jsonSchema;
    private final ObjectMapper mapper = new ObjectMapper();

    public void setFieldsDelivery(String field, String value) {
        switch (field) {
            case "numeroPedido" -> entregaModel.setNumeroPedido(Integer.parseInt(value));
            case "nomeEntregador" -> entregaModel.setNomeEntregador(value);
            case "statusEntrega" -> entregaModel.setStatusEntrega(value);
            case "dataEntrega" -> entregaModel.setDataEntrega(value);
            default -> throw new IllegalStateException("Unexpected field: " + field);
        }
    }

    public void createDelivery(String endpoint) {
        String url = baseUrl + endpoint;
        String body = gson.toJson(entregaModel);

        response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(body)
                .when()
                .post(url)
                .then()
                .extract().response();
    }

    public void retrieveId() {
        idDelivery = String.valueOf(gson.fromJson(response.jsonPath().prettify(), EntregaModel.class)
                .getNumeroPedido());

    }

    public void deleteDelivery(String endpoint) {
        String url = baseUrl + endpoint;

        response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .delete(url)
                .then()
                .extract().response();
    }

    private JSONObject loadJsonFromFile(String filePath) throws IOException {
        try (InputStream inputStream = Files.newInputStream(Paths.get(filePath))) {
            JSONTokener tokener = new JSONTokener(inputStream);
            return new JSONObject(tokener);
        }
    }

    public void setContrato(String nomeContrato) throws IOException {
        switch (nomeContrato) {
            case "Cadastro bem-sucedido de entrega" -> jsonSchema = loadJsonFromFile(schemasPath + "CadastroEntregaBemSucedida.json");
            default -> throw new IllegalStateException("Unexpected contract: " + nomeContrato);

        }
    }

    public Set<ValidationMessage> validateResponseAgainstSchema() throws IOException {

        // obter o corpo da resposta como String e converter para JSONObject
        JSONObject jsonResponse = new JSONObject(response.getBody().asString());

        // configurar o JsonSchemaFactory e criar o JsonSchema
        JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        JsonSchema schema = schemaFactory.getSchema(jsonSchema.toString());

        // converter o JSON da resposta para JsonNode
        JsonNode jsonResponseNode = mapper.readTree(jsonResponse.toString());

        // validar o JSON da resposta contra o schema
        Set<ValidationMessage> schemaValidationErrors = schema.validate(jsonResponseNode);

        return schemaValidationErrors;

    }

}
