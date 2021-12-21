package com.example.entity;

import java.io.Serializable;

public interface BaseEntityInterface<E> extends Serializable
{

	E setId(Long id);

	Long getId();
}
