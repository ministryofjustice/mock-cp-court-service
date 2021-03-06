package uk.gov.justice.digital.court.crimeportal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.justice.digital.court.crimeportal.data.api.CourtList;
import uk.gov.justice.digital.court.crimeportal.data.api.CourtListSummary;
import uk.gov.justice.digital.court.crimeportal.data.entity.DocumentType;
import uk.gov.justice.digital.court.crimeportal.service.CourtListService;
import uk.gov.justice.digital.court.crimeportal.service.DataGenerator;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.time.LocalDate;

@RestController
@Slf4j
public class CourtListController {
    private final CourtListService courtListService;
    private final DataGenerator dataGenerator;

    public CourtListController(CourtListService courtListService, DataGenerator dataGenerator) {
        this.courtListService = courtListService;
        this.dataGenerator = dataGenerator;
    }

    @RequestMapping(value="/court/{court}/list", produces=MediaType.APPLICATION_XML_VALUE)
    public CourtList courtList(
            @PathVariable String court,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam("date") LocalDate date) {

        log.info("Court list requested for court {} for date {}", court, date);
        var savedCourtList = courtListService.courtList(court, date);
        if (savedCourtList.getSessions().getSession().isEmpty()) {
            return courtListService.save(court, date,  dataGenerator.generateOf(court, date));
        }
        return savedCourtList;
    }

    @PostMapping(value="/court/{court}/list", produces=MediaType.APPLICATION_XML_VALUE)
    public DocumentType createCourtList(
            @PathVariable String court,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam("date") LocalDate date) {

        log.info("Court list is being created for court {} for date {}", court, date);
        return dataGenerator.generateOf(court, date);
    }

    @RequestMapping(value="/court/list", produces=MediaType.APPLICATION_XML_VALUE)
    public CourtList courtList() {

        log.info("Court list requested for all court lists");
        return courtListService.allCourtLists();
    }

    @PostMapping(value="/court/list", produces=MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<CourtListSummary> load(@RequestParam("file") MultipartFile uploadfile)  {
            log.info(String.format("Received call upload %s", uploadfile.getOriginalFilename()));
        try {
            return new ResponseEntity<>(courtListService.load(uploadfile.getInputStream()), HttpStatus.CREATED);
        } catch (JAXBException e) {
            log.error("unable to parse xml", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            log.error("unable to read xml", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value="/", produces=MediaType.APPLICATION_XML_VALUE)
    public String deleteAll()  {
        courtListService.deleteAll();
        return "All records have been deleted";
    }
}
