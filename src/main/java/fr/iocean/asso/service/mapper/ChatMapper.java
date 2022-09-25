package fr.iocean.asso.service.mapper;

import fr.iocean.asso.domain.Chat;
import fr.iocean.asso.domain.VisiteVeterinaire;
import fr.iocean.asso.service.dto.ChatDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Mapper for the entity {@link Chat} and its DTO {@link ChatDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        ContratMapper.class, FamilleAccueilMapper.class, PointCaptureMapper.class, RaceChatMapper.class, VisiteVeterinaireMapper.class,
    }
)
public interface ChatMapper extends EntityMapper<ChatDTO, Chat> {
    @AfterMapping
    default void after(@MappingTarget Chat chat) {
        if (chat.getVisites() != null) {
            for (VisiteVeterinaire visite : chat.getVisites()) {
                visite.setChat(chat);
            }
        }
    }
}
