package com.shopme.admin.user;

import com.shopme.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User,Integer>, PagingAndSortingRepository<User,Integer> {

    @Query(value = "select u from User u where u.email = :email")
    public User getUserByEmail(@Param("email") String email);

    public long countById(Integer id);
    @Query(value = "UPDATE User u set u.enabled = ?2 where u.id = ?1")
    @Modifying
    public void enabled(Integer id,boolean enabled);

    @Query(value = "SELECT u FROM User u WHERE u.email like %?1% or " +
            "u.lastName like %?1% or " +
            "u.firstName like %?1%")
    public Page<User> findAll(String keyword, Pageable pageable);

}
