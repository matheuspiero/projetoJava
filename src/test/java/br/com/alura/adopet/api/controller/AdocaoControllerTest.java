package br.com.alura.adopet.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.service.AdocaoService;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class AdocaoControllerTest {
	// MockMvc -> para disparar requisições
	@Autowired
	private MockMvc mvc;
	// para o spring configurar o objeto e injeta-lo automaticamente
	@MockBean
	private AdocaoService service;
	@Autowired
    private JacksonTester<SolicitacaoAdocaoDto> jsonDto;
	
	@Test
	@DisplayName("Deve devolver codigo 400 para solicitacao de adocao com erro")	
	void solicitarAdocao01() throws Exception {
		//Arrange
		String json = "{}";
		
		//Act
		var response = mvc.perform(
				post("/adocoes")
					.content(json)
					.contentType(MediaType.APPLICATION_JSON)				
		).andReturn().getResponse();
		
		//Assert
		Assertions.assertEquals(400, response.getStatus());
		
	}
	
	@Test
	@DisplayName("Deve devolver codigo 200 para solicitacao de adocao sem erro")
	//utilizando o JacksonTester
	void solicitarAdocao02() throws Exception {
		//Arrange
		
		SolicitacaoAdocaoDto dto = new SolicitacaoAdocaoDto(1l, 1l, "Motivo qualquer");
	    
		//Act
		MockHttpServletResponse response = mvc.perform(
				post("/adocoes")
					.content(jsonDto.write(dto).getJson())
					.contentType(MediaType.APPLICATION_JSON)				
		).andReturn().getResponse();
		
		//Assert
		Assertions.assertEquals(200, response.getStatus());
		assertEquals("Adoção solicitada com sucesso!", response.getContentAsString());
		
	}

}
