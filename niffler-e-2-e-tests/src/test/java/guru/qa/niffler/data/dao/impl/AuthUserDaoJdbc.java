package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import guru.qa.niffler.config.Config;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class AuthUserDaoJdbc implements AuthUserDao {

    private static final Config CFG = Config.getInstance();

    private static final PasswordEncoder pe
            = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public AuthUserEntity createAuthUser(AuthUserEntity user) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "INSERT INTO user (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                        "VALUES ( ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            String encodedPassword = pe.encode(user.getPassword());
            user.setPassword(encodedPassword);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setBoolean(3, user.getEnabled());
            ps.setBoolean(4, user.getAccountNonExpired());
            ps.setBoolean(5, user.getAccountNonLocked());
            ps.setBoolean(6, user.getCredentialsNonExpired());
            ps.executeUpdate();
            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            user.setId(generatedKey);
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" WHERE id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    AuthUserEntity ue = new AuthUserEntity();
                    ue.setId(rs.getObject("id", UUID.class));
                    ue.setUsername(rs.getString("username"));
                    ue.setPassword(rs.getString("password"));
                    ue.setEnabled((rs.getBoolean("enabled")));
                    ue.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                    ue.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                    ue.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
                    return Optional.of(ue);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" WHERE username = ?"
        )) {
            ps.setObject(1, username);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    AuthUserEntity ue = new AuthUserEntity();
                    ue.setId(rs.getObject("id", UUID.class));
                    ue.setUsername(rs.getString("username"));
                    ue.setPassword(rs.getString("password"));
                    ue.setEnabled((rs.getBoolean("enabled")));
                    ue.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                    ue.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                    ue.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
                    return Optional.of(ue);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthUserEntity> findAll() {
        List<AuthUserEntity> authUsers = new ArrayList<>();
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\""
        )) {
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    AuthUserEntity ue = new AuthUserEntity();
                    ue.setId(rs.getObject("id", UUID.class));
                    ue.setUsername(rs.getString("username"));
                    ue.setPassword(rs.getString("password"));
                    ue.setEnabled((rs.getBoolean("enabled")));
                    ue.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                    ue.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                    ue.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));

                    authUsers.add(ue);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return authUsers;
    }

    @Override
    public void deleteAuthUser(AuthUserEntity user) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "DELETE FROM user WHERE id = ?"
        )) {
            ps.setObject(1, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}