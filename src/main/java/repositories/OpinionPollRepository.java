package repositories;

import models.OpinionPoll;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpinionPollRepository extends CrudRepository<OpinionPoll, Long> {

    public OpinionPoll findByCode(String code);

}