package RU.MEPHI.ICIS.C17501.messenger.db.repo;

import RU.MEPHI.ICIS.C17501.messenger.db.dao.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {

    @Query(value = "SELECT ud.registration_id " +
            "FROM messenger.users_devices ud " +
            "WHERE ud.telephone_number = ?1", nativeQuery = true)
    List<String> findAllUserDevices(String userTelNumber);
}
