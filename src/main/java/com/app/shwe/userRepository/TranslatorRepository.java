package com.app.shwe.userRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.shwe.model.Translator;

public interface TranslatorRepository extends JpaRepository<Translator, Integer>{

}
