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
    @Mapping(target = "contrat", source = "contrat", qualifiedByName = "id")
    //@Mapping(target = "famille", source = "famille", qualifiedByName = "id")
    @Mapping(target = "adresseCapture", source = "adresseCapture", qualifiedByName = "id")
    @Mapping(target = "race", source = "race", qualifiedByName = "id")
    ChatDTO toDto(Chat s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ChatDTO toDtoId(Chat chat);
}
