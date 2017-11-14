package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.OpinionPoll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import repositories.OpinionPollRepository;
import repositories.QuestionRepository;
import repositories.SectionRepository;
import util.Util;

import java.util.Date;

@Controller
public class ApplicationController {

    private static final Logger log = LoggerFactory.getLogger(ApplicationController.class);

    private final QuestionRepository questionRepository;

    private final OpinionPollRepository opinionPollRepository;

    private final SectionRepository sectionRepository;

    @Autowired
    public ApplicationController(QuestionRepository questionRepository, OpinionPollRepository opinionPollRepository, SectionRepository sectionRepository) {
        this.questionRepository = questionRepository;
        this.opinionPollRepository = opinionPollRepository;
        this.sectionRepository = sectionRepository;
    }

    @RequestMapping(path = "/", method = RequestMethod.GET, produces = "text/html")
    public String index() {
        log.info("index");
        return "/index.html";
    }

    @RequestMapping(path = "/getnewcode", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getNewCode() {
        return ResponseEntity.ok().body(Util.getInstance().toJson(Util.getInstance().generateCode()));
    }

    @RequestMapping(path = "/makeopinionpoll", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity makeNewOpinionPoll(@RequestBody ObjectNode body) {
        String code = body.get("code").textValue();

        OpinionPoll opinionPoll = opinionPollRepository.findByCode(code);

        if (opinionPoll == null) {
            opinionPoll = new OpinionPoll();
            opinionPoll.setCode(code);
            opinionPoll.setPublicCode(Util.getInstance().generateCode());
            opinionPoll.setCreationDate(new Date());
            opinionPoll = opinionPollRepository.save(opinionPoll);
        }

        return ResponseEntity.ok().body(Util.getInstance().toJson(opinionPoll));
    }

    @RequestMapping(path = "/saveopinionpoll", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity saveOpinionPoll(@RequestBody ObjectNode body) {

        OpinionPoll opinionPoll = Util.getInstance().fromJson(body.toString(), OpinionPoll.class);
        if (opinionPoll != null && opinionPoll.getId() != null) {
            opinionPoll = opinionPollRepository.save(opinionPoll);
            return ResponseEntity.ok().body(Util.getInstance().toJson(opinionPoll));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @RequestMapping(path = "/getopinionpoll/{publicCode}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getOpinionPoll(@PathVariable String publicCode) {

        OpinionPoll opinionPoll = opinionPollRepository.findByPublicCode(publicCode);

        if (opinionPoll == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        return ResponseEntity.ok().body(Util.getInstance().toJson(opinionPoll));
    }
}