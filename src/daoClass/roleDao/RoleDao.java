package daoClass.roleDao;

import connection.connectionManager.ConnectionManager;
import daoInterfaces.CrudInterface;
import dto.roleFilter.RoleFilter;
import entity.roleEntity.RoleEntity;
import exception.FindByException.FindByIdException;
import exception.deleteMethodException.DeleteMethodException;
import exception.findAllException.FindAllException;
import exception.saveMethodException.SaveMethodException;
import exception.updateMethodException.UpdateMethodException;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RoleDao implements CrudInterface<Integer, RoleEntity> {

    private static RoleDao instance;
    private static final String ROLE_ID = "id";
    private static final String ROLE = "role";
    private static final String DELETE_SQL = """
            DELETE FROM role
            WHERE id = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO role(id, role)
            VALUES(?,?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE role
                SET role =?
            WHERE id = ?
            """;
    private static final String FIND_ALL_SQL = """
            SELECT
                id,
                role
            FROM role
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;

    public RoleDao() {
    }

    public List<RoleEntity> findAll(RoleFilter roleFilter) {
        List<Object> parametersRole = new ArrayList<>();
        List<String> whereSqlRole = new ArrayList<>();
        if (roleFilter
                    .getRole() != null) {
            whereSqlRole.add("role LIKE ?");
            parametersRole.add(roleFilter.getRole());
        }

        parametersRole.add(roleFilter.getLimit());
        parametersRole.add(roleFilter.getOffset());
        var whereRole = whereSqlRole.stream()
                .collect(Collectors.joining(" AND ", " WHERE ", " LIMIT ? OFFSET ? "));
        var sqlRole = FIND_ALL_SQL + whereRole;
        try (Connection connection = ConnectionManager.get();
             var prepareStatement = connection.prepareStatement(sqlRole)) {
            for (int i = 0; i < parametersRole.size(); i++) {
                prepareStatement.setObject(i + 1, parametersRole.get(i));
            }
            var resultSet = prepareStatement.executeQuery();
            List<RoleEntity> roles = new ArrayList<>();
            while (resultSet.next()) {
                roles.add(buildRole(resultSet));
            }
            return roles;
        } catch (SQLException e) {
            throw new FindAllException(e, " Exception in method findAll with parameters RoleFilter roleFilter in class RoleDao ");
        }
    }

    public List<RoleEntity> findAll() {
        try (var connection = ConnectionManager.get();
             var prepareStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = prepareStatement.executeQuery();
            List<RoleEntity> roles = new ArrayList<>();
            while (resultSet.next()) {
                roles.add(buildRole(resultSet));
            }
            return roles;
        } catch (SQLException e) {
            throw new FindAllException(e, " Exception in findAll method in RoleDao class");
        }
    }

    public Optional<RoleEntity> findById(Integer id) {
        try (var connection = ConnectionManager.get();
             var prepareStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            prepareStatement.setInt(1, id);

            var resultSet = prepareStatement.executeQuery();
            RoleEntity roleEntity = null;
            if (resultSet.next()) {
                roleEntity = buildRole(resultSet);
            }
            return Optional.ofNullable(roleEntity);
        } catch (SQLException e) {
            throw new FindByIdException(e, " Exception in findById method in RoleDao class");
        }
    }

    private static RoleEntity buildRole(ResultSet resultSet) throws SQLException {
        return RoleEntity
                .builder()
                .id(resultSet.getInt(ROLE_ID))
                .role(resultSet.getString(ROLE))
                .build();
    }

    public void update(RoleEntity roleEntity) {
        try (var connection = ConnectionManager.get();
             var prepareStatement = connection.prepareStatement(UPDATE_SQL)) {
            prepareStatement.setString(1, roleEntity.getRole());
            prepareStatement.setInt(2, roleEntity.getId());

            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            throw new UpdateMethodException(e, " Exception in update method in RoleDao class");
        }
    }

    public RoleEntity save(RoleEntity roleEntity) {
        try (Connection connection = ConnectionManager.get();
             var prepareStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            prepareStatement.setInt(1, roleEntity.getId());
            prepareStatement.setString(2, roleEntity.getRole());

            prepareStatement.executeUpdate();
            var generatedKeys = prepareStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                roleEntity.setId(generatedKeys.getInt("id"));
            }
            return roleEntity;
        } catch (SQLException e) {
            throw new SaveMethodException(e, " Exception in save method in RoleDao class");
        }
    }

    public boolean delete(Integer id) {
        try (Connection connection = ConnectionManager.get();
             var prepareStatement = connection.prepareStatement(DELETE_SQL)) {
            prepareStatement.setInt(1, id);
            return prepareStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DeleteMethodException(e, " Exception in delete method in RoleDao class.");
        }

    }

    public static synchronized RoleDao getInstance() {
        if (instance == null) {
            instance = new RoleDao();
        }
        return instance;
    }
}
