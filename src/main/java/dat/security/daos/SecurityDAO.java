package dat.security.daos;


import dat.security.entities.Role;
import dat.security.entities.User;
import dat.security.exceptions.ApiException;
import dat.security.exceptions.ValidationException;
import dk.bugelhartmann.UserDTO;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Purpose: To handle security in the API
 * Author: Thomas Hartmann
 */
public class SecurityDAO implements ISecurityDAO {

    private static ISecurityDAO instance;
    private static EntityManagerFactory emf;

    public SecurityDAO(EntityManagerFactory _emf) {
        emf = _emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public UserDTO getVerifiedUser(String username, String password) throws ValidationException {
        try (EntityManager em = getEntityManager()) {
            User user = em.find(User.class, username);
            if (user == null)
                throw new EntityNotFoundException("No user found with username: " + username); //RuntimeException
            user.getRoles().size(); // force roles to be fetched from db
            if (!user.verifyPassword(password))
                throw new ValidationException("Wrong password");
            return new UserDTO(user.getUsername(), user.getRoles().stream().map(r -> r.getRoleName()).collect(Collectors.toSet()));
        }
    }

    @Override
    public User createUser(String username, String password) {
        try (EntityManager em = getEntityManager()) {
            User userEntity = em.find(User.class, username);
            if (userEntity != null)
                throw new EntityExistsException("User with username: " + username + " already exists");
            userEntity = new User(username, password);
            em.getTransaction().begin();
            Role userRole = em.find(Role.class, "USER");
            Role adminRole = em.find(Role.class, "ADMIN");
            if (username.equals("admin")) {
                // Check if admin role exists, if not create it
                if (adminRole == null) {
                    adminRole = new Role("ADMIN");
                    em.persist(adminRole);
                }
                userEntity.addRole(adminRole);
            } else {
                // Check if user role exists, if not create it
                if (userRole == null) {
                    userRole = new Role("USER");
                    em.persist(userRole);
                }
                userEntity.addRole(userRole);
            }
            em.persist(userEntity);
            em.getTransaction().commit();
            return userEntity;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(400, e.getMessage());
        }
    }


    /*@Override
    public User createUserOG(String username, String password) {
        try (EntityManager em = getEntityManager()) {
            User userEntity = em.find(User.class, username);
            if (userEntity != null)
                throw new EntityExistsException("User with username: " + username + " already exists");
            userEntity = new User(username, password);
            em.getTransaction().begin();
            Role userRole = em.find(Role.class, "user");
            if (userRole == null)
                userRole = new Role("user");
            em.persist(userRole);
            userEntity.addRole(userRole);
            em.persist(userEntity);
            em.getTransaction().commit();
            return userEntity;
        }catch (Exception e){
            e.printStackTrace();
            throw new ApiException(400, e.getMessage());
        }
    }*/

    @Override
    public User addRole(UserDTO userDTO, String newRole) {
        try (EntityManager em = getEntityManager()) {
            User user = em.find(User.class, userDTO.getUsername());
            if (user == null)
                throw new EntityNotFoundException("No user found with username: " + userDTO.getUsername());
            em.getTransaction().begin();
                Role role = em.find(Role.class, newRole);
                if (role == null) {
                    role = new Role(newRole);
                    em.persist(role);
                }
                user.addRole(role);
                //em.merge(user);
            em.getTransaction().commit();
            return user;
        }
    }

    // In SecurityDAO.java
    public List<UserDTO> getAllUsers() {
        try (EntityManager em = getEntityManager()) {
            List<User> users = em.createQuery("SELECT u FROM User u", User.class).getResultList();
            return users.stream().map(u -> new UserDTO(u.getUsername(), u.getRoles().stream().map(r -> r.getRoleName()).collect(Collectors.toSet()))).collect(Collectors.toList());
        }
    }


    public User updateUserRole(String username, String newRole) {
        try (EntityManager em = getEntityManager()) {
            User user = em.find(User.class, username);
            if (user == null) {
                throw new EntityNotFoundException("No user found with username: " + username);
            }
            em.getTransaction().begin();
            Role role = em.find(Role.class, newRole);
            if (role == null) {
                role = new Role(newRole);
                em.persist(role);
            }
            user.getRoles().clear(); // Clear existing roles
            user.addRole(role); // Add new role
            em.getTransaction().commit();
            return user;
        }
    }
}

