package daoClass.userDao;

import connection.connectionManager.ConnectionManager;
import daoInterfaces.CrudInterface;
import dto.userFilter.UserFilter;
import entity.roleEntity.RoleEntity;
import entity.userEntity.UserEntity;
import exception.FindByException.FindByIdException;
import exception.deleteMethodException.DeleteMethodException;
import exception.findAllException.FindAllException;
import exception.saveMethodException.SaveMethodException;
import exception.updateMethodException.UpdateMethodException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserDao implements CrudInterface<Integer, UserEntity> {
    private static UserDao instance;
    private static final String PERSON_ID = "id";
    private static final String PERSON_LOGIN = "login";
    private static final String PERSON_PASSWORD = "password";
    private static final String ROLE = "role";


    private static final String DELETE_SQL = """
            DELETE FROM "user"
            WHERE id = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO "user"(login, password, role_id)
            VALUES (?,?,?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE "user"
            SET login = ?,
                password = ?,
                role_id = ?
            WHERE id = ?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT "user".id,
            login,
            password,
            role
            FROM "user"
            JOIN role r
                on r.id = "user".role_id
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE "user".id = ?
            """;

    private UserDao() {

    }

    @Override
    public boolean delete(Integer id) {
        try (var connection = ConnectionManager.get();
             var prepareStatement = connection.prepareStatement(DELETE_SQL)) {
            prepareStatement.setInt(1, id);
            return prepareStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DeleteMethodException(e, " Exception in delete methode in UserDao class");
        }
    }

    @Override
    public UserEntity save(UserEntity entity) {
        try (var connection = ConnectionManager.get();
             var prepareStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
//
            prepareStatement.setString(1, entity.getLogin());
            prepareStatement.setString(2, entity.getPassword());
            prepareStatement.setInt(3, entity.getRole().getId());

            prepareStatement.executeUpdate();
            var generatedKeys = prepareStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getInt("id"));
            }
            return entity;
        } catch (SQLException e) {
            throw new SaveMethodException(e, " Exception in save methode in UserDao class");
        }
    }

    @Override
    public void update(UserEntity entity) {
        try (var connection = ConnectionManager.get();
             var prepareStatement = connection.prepareStatement(UPDATE_SQL)) {
            if (entity.getLogin() != null) {
                prepareStatement.setString(1, entity.getLogin());
            }
            if (entity.getPassword() != null) {
                prepareStatement.setString(2, entity.getPassword());
            }
            if (entity.getRole() != null) {
                prepareStatement.setInt(3, entity.getRole().getId());
            }

            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            throw new UpdateMethodException(e, " Exception in update method in UserDao class");
        }

    }

    @Override
    public Optional<UserEntity> findById(Integer id) {
        try (var connection = ConnectionManager.get();
             var prepareStatement = connection.prepareStatement(FIND_BY_ID_SQL + id)) {
            prepareStatement.setInt(1, id);

            ResultSet resultSet = prepareStatement.executeQuery();
            UserEntity userEntity = null;
            if (resultSet.next()) {
                userEntity = buildUser(resultSet);
            }
            return Optional.ofNullable(userEntity);
        } catch (SQLException e) {
            throw new FindByIdException(e, " Exception in findById method in UserDao class");
        }
    }
    public List<UserEntity> findAll(UserFilter userFilter){
        List<Object> parametersUser = new ArrayList<>();
        List<String> whereSqlUser = new ArrayList<>();
        if(userFilter
                .getRole() != null){
            whereSqlUser.add(" role LIKE ?");
            parametersUser.add(userFilter.getRole());
        }
        parametersUser.add(userFilter.getLimit());
        parametersUser.add(userFilter.getOffset());
        var whereUser = whereSqlUser.stream()
                .collect(Collectors.joining(" AND ", " WHERE ", " LIMIT ? OFFSET ?"));
       var sqlUser =  FIND_ALL_SQL + whereUser;
        try (Connection connection = ConnectionManager.get();
        var prepareStatement = connection.prepareStatement(sqlUser)) {
            for (int i = 0; i < parametersUser.size(); i++) {
                prepareStatement.setObject(i + 1, parametersUser.get(i));
            }
            ResultSet resultSet = prepareStatement.executeQuery();
            List<UserEntity> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(buildUser(resultSet));
            }
            return users;
        } catch (SQLException e) {
            throw new FindAllException(e, " Exception in method findAll method with parameters UserFilter userFilter in UserDao class");
        }
    }

    @Override
    public List<UserEntity> findAll() {
        try (var connection = ConnectionManager.get();
             var prepareStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = prepareStatement.executeQuery();
            List<UserEntity> userEntities = new ArrayList<>();
            while (resultSet.next()) {
                UserEntity userEntity = buildUser(resultSet);
                userEntities.add(userEntity);
            }
            return userEntities;
        } catch (SQLException e) {
            throw new FindAllException(e, " Exception in findAll method in UserDao class");
        }
    }

    private UserEntity buildUser(ResultSet resultSet) throws SQLException {
        RoleEntity roleEntity = RoleEntity
                .builder()
                .role(resultSet.getString(ROLE))
                .build();
        return UserEntity
                .builder()
                .id(resultSet.getInt(PERSON_ID))
                .login(resultSet.getString(PERSON_LOGIN))
                .password(resultSet.getString(PERSON_PASSWORD))
                .role(roleEntity)
                .build();
    }

    ;

    public static UserDao getInstance() {
        if (instance == null) {
            return new UserDao();
        }
        return instance;
    }
}