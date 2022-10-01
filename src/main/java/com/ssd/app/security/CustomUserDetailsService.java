package com.ssd.app.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ssd.app.model.utente;
import com.ssd.app.repository.utenteRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private utenteRepo utentiRepository;

	public CustomUserDetailsService(utenteRepo utentiRepository) {
		super();
		this.utentiRepository = utentiRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String matricola) throws UsernameNotFoundException {
		
		utente utente = utentiRepository.findByMatricola(matricola);
		
		if(utente == null) {
			throw new UsernameNotFoundException("The user doesn't exist!");
		}
		
		return new CustomUserDetails(utente);
	}

}