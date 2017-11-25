package app.controllers;

import app.models.Opinion;
import app.repositories.TemporaryDAO;
import app.util.OpinionService;
import app.util.Util;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

@Controller
public class ApplicationController {

    private static final Logger log = LoggerFactory.getLogger(ApplicationController.class);

    private final TemporaryDAO temporaryDAO;

    @Autowired
    public ApplicationController(TemporaryDAO temporaryDAO) {
        this.temporaryDAO = temporaryDAO;
    }

    @RequestMapping(path = "/", method = RequestMethod.GET, produces = "text/html")
    public String index() {
        return "/index.html";
    }

    @RequestMapping(path = "/getnewcode", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getNewCode() {
        return ResponseEntity.ok().body(Util.getInstance().toJson(Util.getInstance().generateCode()));
    }

    @RequestMapping(path = "/makeopinionpoll", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity makeNewOpinionPoll(@RequestBody ObjectNode body) {
        String code = body.get("code").textValue();

        Opinion opinion = temporaryDAO.findOpinionByCode(code);

        if (opinion == null) {
            opinion = OpinionService.getInstance().makeOpinionTemplate(code, temporaryDAO);
        }

        return ResponseEntity.ok().body(Util.getInstance().toJson(opinion));
    }

    @RequestMapping(path = "/saveopinionpoll", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity saveOpinionPoll(@RequestBody ObjectNode body) {

        Opinion opinion = Util.getInstance().fromJson(body.toString(), Opinion.class);
        if (opinion != null && opinion.getId() != null) {
            opinion = (Opinion) temporaryDAO.save(Opinion.class, opinion, opinion.getId());
            return ResponseEntity.ok().body(Util.getInstance().toJson(opinion));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @RequestMapping(path = {"/getopinionpoll/{publicCode}/{test}", "/getopinionpoll/{publicCode}"}, method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getOpinionPoll(@PathVariable String publicCode, @PathVariable boolean test) {

        Opinion opinion = temporaryDAO.findOpinionByPublicCode(publicCode);

        if (opinion == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        if (!test) {
            opinion = OpinionService.getInstance().makeOpinionFromTemplate(opinion, temporaryDAO);
        }

        return ResponseEntity.ok().body(Util.getInstance().toJson(opinion));
    }

    @RequestMapping(path = {"/remove/{type}/{id}"}, method = RequestMethod.POST)
    public ResponseEntity removeEntity(@PathVariable String type, @PathVariable Long id) {

        switch (type) {
            case "section":
                temporaryDAO.removeSection(id);
                break;
            case "question":
                temporaryDAO.removeQuestion(id);
                break;
            case "answer":
                temporaryDAO.removeAnswer(id);
                break;
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}