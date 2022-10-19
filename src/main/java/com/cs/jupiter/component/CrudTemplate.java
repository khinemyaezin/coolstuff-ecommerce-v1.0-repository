package com.cs.jupiter.component;


import java.sql.Connection;

import org.springframework.stereotype.Component;

import com.cs.jupiter.model.interfaces.RequestCredential;
import com.cs.jupiter.model.interfaces.ViewResult;

@Component
public interface CrudTemplate <T>{

	public ViewResult<T> save(T data,RequestCredential crd,Connection conn);
	public ViewResult<T> inactive(T data,RequestCredential crd,Connection conn);
	public ViewResult<T> deleteAll(RequestCredential crd,Connection conn);
	public ViewResult<T> deleteById(T data,RequestCredential crd,Connection conn);
	public ViewResult<T> update(T data,RequestCredential crd,Connection conn);
	public ViewResult<T> updateById(T data,RequestCredential crd,Connection conn);
	public ViewResult<T> getAll(T data,RequestCredential crd,Connection conn);
	public ViewResult<T> getById(String id,RequestCredential crd,Connection conn);
	
}
