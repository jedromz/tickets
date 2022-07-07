package pl.kurs.tickets.mapping;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import pl.kurs.tickets.controller.OffenseDictionaryController;
import pl.kurs.tickets.controller.PersonController;
import pl.kurs.tickets.model.Offense;
import pl.kurs.tickets.model.dto.OffenseDto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class OffenseToOffenseDto implements Converter<Offense, OffenseDto> {
    @Override
    public OffenseDto convert(MappingContext<Offense, OffenseDto> mappingContext) {
        Offense offense = mappingContext.getSource();
        OffenseDto offenseDto = OffenseDto.builder()
                .id(offense.getId())
                .charge(offense.getCharge())
                .deleted(offense.isDeleted())
                .version(offense.getVersion())
                .build();
        offenseDto.add(linkTo(methodOn(OffenseDictionaryController.class).getByName(offense.getOffenseDictionary().getName()))
                .withRel("offense-dictionary-entry"));
        return offenseDto;
    }
}
