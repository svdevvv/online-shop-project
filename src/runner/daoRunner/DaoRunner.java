package runner.daoRunner;

import daoClass.roleDao.RoleDao;
import daoClass.userDao.UserDao;
import dto.roleFilter.RoleFilter;
import dto.userFilter.UserFilter;
import entity.roleEntity.RoleEntity;
import entity.userEntity.UserEntity;

import java.util.List;
import java.util.Optional;

public class DaoRunner {
    private static final RoleDao ROLE_DAO = RoleDao.getInstance();
    private static final UserDao USER_DAO = UserDao.getInstance();


    public static void main(String[] args) {

        findByFilterForUser();
    }

    public static void deleteForUser() {
        UserDao userDao = UserDao.getInstance();
        boolean delete = userDao.delete(5);
        System.out.println(delete);
    }


    public static void saveForUser() {
        List<RoleEntity> roles = ROLE_DAO.findAll();
        RoleEntity roleEntity = roles.isEmpty() ? roles.getLast() : roles.get(0);

        System.out.println(roleEntity);

        if (roleEntity != null) {
            UserEntity userEntity = new UserEntity();
            userEntity.setLogin("Babas");

            userEntity.setPassword("polkovodec");
            userEntity.setRole(roleEntity);

            UserEntity save = USER_DAO.save(userEntity);
            System.out.println(save);
        }
    }

    public static void saveForRole() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(3);
        roleEntity.setRole("GHOST");

        RoleEntity save = ROLE_DAO.save(roleEntity);
        System.out.println(save);
    }

    public static void findById() {
        Optional<RoleEntity> findById = ROLE_DAO.findById(1);
        System.out.println(findById);
    }

    public static void findAll() {
        List<RoleEntity> findAll = ROLE_DAO.findAll();
        System.out.println(findAll);
    }

    private static void findByFilterForRole() {
        RoleFilter user = new RoleFilter(3, 0, "USER");
        List<RoleEntity> all = ROLE_DAO.findAll(user);
        System.out.println(all);
    }
    private static void findByFilterForUser() {
        UserFilter user = new UserFilter(3, 0, "ADMIN");
        List<UserEntity> all = USER_DAO.findAll(user);
        System.out.println(all);
    }

    public static void deleteForRole() {
        boolean delete = ROLE_DAO.delete(1);
        System.out.println(delete);
    }
}
