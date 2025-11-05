package com.ncu.hospital.apigateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceCredentialsRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ServiceCredentials getCredentialsByServiceName(String serviceName) {
        String sql = "SELECT username, password FROM service_credentials WHERE service_name = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{serviceName}, (rs, rowNum) -> {
            return new ServiceCredentials(rs.getString("username"), rs.getString("password"));
        });
    }
}
