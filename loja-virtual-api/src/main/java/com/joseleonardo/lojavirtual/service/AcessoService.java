package com.joseleonardo.lojavirtual.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joseleonardo.lojavirtual.model.Acesso;
import com.joseleonardo.lojavirtual.repository.AcessoRepository;

@Service
public class AcessoService {

	@Autowired
	private AcessoRepository acessoRepository;
	
	public Acesso salvar(Acesso acesso) {
		/* Qualque tipo de validação */
		return acessoRepository.save(acesso);
	}
	
}
