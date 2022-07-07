package pl.kurs.tickets.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.tickets.model.OffenseDictionary;
import pl.kurs.tickets.model.command.CreateOffenseDictionaryEntryCommand;
import pl.kurs.tickets.model.command.UpdateOffenseDictionaryEntryCommand;
import pl.kurs.tickets.model.dto.OffenseDictionaryDto;
import pl.kurs.tickets.service.OffenseDictionaryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/offense-dictionary")
@RequiredArgsConstructor
public class OffenseDictionaryController {
    private final OffenseDictionaryService offenseDictionaryService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<OffenseDictionaryDto> postOffenseDictionaryEntry(@RequestBody @Valid CreateOffenseDictionaryEntryCommand command) {
        OffenseDictionary entry = offenseDictionaryService.saveEntry(command);
        OffenseDictionaryDto dto = modelMapper.map(entry, OffenseDictionaryDto.class);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity deleteOffenseDictionaryEntry(@PathVariable String name) {
        offenseDictionaryService.deleteByName(name);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{name}")
    public ResponseEntity<OffenseDictionaryDto> putDictionaryEntry(@PathVariable String name
            , UpdateOffenseDictionaryEntryCommand command) {
        OffenseDictionary entry = offenseDictionaryService.editByName(name, command);
        return new ResponseEntity<>(modelMapper.map(entry, OffenseDictionaryDto.class), HttpStatus.CREATED);
    }

    @GetMapping("/{name}")
    public ResponseEntity<OffenseDictionaryDto> getByName(@PathVariable String name) {
        OffenseDictionary entry = offenseDictionaryService.getByName(name);
        OffenseDictionaryDto dto = modelMapper.map(entry, OffenseDictionaryDto.class);
        return ResponseEntity.ok(dto);
    }
}
