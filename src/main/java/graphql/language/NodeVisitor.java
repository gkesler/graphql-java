/*
 * Copyright 2016 Intuit Inc. All rights reserved. Unauthorized reproduction
 * is a violation of applicable law. This material contains certain
 * confidential or proprietary information and trade secrets of Intuit Inc.
 * 
 * graphql.language.NodeVisitor.java
 * 
 * Created: Oct 30, 2017 5:39:22 PM
 * Author: gkesler
 */
package graphql.language;

import graphql.Internal;
import graphql.PublicApi;

/**
 *
 * @author gkesler
 */
@PublicApi
public interface NodeVisitor<U> {
    U visit (Argument node, U data);
    U visit (BooleanValue node, U data);
    U visit (Directive node, U data);
    U visit (DirectiveDefinition node, U data);
    U visit (DirectiveLocation node, U data);
    U visit (Document node, U data);
    U visit (EnumTypeDefinition node, U data);
    U visit (EnumValue node, U data);
    U visit (EnumValueDefinition node, U data);
    U visit (Field node, U data);
    U visit (FieldDefinition node, U data);
    U visit (FloatValue node, U data);
    U visit (FragmentDefinition node, U data);
    U visit (FragmentSpread node, U data);
    U visit (InlineFragment node, U data);
    U visit (InputObjectTypeDefinition node, U data);
    U visit (InputValueDefinition node, U data);
    U visit (IntValue node, U data);
    U visit (InterfaceTypeDefinition node, U data);
    U visit (ListType node, U data);
    U visit (NonNullType node, U data);
    U visit (NullValue node, U data);
    U visit (ObjectField node, U data);
    U visit (ObjectTypeDefinition node, U data);
    U visit (ObjectValue node, U data);
    U visit (OperationDefinition node, U data);
    @Internal
    U visit (OperationTypeDefinition node, U data);
    U visit (ScalarTypeDefinition node, U data);
    U visit (SchemaDefinition node, U data);
    U visit (SelectionSet node, U data);
    U visit (StringValue node, U data);
    U visit (TypeName node, U data);
    U visit (UnionTypeDefinition node, U data);
    U visit (VariableDefinition node, U data);
    U visit (VariableReference node, U data);
}
