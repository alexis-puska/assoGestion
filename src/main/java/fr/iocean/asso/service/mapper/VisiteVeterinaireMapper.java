package fr.iocean.asso.service.mapper;

import fr.iocean.asso.domain.ActeVeterinaire;
import fr.iocean.asso.domain.Chat;
import fr.iocean.asso.domain.VisiteVeterinaire;
import fr.iocean.asso.service.dto.VisiteVeterinaireDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper for the entity {@link VisiteVeterinaire} and its DTO {@link VisiteVeterinaireDTO}.
 */
@Mapper(componentModel = "spring", uses = { CliniqueVeterinaireMapper.class, ChatMapper.class, ActeVeterinaireMapper.class })
public interface VisiteVeterinaireMapper extends EntityMapper<VisiteVeterinaireDTO, VisiteVeterinaire> {
    @Mapping(target = "chatId", source = "chat.id")
    VisiteVeterinaireDTO toDto(VisiteVeterinaire s);

    @Mapping(target = "chat", source = "chatId")
    VisiteVeterinaire toEntity(VisiteVeterinaireDTO s);

    default Chat fromChatId(Long id) {
        if (id == null) {
            return null;
        }
        Chat chat = new Chat();
        chat.setId(id);
        return chat;
    }

    @AfterMapping
    default void after(@MappingTarget VisiteVeterinaire visite) {
        if (visite.getActes() != null) {
            for (ActeVeterinaire acte : visite.getActes()) {
                acte.setVisiteVeterinaire(visite);
            }
        }
    }
}
