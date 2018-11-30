package ar.com.grupoesfera.demo.aceptacion;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.apache.http.HttpStatus;

import com.grupoesfera.demo.integration.domain.Client;

public class DefinicionDeSteps {
	
	@Given("no existe el cliente {string}")
	public void noExisteElCliente(String nombreCliente) {
		Response respuesta =  get("http://172.17.0.4:8080/clients/v1.0/?name=" + nombreCliente);
		
		if (respuesta.statusCode() == HttpStatus.SC_OK) {
			Client cliente = respuesta.as(Client.class);
			System.out.println(cliente);
			given()
				.body(cliente)
				.contentType(ContentType.JSON).
			delete("http://172.17.0.4:8080/clients/v1.0").
			then()
				.statusCode(HttpStatus.SC_OK);
		}
		
	}

	@When("creo el cliente {string}")
	public void creoElCliente(String nombreCliente) {
		Client cliente = new Client();
		cliente.setName(nombreCliente);
		cliente.setLastName("Prueba");

		given()
			.body(cliente)
			.contentType(ContentType.JSON).
		when()
			.post("http://172.17.0.4:8080/clients/v1.0").
		then()
			.statusCode(HttpStatus.SC_OK);	
	}

	@Then("el listado de clientes incluye a {string}")
	public void elListadoDeClientesIncluyeA(String nombreCliente) {
		when()
			.get("http://172.17.0.4:8080/clients/v1.0/?name=" + nombreCliente).
		then()
			.statusCode(HttpStatus.SC_OK)
			.assertThat().body(containsString(nombreCliente));
	}
}
