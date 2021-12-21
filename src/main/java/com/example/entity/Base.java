package com.example.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

@MappedSuperclass
public abstract class Base implements BaseEntityInterface
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	protected Long id;

	/**
	 * Compares only object id
	 *
	 * @param a
	 * @param b
	 *
	 * @return
	 */
	public static boolean equals(Base a, Base b)
	{
		if (a == null && b == null) {
			return true;
		}

		if (a != null && b != null && a.getId().equals(b.getId())) {
			return true;
		}

		return false;
	}

	@Override
	public Long getId()
	{
		return id;
	}

	@Override
	public Base setId(Long id)
	{
		this.id = id;

		return this;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (! (o instanceof Base)) return false;
		Base base = (Base) o;

		return Objects.equals(id, base.id);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id);
	}
}
