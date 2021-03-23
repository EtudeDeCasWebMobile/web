package com.amboucheba.etudeDeCasWeb.Repositories;

import com.amboucheba.etudeDeCasWeb.Models.Entities.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);

    // For testing purpose (users are not deletable)
    @Modifying
    @Transactional
    @Query( value = "UPDATE collections set owner_id = NULL WHERE owner_id= :id ;DELETE FROM users WHERE id= :id ;", nativeQuery = true)
    void deleteByIdAndSetCollectionOwnershipToNull(@Param("id")long id);
}
