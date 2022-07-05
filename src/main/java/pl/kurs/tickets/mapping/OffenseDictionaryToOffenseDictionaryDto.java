package pl.kurs.tickets.mapping;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kurs.tickets.controller.PersonController;
import pl.kurs.tickets.controller.TicketController;
import pl.kurs.tickets.model.OffenseDictionary;
import pl.kurs.tickets.model.Person;
import pl.kurs.tickets.model.dto.OffenseDictionaryDto;
import pl.kurs.tickets.model.dto.PersonDto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class OffenseDictionaryToOffenseDictionaryDto implements Converter<OffenseDictionary, OffenseDictionaryDto> {

    @Override
    public OffenseDictionaryDto convert(MappingContext<OffenseDictionary, OffenseDictionaryDto> mappingContext) {
        OffenseDictionary offenseDictionary = mappingContext.getSource();
        OffenseDictionaryDto dto = OffenseDictionaryDto.builder()
                .id(offenseDictionary.getId())
                .name(offenseDictionary.getName())
                .minCharge(offenseDictionary.getMinCharge())
                .maxCharge(offenseDictionary.getMaxCharge())
                .points(offenseDictionary.getPoints())
                .deleted(offenseDictionary.isDeleted())
                .version(offenseDictionary.getVersion())
                .build();
        return dto;
    }
}
