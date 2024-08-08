package br.com.alura.adopet.api.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validacoes.ValidacaoSolicitacaoAdocao;

@ExtendWith(MockitoExtension.class)
public class AdocaoServiceTest {
	
    @InjectMocks
    private AdocaoService service;

    @Mock
    private AdocaoRepository repository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private TutorRepository tutorRepository;
    // Mock -> simula o comportamento da class
    @Mock
    private EmailService emailService;
    //Spy -> simula um objeto e mantem seu comportamento original, porem é possivel mudar o comportamento de seus metodos(um "duble" com comportamentos originais)
    @Spy
    private List<ValidacaoSolicitacaoAdocao> validacoes = new ArrayList<>();    
    @Mock
    private ValidacaoSolicitacaoAdocao validador1;
    @Mock
    private ValidacaoSolicitacaoAdocao validador2;
    @Mock
    private Pet pet;

    @Mock
    private Tutor tutor;

    @Mock
    private Abrigo abrigo;
   
    private SolicitacaoAdocaoDto dto;
    // para capturar uma class, no caso a Adocao
    @Captor
    private ArgumentCaptor<Adocao> adocaoCaptor;
	
	
	@Test
	@DisplayName("Deve Salvar a solicitação da Adoção")
	void solicitarAdoção01(){
		//Arrange
		this.dto = new SolicitacaoAdocaoDto(10l, 20l, "motivo qualquer");
		BDDMockito.given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
		BDDMockito.given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
		BDDMockito.given(pet.getAbrigo()).willReturn(abrigo);		
		
		//Act
		service.solicitar(dto);
		
		//Assert
		BDDMockito.then(repository).should().save(adocaoCaptor.capture());
		Adocao adocaoSalva = adocaoCaptor.getValue();
		Assertions.assertEquals(pet, adocaoSalva.getPet());
		Assertions.assertEquals(tutor, adocaoSalva.getTutor());
		Assertions.assertEquals(dto.motivo(), adocaoSalva.getMotivo());
		
	}
	
	@Test
	@DisplayName("Deve chamar validadores de adoção")
	void solicitarAdoção02(){
		//Arrange
		this.dto = new SolicitacaoAdocaoDto(10l, 20l, "motivo qualquer");
		BDDMockito.given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
		BDDMockito.given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
		BDDMockito.given(pet.getAbrigo()).willReturn(abrigo);		
		validacoes.add(validador1);
		validacoes.add(validador2);
		
		//Act
		service.solicitar(dto);
		
		//Assert
		BDDMockito.then(validador1).should().validar(dto);
		BDDMockito.then(validador2).should().validar(dto);
		
	}

}
