package com.tosi.chat.repository;

import com.tosi.chat.dto.TaleDetailDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaleDetailDtoRedisRepository extends CrudRepository<TaleDetailDto, String> {
}
