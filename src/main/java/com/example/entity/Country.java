package com.example.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Schema
@Table(
	name = "countries",
	indexes = {
		@Index(name = "idx__countries__code_alpha_2", columnList = "code_alpha_2"),
		@Index(name = "idx__countries__code_alpha_3", columnList = "code_alpha_3"),
	},
	uniqueConstraints = {
		@UniqueConstraint(name = "uc__countries__code_alpha_2__code_alpha_3", columnNames = {"code_alpha_2", "code_alpha_3"}),
	}
)
@Access(AccessType.FIELD)
public class Country extends Base implements CountryEntityInterface
{

	private static final long serialVersionUID = 155304980711420579L;

	@NotNull
	@Column(name = "name", length = 255, nullable = false)
	private String name;

	@NotNull
	@Column(name = "code_alpha_2", length = 2, nullable = false)
	private String codeAlpha2;

	@NotNull
	@Column(name = "code_alpha_3", length = 3, nullable = false)
	private String codeAlpha3;

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public Country setName(String name)
	{
		this.name = name;

		return this;
	}

	@Override
	public String getCodeAlpha2()
	{
		return codeAlpha2;
	}

	@Override
	public Country setCodeAlpha2(String codeAlpha2)
	{
		this.codeAlpha2 = codeAlpha2;

		return this;
	}

	@Override
	public String getCodeAlpha3()
	{
		return codeAlpha3;
	}

	@Override
	public Country setCodeAlpha3(String codeAlpha3)
	{
		this.codeAlpha3 = codeAlpha3;

		return this;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (! (o instanceof Country)) return false;
		if (! super.equals(o)) return false;

		Country country1 = (Country) o;

		return Objects.equals(name, country1.name) &&
			Objects.equals(codeAlpha2, country1.codeAlpha2) &&
			Objects.equals(codeAlpha3, country1.codeAlpha3);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), name, codeAlpha2, codeAlpha3);
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
			.append("id", id)
			.append("name", name)
			.append("codeAlpha2", codeAlpha2)
			.append("codeAlpha3", codeAlpha3)
			.toString();
	}

}
