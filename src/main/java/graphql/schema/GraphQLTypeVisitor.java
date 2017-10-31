/*
 * Copyright 2016 Intuit Inc. All rights reserved. Unauthorized reproduction
 * is a violation of applicable law. This material contains certain
 * confidential or proprietary information and trade secrets of Intuit Inc.
 * 
 * graphql.schema.GraphQLTypeVisitor.java
 * 
 * Created: Oct 30, 2017 5:57:03 PM
 * Author: gkesler
 */
package graphql.schema;

import graphql.PublicApi;

/**
 *
 * @author gkesler
 */
@PublicApi
public interface GraphQLTypeVisitor<U> {
    U visit (GraphQLEnumType type, U data);
    U visit (GraphQLInputObjectType type, U data);
    U visit (GraphQLInterfaceType type, U data);
    U visit (GraphQLList type, U data);
    U visit (GraphQLNonNull type, U data);
    U visit (GraphQLObjectType type, U data);
    U visit (GraphQLScalarType type, U data);
    U visit (GraphQLTypeReference type, U data);
    U visit (GraphQLUnionType type, U data);
}
