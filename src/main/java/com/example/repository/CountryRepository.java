package com.example.repository;

import com.example.entity.Country;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.PageableRepository;

@Repository
public interface CountryRepository extends PageableRepository<Country, Long>
{

}
