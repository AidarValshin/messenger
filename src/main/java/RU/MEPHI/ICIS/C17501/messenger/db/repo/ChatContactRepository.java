package RU.MEPHI.ICIS.C17501.messenger.db.repo;

import RU.MEPHI.ICIS.C17501.messenger.db.dao.ChatContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatContactRepository extends JpaRepository<ChatContact, Long> {
    public List<ChatContact> findAllByTelephoneNumber(String telephoneNumber);

    public List<ChatContact> findByTelephoneNumberAndChatId(String telephoneNumber, Long chatId);


}
