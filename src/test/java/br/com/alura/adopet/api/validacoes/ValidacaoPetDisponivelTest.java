package br.com.alura.adopet.api.validacoes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.PetRepository;

@ExtendWith(MockitoExtension.class)
public class ValidacaoPetDisponivelTest {
	
	@InjectMocks
	private ValidacaoPetDisponivel validacao;	
	@Mock
    private PetRepository petRepository;
	@Mock
	private Pet pet;
	@Mock
	private SolicitacaoAdocaoDto dto;
	
	@Test
	@DisplayName("Permitir solicitacao de adocao")
	void AdocaoPetCenario1() {			
		
		BDDMockito.given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
		BDDMockito.given(pet.getAdotado()).willReturn(false);
		
		//tem q passar como lambda
		Assertions.assertDoesNotThrow(() -> validacao.validar(dto));
		
	}
	
	@Test
	@DisplayName("Não Permitir solicitacao de adocao")
	void AdocaoPetCenario0() {			
		
		BDDMockito.given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
		BDDMockito.given(pet.getAdotado()).willReturn(true);
		
		//tem q passar como lambda
		Assertions.assertThrows(ValidacaoException.class, () -> validacao.validar(dto));
		
	}

}
