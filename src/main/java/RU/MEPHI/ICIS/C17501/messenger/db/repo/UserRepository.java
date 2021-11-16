package RU.MEPHI.ICIS.C17501.messenger.db.repo;

import RU.MEPHI.ICIS.C17501.messenger.db.dao.User;
import RU.MEPHI.ICIS.C17501.messenger.db.projection.UserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query(value="select u.telephone_number as telephoneNumber," +
            "       u.login as login," +
            "       u.first_name as firstName," +
            "       u.second_name as secondName," +
            "       u.date_of_birth as dateOfBirth," +
            "       u.photo_url as photoUrl," +
            "       u.is_deleted as isDeleted," +
            "       u.is_locked as isBlocked," +
            "       u.gender as gender," +
            "       r.name as roleName " +
            "from messenger.users u left join  messenger.roles_users_mapping m on u.telephone_number=m.telephone_number" +
            " left join  messenger.roles r on m.id_role=r.id_role",nativeQuery = true)
public List<UserProjection> findAllByProjection();




}
