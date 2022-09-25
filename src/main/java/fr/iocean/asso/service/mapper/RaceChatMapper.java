package fr.iocean.asso.service.mapper;

import fr.iocean.asso.domain.RaceChat;
import fr.iocean.asso.service.dto.RaceChatDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link RaceChat} and its DTO {@link RaceChatDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RaceChatMapper extends EntityMapper<RaceChatDTO, RaceChat> {}
