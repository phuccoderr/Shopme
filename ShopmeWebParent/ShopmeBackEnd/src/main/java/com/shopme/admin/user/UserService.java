package com.shopme.admin.user;

import com.shopme.common.entity.Category;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService {
    public static final int USER_PER_PAGE = 10;
    @Autowired private UserRepository repo;
    @Autowired private RoleRepository roleRepo;
    @Autowired private PasswordEncoder passwordEncoder;


    public List<User> listUser() {
        return (List<User>) repo.findAll();
    }

    public Page<User> listByPage(int pageNum, String sortField,String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1 ,USER_PER_PAGE,sort);

        if (keyword != null) {
            return repo.findAll(keyword,pageable);
        }
        return repo.findAll(pageable);
    }
    public User get(Integer id) throws UserNotFoundException {
        try {
            return repo.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new UserNotFoundException("Cloud not find any user with ID:" + id);
        }
    }

    public List<Role> listRoles() {
        return (List<Role>) roleRepo.findAll();
    }

    public User save(User user) {
        boolean isUpdatingUser = (user.getId() != null);
        if (isUpdatingUser) {
            User userInDB = repo.findById(user.getId()).get();
            if (user.getPassword() == null) {
                user.setPassword(userInDB.getPassword());
            } else {
                encodePassword(user);
            }
            if (user.getPhoto() == null) {
                user.setPhoto(userInDB.getPhoto());
            }
        }

        if ( user.getPhoto() == null) {
            user.setPhoto("");
            if (!isUpdatingUser) {
                user.setPhoto("");
            } else {
                User userInDB = repo.findById(user.getId()).get();
                user.setPhoto(userInDB.getPhoto());
            }
        }
        return repo.save(user);
    }

    private void encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public void updateEnabled(Integer id,boolean enabled) {
        repo.enabled(id,enabled);
    }

    public User getUserByEmail(String email) {
        return repo.getUserByEmail(email);
    }

    public boolean isEmailUnique(Integer id,String email) {
        User userByEmail = repo.getUserByEmail(email);
        if(userByEmail == null) return true;

        boolean isCreatingNew = (id == null);
        if (isCreatingNew) {
            if (userByEmail != null) return false;
        } else {
            if (userByEmail.getId() != id) return false;
        }
        return true;
    }

    public void delete(Integer id) throws UserNotFoundException {
        User user = repo.findById(id).get();
        if (user == null || user.getId() == 0) {
            throw new UserNotFoundException("Cloud not find any user  with ID:" + id);
        }
        repo.deleteById(id);
    }
}
