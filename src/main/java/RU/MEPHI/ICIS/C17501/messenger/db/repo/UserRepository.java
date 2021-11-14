package RU.MEPHI.ICIS.C17501.messenger.db.repo;

import RU.MEPHI.ICIS.C17501.messenger.db.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
