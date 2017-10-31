/*
 * Copyright 2016 Intuit Inc. All rights reserved. Unauthorized reproduction
 * is a violation of applicable law. This material contains certain
 * confidential or proprietary information and trade secrets of Intuit Inc.
 * 
 * graphql.schema.GraphQLTypeVisitorStub.java
 * 
 * Created: Oct 30, 2017 6:51:40 PM
 * Author: gkesler
 */
package graphql.schema;

/**
 *
 * @author gkesler
 */
public abstract class GraphQLTypeVisitorStub<U> implements GraphQLTypeVisitor<U> {
    @Override
    public U visit(GraphQLEnumType type, U data) {
        return visitType(type, data);
    }

    @Override
    public U visit(GraphQLInputObjectType type, U data) {
        return visitType(type, data);
    }

    @Override
    public U visit(GraphQLInterfaceType type, U data) {
        return visitType(type, data);
    }

    @Override
    public U visit(GraphQLList type, U data) {
        return visitType(type, data);
    }

    @Override
    public U visit(GraphQLNonNull type, U data) {
        return visitType(type, data);
    }

    @Override
    public U visit(GraphQLObjectType type, U data) {
        return visitType(type, data);
    }

    @Override
    public U visit(GraphQLScalarType type, U data) {
        return visitType(type, data);
    }

    @Override
    public U visit(GraphQLTypeReference type, U data) {
        return visitType(type, data);
    }

    @Override
    public U visit(GraphQLUnionType type, U data) {
        return visitType(type, data);
    }

    protected U visitType (GraphQLType type, U data) {
        return data;
    }
    
    @Override
    public U leave(GraphQLType type, U data) {
        return data;
    }    
}
