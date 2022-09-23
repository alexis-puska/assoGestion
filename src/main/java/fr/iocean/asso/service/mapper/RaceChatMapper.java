package fr.iocean.asso.service.mapper;

import fr.iocean.asso.domain.RaceChat;
import fr.iocean.asso.service.dto.RaceChatDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RaceChat} and its DTO {@link RaceChatDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RaceChatMapper extends EntityMapper<RaceChatDTO, RaceChat> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RaceChatDTO toDtoId(RaceChat raceChat);
}
