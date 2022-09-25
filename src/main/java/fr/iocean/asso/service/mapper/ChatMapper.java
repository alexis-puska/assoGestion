package fr.iocean.asso.service.mapper;

import fr.iocean.asso.domain.Chat;
import fr.iocean.asso.service.dto.ChatDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Chat} and its DTO {@link ChatDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { ContratMapper.class, FamilleAccueilMapper.class, PointCaptureMapper.class, RaceChatMapper.class }
)
public interface ChatMapper extends EntityMapper<ChatDTO, Chat> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ChatDTO toDtoId(Chat chat);
}
