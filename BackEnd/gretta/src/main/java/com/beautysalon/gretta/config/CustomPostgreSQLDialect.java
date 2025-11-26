package com.beautysalon.gretta.config;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class CustomPostgreSQLDialect extends PostgreSQLDialect {
    
    public CustomPostgreSQLDialect() {
        super();
    }
    
    @Override
    public void initializeFunctionRegistry(FunctionContributions functionContributions) {
        super.initializeFunctionRegistry(functionContributions);
        
        // Registrar funciones personalizadas si es necesario
    }
}
